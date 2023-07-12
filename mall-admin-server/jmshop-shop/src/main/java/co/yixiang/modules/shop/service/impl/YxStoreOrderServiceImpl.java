package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.EntityExistException;
import co.yixiang.modules.activity.domain.YxStorePink;
import co.yixiang.modules.activity.repository.YxStorePinkRepository;
import co.yixiang.modules.shop.domain.*;
import co.yixiang.modules.shop.repository.*;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.service.dto.*;
import co.yixiang.modules.shop.service.mapper.YxStoreOrderMapper;
import co.yixiang.mp.service.YxMiniPayService;
import co.yixiang.mp.service.YxPayService;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.QueryHelp;
import co.yixiang.utils.ValidationUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author hupeng
 * @date 2019-10-14
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreOrderServiceImpl implements YxStoreOrderService {

    private final YxStoreOrderRepository yxStoreOrderRepository;
    private final YxStoreOrderCartInfoRepository yxStoreOrderCartInfoRepository;
    private final YxUserRepository userRepository;
    private final YxStorePinkRepository storePinkRepository;
    private final YxStoreProductRepository storeProductRepository;
    private final YxStoreCartRepository yxStoreCartRepository;
    private final YxStoreProductAttrValueRepository yxStoreProductAttrValueRepository;
    private final YxStoreOrderMapper yxStoreOrderMapper;
    private final YxUserBillService yxUserBillService;
    private final YxStoreOrderStatusService yxStoreOrderStatusService;
    private final YxUserService userService;
    private final YxPayService payService;
    private final YxMiniPayService miniPayService;
    private final YxSystemStoreService systemStoreService;
    private final YxSystemStoreStaffService systemStoreStaffService;

    public YxStoreOrderServiceImpl(YxStoreOrderRepository yxStoreOrderRepository, YxStoreOrderCartInfoRepository yxStoreOrderCartInfoRepository, YxUserRepository userRepository,
                                   YxStorePinkRepository storePinkRepository, YxStoreOrderMapper yxStoreOrderMapper, YxUserBillService yxUserBillService,
                                   YxStoreOrderStatusService yxStoreOrderStatusService, YxSystemStoreService systemStoreService, YxStoreCartRepository yxStoreCartRepository,
                                   YxUserService userService, YxPayService payService, YxMiniPayService miniPayService, YxStoreProductRepository storeProductRepository,
                                   YxStoreProductAttrValueRepository yxStoreProductAttrValueRepository, YxSystemStoreStaffService systemStoreStaffService) {
        this.yxStoreOrderRepository = yxStoreOrderRepository;
        this.yxStoreOrderCartInfoRepository = yxStoreOrderCartInfoRepository;
        this.userRepository = userRepository;
        this.storePinkRepository = storePinkRepository;
        this.yxStoreOrderMapper = yxStoreOrderMapper;
        this.yxUserBillService = yxUserBillService;
        this.yxStoreOrderStatusService = yxStoreOrderStatusService;
        this.userService = userService;
        this.payService = payService;
        this.miniPayService = miniPayService;
        this.systemStoreService = systemStoreService;
        this.storeProductRepository = storeProductRepository;
        this.yxStoreCartRepository = yxStoreCartRepository;
        this.yxStoreProductAttrValueRepository = yxStoreProductAttrValueRepository;
        this.systemStoreStaffService = systemStoreStaffService;
    }

    @Override
    public OrderCountDto getOrderCount() {
        //获取所有订单转态为已支付的
        List<CountDto> nameList = yxStoreCartRepository.findCateName();
        System.out.println("nameList:" + nameList);
        Map<String, Integer> childrenMap = new HashMap<>();
        nameList.forEach(i -> {
            if (i != null) {
                if (childrenMap.containsKey(i.getCatename())) {
                    childrenMap.put(i.getCatename(), childrenMap.get(i.getCatename()) + 1);
                } else {
                    childrenMap.put(i.getCatename(), 1);
                }
            }

        });
        List<OrderCountDto.OrderCountData> list = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        childrenMap.forEach((k, v) -> {
            OrderCountDto.OrderCountData orderCountData = new OrderCountDto.OrderCountData();
            orderCountData.setName(k);
            orderCountData.setValue(v);
            columns.add(k);
            list.add(orderCountData);
        });
        OrderCountDto orderCountDto = new OrderCountDto();
        orderCountDto.setColumn(columns);
        orderCountDto.setOrderCountDatas(list);
        return orderCountDto;
    }

    @Override
    public OrderTimeDataDTO getOrderTimeData() {
        int today = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(new Date()));
        int yesterday = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(DateUtil.
                yesterday()));
        int lastWeek = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(DateUtil.lastWeek()));
        int nowMonth = OrderUtil.dateToTimestampT(DateUtil
                .beginOfMonth(new Date()));
        OrderTimeDataDTO orderTimeDataDTO = new OrderTimeDataDTO();

        orderTimeDataDTO.setTodayCount(yxStoreOrderRepository.countByPayTimeGreaterThanEqual(today));
        //orderTimeDataDTO.setTodayPrice(yxStoreOrderRepository.sumPrice(today));

        orderTimeDataDTO.setProCount(yxStoreOrderRepository
                .countByPayTimeLessThanAndPayTimeGreaterThanEqual(today, yesterday));
        //orderTimeDataDTO.setProPrice(yxStoreOrderRepository.sumTPrice(today,yesterday));

        orderTimeDataDTO.setLastWeekCount(yxStoreOrderRepository.countByPayTimeGreaterThanEqual(lastWeek));
        //orderTimeDataDTO.setLastWeekPrice(yxStoreOrderRepository.sumPrice(lastWeek));

        orderTimeDataDTO.setMonthCount(yxStoreOrderRepository.countByPayTimeGreaterThanEqual(nowMonth));
        //orderTimeDataDTO.setMonthPrice(yxStoreOrderRepository.sumPrice(nowMonth));

        orderTimeDataDTO.setUserCount(userRepository.count());
        orderTimeDataDTO.setOrderCount(yxStoreOrderRepository.count());
        orderTimeDataDTO.setPriceCount(yxStoreOrderRepository.sumTotalPrice());
        orderTimeDataDTO.setGoodsCount(storeProductRepository.count());

        return orderTimeDataDTO;
    }

    @Override
    public Map<String, Object> chartCount() {
        Map<String, Object> map = new LinkedHashMap<>();
        int nowMonth = OrderUtil.dateToTimestampT(DateUtil
                .beginOfMonth(new Date()));
        //        map.put("chart", yxStoreOrderRepository.chartList(nowMonth));
        //        map.put("chartT", yxStoreOrderRepository.chartListT(nowMonth));
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(YxStoreOrder resources) {
        if (resources.getPayPrice().doubleValue() < 0) {
            throw new BadRequestException("请输入退款金额");
        }

        if (resources.getPayType().equals("yue")) {
            //修改状态
            resources.setRefundStatus(2);
            resources.setRefundPrice(resources.getPayPrice());
            update(resources);

            //退款到余额
            YxUserDTO userDTO = userService.findById(resources.getUid());
            userRepository.updateMoney(resources.getPayPrice().doubleValue(),
                    resources.getUid());

            YxUserBill userBill = new YxUserBill();
            userBill.setUid(resources.getUid());

            userBill.setLinkId(resources.getId().toString());
            userBill.setPm(1);
            userBill.setTitle("商品退款");
            userBill.setCategory("now_money");
            userBill.setType("pay_product_refund");
            userBill.setNumber(resources.getPayPrice());
            userBill.setBalance(NumberUtil.add(resources.getPayPrice(), userDTO.getNowMoney()));
            userBill.setMark("订单退款到余额");
            userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
            userBill.setStatus(1);
            yxUserBillService.create(userBill);


            YxStoreOrderStatus storeOrderStatus = new YxStoreOrderStatus();
            storeOrderStatus.setOid(resources.getId());
            storeOrderStatus.setChangeType("refund_price");
            storeOrderStatus.setChangeMessage("退款给用户：" + resources.getPayPrice() + "元");
            storeOrderStatus.setChangeTime(OrderUtil.getSecondTimestampTwo());

            yxStoreOrderStatusService.create(storeOrderStatus);
        } else {
            BigDecimal bigDecimal = new BigDecimal("100");
            try {
                if (OrderInfoEnum.PAY_CHANNEL_1.getValue().equals(resources.getIsChannel())) {
                    miniPayService.refundOrder(resources.getOrderId(),
                            bigDecimal.multiply(resources.getPayPrice()).intValue());
                } else {
                    payService.refundOrder(resources.getOrderId(),
                            bigDecimal.multiply(resources.getPayPrice()).intValue());
                }

            } catch (WxPayException e) {
                log.info("refund-error:{}", e.getMessage());
            }

        }


    }

    @Override
    public String orderType(int id, int pinkId, int combinationId, int seckillId,
                            int bargainId, int shippingType) {
        String str = "[普通订单]";
        if (pinkId > 0 || combinationId > 0) {
            YxStorePink storePink = storePinkRepository.findByOrderIdKey(id);
            if (ObjectUtil.isNull(storePink)) {
                str = "[拼团订单]";
            } else {
                switch (storePink.getStatus()) {
                    case 1:
                        str = "[拼团订单]正在进行中";
                        break;
                    case 2:
                        str = "[拼团订单]已完成";
                        break;
                    case 3:
                        str = "[拼团订单]未完成";
                        break;
                    default:
                        str = "[拼团订单]历史订单";
                        break;
                }
            }

        } else if (seckillId > 0) {
            str = "[秒杀订单]";
        } else if (bargainId > 0) {
            str = "[砍价订单]";
        }
        if (shippingType == 2) {
            str = "[核销订单]";
        }
        return str;
    }

    public List<YxStoreOrderDTO> manageList(List<YxStoreOrder> content) {
        List<YxStoreOrderDTO> storeOrderDTOS = new ArrayList<>();
        for (YxStoreOrder yxStoreOrder : content) {
            YxStoreOrderDTO yxStoreOrderDTO = yxStoreOrderMapper.toDto(yxStoreOrder);

            Integer _status = OrderUtil.orderStatus(yxStoreOrder.getPaid(), yxStoreOrder.getStatus(),
                    yxStoreOrder.getRefundStatus());

            //返回门店信息处理
            if (yxStoreOrder.getStoreId() > 0) {
                YxSystemStoreDto systemStoreDto = systemStoreService.findById(yxStoreOrder.getStoreId());
                yxStoreOrderDTO.setStoreName(systemStoreDto.getName());
                yxStoreOrderDTO.setStoreAddress(systemStoreDto.getAddress() + systemStoreDto.getDetailedAddress());
            }

            //订单状态
            String orderStatusStr = OrderUtil.orderStatusStr(yxStoreOrder.getPaid()
                    , yxStoreOrder.getStatus(), yxStoreOrder.getShippingType()
                    , yxStoreOrder.getRefundStatus());

            if (_status == 3) {
                String refundTime = OrderUtil.stampToDate(String.valueOf(yxStoreOrder
                        .getRefundReasonTime()));
                String str = "<b style='color:#f124c7'>申请退款</b><br/>" +
                        "<span>退款原因：" + yxStoreOrder.getRefundReasonWap() + "</span><br/>" +
                        "<span>备注说明：" + yxStoreOrder.getRefundReasonWapExplain() + "</span><br/>" +
                        "<span>退款时间：" + refundTime + "</span><br/>";
                orderStatusStr = str;
            }
            yxStoreOrderDTO.setStatusName(orderStatusStr);

            yxStoreOrderDTO.set_status(_status);

            String payTypeName = OrderUtil.payTypeName(yxStoreOrder.getPayType()
                    , yxStoreOrder.getPaid());
            yxStoreOrderDTO.setPayTypeName(payTypeName);

            yxStoreOrderDTO.setPinkName(orderType(yxStoreOrder.getId()
                    , yxStoreOrder.getPinkId(), yxStoreOrder.getCombinationId()
                    , yxStoreOrder.getSeckillId(), yxStoreOrder.getBargainId(),
                    yxStoreOrder.getShippingType()));

            List<StoreOrderCartInfo> cartInfos = yxStoreOrderCartInfoRepository
                    .findByOid(yxStoreOrder.getId());
            List<StoreOrderCartInfoDTO> cartInfoDTOS = new ArrayList<>();
            for (StoreOrderCartInfo cartInfo : cartInfos) {
                StoreOrderCartInfoDTO cartInfoDTO = new StoreOrderCartInfoDTO();
                cartInfoDTO.setId(cartInfo.getId());
                cartInfoDTO.setCartId(cartInfo.getCartId());
                cartInfoDTO.setOid(cartInfo.getOid());
                cartInfoDTO.setUnique(cartInfo.getUnique());

                JSONObject jsonObject = JSON.parseObject(cartInfo.getCartInfo());
                YxStoreProductAttrValue yxStoreProductAttrValue = new YxStoreProductAttrValue();
                if (!ObjectUtils.isEmpty(jsonObject.get("productAttrUnique"))) {
                    yxStoreProductAttrValue = yxStoreProductAttrValueRepository
                            .findbyUnique(jsonObject.get("productAttrUnique").toString());
                }
                JSONObject jsonObject1 = new JSONObject();
                if (!ObjectUtils.isEmpty(yxStoreProductAttrValue)) {
                    jsonObject1 = JSONObject.parseObject(JSONObject.toJSONString(yxStoreProductAttrValue));
                }
                cartInfoDTO.setCartInfoMap(jsonObject);
                cartInfoDTO.setAttrInfoMap(jsonObject1);

                cartInfoDTOS.add(cartInfoDTO);
            }
            yxStoreOrderDTO.setCartInfoList(cartInfoDTOS);
            YxUserDTO userDTO = userService.findById(yxStoreOrder.getUid());
            yxStoreOrderDTO.setUserDTO(userDTO);
            //返回店长信息
            YxSystemStoreStaffDto systemStoreStaffDto = systemStoreStaffService.findByUids(userDTO.getUid(), userDTO.getSpreadUid());
            if (!ObjectUtils.isEmpty(systemStoreStaffDto)) {
                yxStoreOrderDTO.setSotreUser(systemStoreStaffDto.getSpreadNickname());
            } else {
                yxStoreOrderDTO.setSotreUser(null);
            }

            storeOrderDTOS.add(yxStoreOrderDTO);
        }

       /* Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",storeOrderDTOS);
        map.put("totalElements",page.getTotalElements());*/
        return storeOrderDTOS;
    }

    @Override
    public Map<String, Object> queryAll(YxStoreOrderQueryCriteria criteria, Pageable pageable) {

        Page<YxStoreOrder> page = yxStoreOrderRepository
                .findAll((root, criteriaQuery, criteriaBuilder)
                        -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        List<YxStoreOrderDTO> storeOrderDTOS = new ArrayList<>();
        storeOrderDTOS = manageList(page.getContent());

        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", storeOrderDTOS);
        map.put("totalElements", page.getTotalElements());
        return map;
    }

    @Override
    public List<YxStoreOrderDTO> queryAll(YxStoreOrderQueryCriteria criteria) {
        return yxStoreOrderMapper.toDto(yxStoreOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public YxStoreOrderDTO findById(Integer id) {
        Optional<YxStoreOrder> yxStoreOrder = yxStoreOrderRepository.findById(id);
        //ValidationUtil.isNull(yxStoreOrder,"YxStoreOrder","id",id);
        if (yxStoreOrder.isPresent()) {
            return yxStoreOrderMapper.toDto(yxStoreOrder.get());
        }
        return new YxStoreOrderDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public YxStoreOrderDTO create(YxStoreOrder resources) {
        if (yxStoreOrderRepository.findByUnique(resources.getUnique()) != null) {
            throw new EntityExistException(YxStoreOrder.class, "unique", resources.getUnique());
        }
        return yxStoreOrderMapper.toDto(yxStoreOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(YxStoreOrder resources) {
        Optional<YxStoreOrder> optionalYxStoreOrder = yxStoreOrderRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalYxStoreOrder, "YxStoreOrder", "id", resources.getId());
        YxStoreOrder yxStoreOrder = optionalYxStoreOrder.get();
        YxStoreOrder yxStoreOrder1 = yxStoreOrderRepository.findByUnique(resources.getUnique());
        if (yxStoreOrder1 != null && !yxStoreOrder1.getId().equals(yxStoreOrder.getId())) {
            throw new EntityExistException(YxStoreOrder.class, "unique", resources.getUnique());
        }
        yxStoreOrder.copy(resources);
        yxStoreOrderRepository.save(yxStoreOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        yxStoreOrderRepository.deleteById(id);
    }

    /**
     * 导出订单数据
     *
     * @param queryAll
     * @param response
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public void download(List<YxStoreOrderDTO> queryAll, HttpServletResponse response) throws IOException, ParseException {
        List<Map<String, Object>> list = new ArrayList<>();

        for (YxStoreOrderDTO storeOrderDTO : queryAll) {
            List<StoreOrderCartInfoDTO> storeList = storeOrderDTO.getCartInfoList();
            if (storeList != null) {
                for (StoreOrderCartInfoDTO storeOrderCartInfoDTO : storeList) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    Map proInfo = ((Map) storeOrderCartInfoDTO.getCartInfoMap().get("productInfo"));
                    Map cartInfo = storeOrderCartInfoDTO.getCartInfoMap();
                    Map attrInfo = storeOrderCartInfoDTO.getAttrInfoMap();
                    map.put("订单号", storeOrderDTO.getOrderId());
                    map.put("下单时间", OrderUtil.stampToDate(storeOrderDTO.getAddTime() + ""));

                    map.put("客户编号", storeOrderDTO.getUserDTO().getAccount());
                    map.put("客户名称", storeOrderDTO.getUserDTO().getNickname());
                    map.put("客户类型", storeOrderDTO.getUserDTO().getUserType());
                    map.put("客户手机号码", storeOrderDTO.getUserDTO().getPhone() + '/' + storeOrderDTO.getUserDTO().getTelephone());
                    map.put("是否为推广员", storeOrderDTO.getUserDTO().getIsPromoter());

                    map.put("收货人", storeOrderDTO.getRealName());
                    map.put("联系电话", storeOrderDTO.getUserPhone());
                    map.put("收货地址", storeOrderDTO.getUserAddress());

                    String price = cartInfo.get("truePrice") == null ? "0.00" : cartInfo.get("truePrice").toString();
                    String salse = cartInfo.get("cartNum") == null ? "0" : cartInfo.get("cartNum").toString();
                    BigDecimal postage = storeOrderDTO.getPayPostage() == null ? new BigDecimal(0.00) : storeOrderDTO.getPayPostage();
                    Map cartIn = (Map) cartInfo.get("productInfo");
                    Map skuInfo = new HashMap();
                    if (cartIn != null) {
                        skuInfo = (Map) cartIn.get("attrInfo");
                    }
                    BigDecimal total = new BigDecimal(price).multiply(new BigDecimal(salse));
                    BigDecimal totalNum = new BigDecimal(total.toString()).add(new BigDecimal(postage.toString()));
                    map.put("商品编号", proInfo.get("id"));
                    map.put("商品名称", proInfo.get("storeName"));
                    if (skuInfo != null) {
                        map.put("商品规格", skuInfo.get("suk"));
                    }
                    map.put("单位", cartInfo.get("packaging"));
                    map.put("商品库存", cartInfo.get("trueStock"));

                    map.put("订单状态", storeOrderDTO.getStatusName());
                    map.put("支付状态", storeOrderDTO.getPayTypeName());
                    map.put("单价", price);
                    map.put("订购数量", salse);
                    map.put("小计", total);
                    map.put("配送费用", storeOrderDTO.getPayPostage());
                    map.put("总计", totalNum);
                    map.put("订单备注", storeOrderDTO.getMark());

                    list.add(map);
                }
            }
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<YxStoreOrderDTO> findByIds(List<String> idList) {
        List<YxStoreOrder> byIds = yxStoreOrderRepository.findByIds(idList);
        List<YxStoreOrderDTO> yxStoreOrderDTOS = manageList(byIds);
        return yxStoreOrderDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoCloseOrder() {
        List<YxStoreOrder>orderList=  yxStoreOrderRepository.findUnPayOrders();
        orderList.stream().forEach(order->{
            List<StoreOrderCartInfo> cartInfos = yxStoreOrderCartInfoRepository.findByOid(order.getId());
            cartInfos.stream().forEach(catInfo->{
                YxStoreProduct storeProduct= storeProductRepository.findById(catInfo.getId()).get();
                YxStoreCartQueryVo cart = JSONObject.parseObject(catInfo.getCartInfo(), YxStoreCartQueryVo.class);
                storeProduct.setStock(storeProduct.getStock()+cart.getCartNum());
                storeProductRepository.updateStock(storeProduct.getStock(),storeProduct.getId());
            });

            yxStoreOrderRepository.delete(order);
        });
    }
}
