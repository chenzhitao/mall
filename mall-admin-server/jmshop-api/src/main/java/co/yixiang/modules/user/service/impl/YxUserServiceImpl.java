/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.shop.entity.YxStoreDiscount;
import co.yixiang.modules.shop.service.YxStoreCouponUserService;
import co.yixiang.modules.shop.service.YxStoreDiscountService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxSystemStoreStaffService;
import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxUserLevel;
import co.yixiang.modules.user.mapper.YxUserMapper;
import co.yixiang.modules.user.mapping.SystemUserLevelMapImpl;
import co.yixiang.modules.user.service.YxSystemUserLevelService;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserLevelService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.dto.PromUserDTO;
import co.yixiang.modules.user.web.param.PromParam;
import co.yixiang.modules.user.web.param.YxUserQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemLevelProduct;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxUserServiceImpl extends BaseServiceImpl<YxUserMapper, YxUser> implements YxUserService {

    @Autowired
    private YxUserMapper yxUserMapper;
    @Autowired
    private YxStoreOrderService orderService;
    @Autowired
    private YxSystemConfigService systemConfigService;
    @Autowired
    private YxUserBillService billService;
    @Autowired
    private YxUserLevelService userLevelService;
    @Autowired
    private YxStoreCouponUserService storeCouponUserService;
    @Autowired
    private YxSystemStoreStaffService systemStoreStaffService;
    @Autowired
    private YxStoreDiscountService yxStoreDiscountService;
    @Autowired
    private YxSystemUserLevelService yxSystemUserLevelService;


    /**
     * 获取折扣价格
     *
     * @param price
     * @return
     */
    @Override
    public YxSystemLevelProduct setYxSystemLevelProduct(Double price) {
        YxSystemLevelProduct yxSystemLevelProduct = new YxSystemLevelProduct();
        //折扣
        YxSystemUserLevel yxSystemUserLevel = new YxSystemUserLevel();
        //获取会员等级
        List<YxSystemUserLevel> systemUserLevelList = yxSystemUserLevelService.list(Wrappers.<YxSystemUserLevel>query().lambda().eq(YxSystemUserLevel::getIsShow, 1).eq(YxSystemUserLevel::getIsDel, 0));
        if (systemUserLevelList.size() > 0) {
            //根据会员折扣进行排序
            systemUserLevelList.sort(Comparator.comparing(YxSystemUserLevel::getDiscount));
            //最优会员折扣
            yxSystemUserLevel = systemUserLevelList.get(0);
        }
        yxSystemLevelProduct.setYxSystemUserLevel(yxSystemUserLevel);
        yxSystemLevelProduct.setPrice(price);

        Double discountPrice = price;
        //折扣-价格
        if (yxSystemUserLevel.getDiscount() != null) {
            discountPrice = NumberUtil.mul(NumberUtil.div(yxSystemUserLevel.getDiscount(), 100), price).doubleValue();
        }
        yxSystemLevelProduct.setDiscountPrice(discountPrice);
        return yxSystemLevelProduct;
    }

    /**
     * 返回会员价
     *
     * @param price
     * @param uid
     * @return
     */
    @Override
    public double setLevelPrice(double price, int uid, int id) {
        QueryWrapper<YxUserLevel> wrapper = new QueryWrapper<YxUserLevel>();
        wrapper.eq("is_del", 0).eq("status", 1)
                .eq("uid", uid).orderByDesc("grade").last("limit 1");
        YxUserLevel userLevel = userLevelService.getOne(wrapper);
        if (userLevel == null) {
            return price;
        }
        int time = OrderUtil.dateToTimestamp(new Date());
        QueryWrapper<YxStoreDiscount> wrapper1 = new QueryWrapper<YxStoreDiscount>();
        wrapper1.eq("is_del", 0).eq("status", 1)
                .eq("product_id", id).eq("grade", userLevel.getGrade())
                .le("start_time", time).ge("stop_time", time).orderByDesc("add_time").last("limit 1");
        YxStoreDiscount yxStoreDiscount = yxStoreDiscountService.getOne(wrapper1);
        int discount = 100;
        int discount1 = 100;
        double vipPrice = 0.00;
        double discountPrice = 0.00;
        if (ObjectUtil.isNotNull(userLevel)) {
            discount = userLevel.getDiscount();
        }
        vipPrice = NumberUtil.mul(NumberUtil.div(discount, 100), price);

        if (ObjectUtil.isNotNull(yxStoreDiscount)) {
            discount1 = yxStoreDiscount.getDiscount();
            discountPrice = NumberUtil.mul(NumberUtil.div(discount1, 100), vipPrice);
            return discountPrice;
        } else {
            return vipPrice;
        }
    }

    /**
     * 更新用户余额
     *
     * @param uid
     * @param price
     */
    @Override
    public void incMoney(int uid, double price) {
        yxUserMapper.incMoney(uid, price);
    }

    @Override
    public void incIntegral(int uid, double integral) {
        yxUserMapper.incIntegral(integral, uid);
    }

    /**
     * 一级返佣
     *
     * @param order
     * @return
     */
    @Override
    public boolean backOrderBrokerage(YxStoreOrderQueryVo order) {
        //如果分销没开启直接返回
        String open = systemConfigService.getData("store_brokerage_open");
        if (StrUtil.isEmpty(open) || open.equals("2")) {
            return false;
        }
        //支付金额减掉邮费
        double payPrice = 0d;
        payPrice = NumberUtil.sub(order.getPayPrice(), order.getPayPostage()).doubleValue();

        //获取购买商品的用户
        YxUserQueryVo userInfo = getYxUserById(order.getUid());
        //当前用户不存在 没有上级  直接返回
        if (ObjectUtil.isNull(userInfo) || userInfo.getSpreadUid() == 0) {
            return true;
        }

        //获取后台分销类型  1 指定分销 2 人人分销
        int storeBrokerageStatus = 1;
        if (StrUtil.isNotEmpty(systemConfigService.getData("store_brokerage_statu"))) {
            storeBrokerageStatus = Integer.valueOf(systemConfigService
                    .getData("store_brokerage_statu"));
        }

        //指定分销 判断 上级是否时推广员  如果不是推广员直接跳转二级返佣
        YxUserQueryVo preUser = getYxUserById(userInfo.getSpreadUid());
        if (storeBrokerageStatus == 1) {

            if (preUser.getIsPromoter() == 0) {
                return backOrderBrokerageTwo(order);
            }
        }

        //获取后台一级返佣比例
        String storeBrokerageRatioStr = systemConfigService.getData("store_brokerage_ratio");
        int storeBrokerageRatio = 0;
        if (StrUtil.isNotEmpty(storeBrokerageRatioStr)) {
            storeBrokerageRatio = Integer.valueOf(storeBrokerageRatioStr);
        }
        //一级返佣比例 等于零时直接返回 不返佣
        if (storeBrokerageRatio == 0) {
            return true;
        }

        //计算获取一级返佣比例
        double brokerageRatio = NumberUtil.div(storeBrokerageRatio, 100);
        //成本价
        double cost = order.getCost().doubleValue();

        //成本价大于等于支付价格时直接返回
        if (cost >= payPrice) {
            return true;
        }

        //获取订单毛利
        payPrice = NumberUtil.sub(payPrice, cost);

        //返佣金额 = 毛利 / 一级返佣比例
        double brokeragePrice = NumberUtil.mul(payPrice, brokerageRatio);

        //返佣金额小于等于0 直接返回不返佣金
        if (brokeragePrice <= 0) {
            return true;
        }

        //计算上级推广员返佣之后的金额
        double balance = NumberUtil.add(preUser.getBrokeragePrice(), brokeragePrice)
                .doubleValue();
        String mark = userInfo.getNickname() + "成功消费" + order.getPayPrice() + "元,奖励推广佣金" +
                brokeragePrice;
        //插入流水
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(userInfo.getSpreadUid());
        userBill.setTitle("获得推广佣金");
        userBill.setLinkId(order.getId().toString());
        userBill.setCategory("now_money");
        userBill.setType("brokerage");
        userBill.setNumber(BigDecimal.valueOf(brokeragePrice));
        userBill.setBalance(BigDecimal.valueOf(balance));
        userBill.setMark(mark);
        userBill.setStatus(1);
        userBill.setPm(1);
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        billService.save(userBill);

        //添加用户余额
        yxUserMapper.incBrokeragePrice(brokeragePrice,
                userInfo.getSpreadUid());

        //一级返佣成功 跳转二级返佣
        backOrderBrokerageTwo(order);

        return false;
    }

    /**
     * 二级返佣
     *
     * @param order
     * @return
     */
    @Override
    public boolean backOrderBrokerageTwo(YxStoreOrderQueryVo order) {

        double payPrice = 0d;
        payPrice = NumberUtil.sub(order.getPayPrice(), order.getPayPostage()).doubleValue();

        YxUserQueryVo userInfo = getYxUserById(order.getUid());

        //获取上推广人
        YxUserQueryVo userInfoTwo = getYxUserById(userInfo.getSpreadUid());

        //上推广人不存在 或者 上推广人没有上级    直接返回
        if (ObjectUtil.isNull(userInfoTwo) || userInfoTwo.getSpreadUid() == 0) {
            return true;
        }

        //获取后台分销类型  1 指定分销 2 人人分销
        int storeBrokerageStatus = 1;
        if (StrUtil.isNotEmpty(systemConfigService.getData("store_brokerage_statu"))) {
            storeBrokerageStatus = Integer.valueOf(systemConfigService
                    .getData("store_brokerage_statu"));
        }
        //指定分销 判断 上上级是否时推广员  如果不是推广员直接返回
        YxUserQueryVo preUser = getYxUserById(userInfoTwo.getSpreadUid());
        if (storeBrokerageStatus == 1) {

            if (preUser.getIsPromoter() == 0) {
                return true;
            }
        }

        //获取二级返佣比例
        String storeBrokerageTwoStr = systemConfigService.getData("store_brokerage_two");
        int storeBrokerageTwo = 0;
        if (StrUtil.isNotEmpty(storeBrokerageTwoStr)) {
            storeBrokerageTwo = Integer.valueOf(storeBrokerageTwoStr);
        }
        //一级返佣比例 等于零时直接返回 不返佣
        if (storeBrokerageTwo == 0) {
            return true;
        }

        //计算获取二级返佣比例
        double brokerageRatio = NumberUtil.div(storeBrokerageTwo, 100);
        //成本价
        double cost = order.getCost().doubleValue();

        //成本价大于等于支付价格时直接返回
        if (cost >= payPrice) {
            return true;
        }

        //获取订单毛利
        payPrice = NumberUtil.sub(payPrice, cost);

        //返佣金额 = 毛利 / 二级返佣比例
        double brokeragePrice = NumberUtil.mul(payPrice, brokerageRatio);

        //返佣金额小于等于0 直接返回不返佣金
        if (brokeragePrice <= 0) {
            return true;
        }

        //获取上上级推广员信息
        double balance = NumberUtil.add(preUser.getBrokeragePrice(), brokeragePrice)
                .doubleValue();
        String mark = "二级推广人" + userInfo.getNickname() + "成功消费" + order.getPayPrice() + "元,奖励推广佣金" +
                brokeragePrice;
        //插入流水
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(userInfoTwo.getSpreadUid());
        userBill.setTitle("获得推广佣金");
        userBill.setLinkId(order.getId().toString());
        userBill.setCategory("now_money");
        userBill.setType("brokerage");
        userBill.setNumber(BigDecimal.valueOf(brokeragePrice));
        userBill.setBalance(BigDecimal.valueOf(balance));
        userBill.setMark(mark);
        userBill.setStatus(1);
        userBill.setPm(1);
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        billService.save(userBill);

        //添加用户余额
        yxUserMapper.incBrokeragePrice(brokeragePrice,
                userInfoTwo.getSpreadUid());


        return false;
    }

    @Override
    public void setUserSpreadCount(int uid) {
        QueryWrapper<YxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("spread_uid", uid);
        int count = yxUserMapper.selectCount(wrapper);

        YxUser user = new YxUser();
        user.setUid(uid);
        user.setSpreadCount(count);

        yxUserMapper.updateById(user);
    }

    @Override
    public int getSpreadCount(int uid, int type) {
        QueryWrapper<YxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("spread_uid", uid);
        int count = 0;
        if (type == 1) {
            count = yxUserMapper.selectCount(wrapper);
        } else {
            List<YxUser> userList = yxUserMapper.selectList(wrapper);
            List<Integer> userIds = userList.stream().map(YxUser::getUid)
                    .collect(Collectors.toList());
            if (userIds.isEmpty()) {
                count = 0;
            } else {
                QueryWrapper<YxUser> wrapperT = new QueryWrapper<>();
                wrapperT.in("spread_uid", userIds);

                count = yxUserMapper.selectCount(wrapperT);
            }

        }
        return count;
    }

    @Override
    public List<PromUserDTO> getUserSpreadGrade(PromParam promParam, int uid) {
        QueryWrapper<YxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("spread_uid", uid);
        List<YxUser> userList = yxUserMapper.selectList(wrapper);
        List<Integer> userIds = userList.stream().map(YxUser::getUid)
                .collect(Collectors.toList());
        List<PromUserDTO> list = new ArrayList<>();
        if (userIds.isEmpty()) {
            return list;
        }
        String sort;
        if (StrUtil.isEmpty(promParam.getSort())) {
            sort = "u.add_time desc";
        } else {
            sort = promParam.getSort();
        }
        String keyword = null;
        if (StrUtil.isNotEmpty(promParam.getKeyword())) {
            keyword = promParam.getKeyword();
        }
        Page<YxUser> pageModel = new Page<>(promParam.getPage(), promParam.getLimit());
        if (promParam.getGrade() == 0) {//-级
            list = yxUserMapper.getUserSpreadCountList(pageModel, userIds,
                    keyword, sort);
        } else {//二级
            QueryWrapper<YxUser> wrapperT = new QueryWrapper<>();
            wrapperT.in("spread_uid", userIds);
            List<YxUser> userListT = yxUserMapper.selectList(wrapperT);
            List<Integer> userIdsT = userListT.stream().map(YxUser::getUid)
                    .collect(Collectors.toList());
            if (userIdsT.isEmpty()) {
                return list;
            }
            list = yxUserMapper.getUserSpreadCountList(pageModel, userIdsT,
                    keyword, sort);

        }
        return list;
    }

    /**
     * 设置推广关系
     *
     * @param spread
     * @param uid
     */
    @Override
    public boolean setSpread(int spread, int uid) {
        //如果分销没开启直接返回
        String open = systemConfigService.getData("store_brokerage_open");
        if (StrUtil.isEmpty(open) || open.equals("2")) {
            return false;
        }
        //当前用户信息
        YxUserQueryVo userInfo = getYxUserById(uid);
        if (ObjectUtil.isNull(userInfo)) {
            return true;
        }

        //当前用户有上级直接返回
        if (userInfo.getSpreadUid() > 0) {
            return true;
        }
        //没有推广编号直接返回
        if (spread == 0) {
            return true;
        }
        if (spread == uid) {
            return true;
        }

        //不能互相成为上下级
        YxUserQueryVo userInfoT = getYxUserById(spread);
        if (ObjectUtil.isNull(userInfoT)) {
            return true;
        }

        if (userInfoT.getSpreadUid() == uid) {
            return true;
        }

        //1-指定分销 2-人人分销
        int storeBrokerageStatus = Integer.valueOf(systemConfigService
                .getData("store_brokerage_statu"));
        //如果是指定分销，如果 推广人不是分销员不能形成关系
        if (storeBrokerageStatus == 1 && userInfoT.getIsPromoter() == 0) {
            return true;
        }
        YxUser yxUser = new YxUser();

        yxUser.setSpreadUid(spread);
        yxUser.setSpreadTime(OrderUtil.getSecondTimestampTwo());
        yxUser.setUid(uid);
        yxUserMapper.updateById(yxUser);

        return true;

    }

    @Override
    public void incPayCount(int uid) {
        yxUserMapper.incPayCount(uid);
    }

    @Override
    public void decPrice(int uid, double payPrice) {
        yxUserMapper.decPrice(payPrice, uid);
    }

    @Override
    public void decIntegral(int uid, double integral) {
        yxUserMapper.decIntegral(integral, uid);
    }

    @Override
    public YxUserQueryVo getYxUserById(Serializable id) {
        YxUserQueryVo userQueryVo = yxUserMapper.getYxUserById(id);
        return userQueryVo;
    }

    @Override
    public YxUserQueryVo getNewYxUserById(Serializable id) {
        YxUserQueryVo userQueryVo = yxUserMapper.getYxUserById(id);
        if (userQueryVo == null) {
            throw new ErrorRequestException("用户不存在");
        }
        userQueryVo.setOrderStatusNum(orderService.orderData((int) id));
        userQueryVo.setCouponCount(storeCouponUserService.getUserValidCouponCount((int) id));
        //判断分销类型
        String statu = systemConfigService.getData("store_brokerage_statu");

        if (StrUtil.isNotEmpty(statu)) {
            userQueryVo.setStatu(Integer.valueOf(statu));
        } else {
            userQueryVo.setStatu(0);
        }

        //获取核销权限
        userQueryVo.setCheckStatus(systemStoreStaffService.checkStatus((int) id, 0));

        return userQueryVo;
    }

    @Override
    public Paging<YxUserQueryVo> getYxUserPageList(YxUserQueryParam yxUserQueryParam) throws Exception {
        Page page = setPageParam(yxUserQueryParam, OrderItem.desc("add_time"));
        IPage<YxUserQueryVo> iPage = yxUserMapper.getYxUserPageList(page, yxUserQueryParam);
        return new Paging(iPage);
    }

    @Override
    public YxUser findByName(String name) {
        QueryWrapper<YxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", name);
        return getOne(wrapper);
    }
}
