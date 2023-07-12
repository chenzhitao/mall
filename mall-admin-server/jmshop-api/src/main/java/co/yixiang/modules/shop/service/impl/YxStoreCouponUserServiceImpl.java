/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.CouponEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.shop.entity.YxStoreCouponUser;
import co.yixiang.modules.shop.mapper.YxStoreCartMapper;
import co.yixiang.modules.shop.mapper.YxStoreCouponUserMapper;
import co.yixiang.modules.shop.mapper.YxStoreProductMapper;
import co.yixiang.modules.shop.mapping.CouponMap;
import co.yixiang.modules.shop.service.YxStoreCartService;
import co.yixiang.modules.shop.service.YxStoreCategoryService;
import co.yixiang.modules.shop.service.YxStoreCouponService;
import co.yixiang.modules.shop.service.YxStoreCouponUserService;
import co.yixiang.modules.shop.web.param.YxStoreCouponUserQueryParam;
import co.yixiang.modules.shop.web.vo.*;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 优惠券发放记录表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreCouponUserServiceImpl extends BaseServiceImpl<YxStoreCouponUserMapper, YxStoreCouponUser> implements YxStoreCouponUserService {

    @Autowired
    private YxStoreCouponUserMapper yxStoreCouponUserMapper;
    @Autowired
    private YxStoreCouponService storeCouponService;
    @Autowired
    private CouponMap couponMap;
    @Autowired
    private YxStoreCartService yxStoreCartService;

    @Autowired
    private YxStoreProductMapper yxStoreProductMapper;

    @Autowired
    private YxStoreCategoryService yxStoreCategoryService;

    @Override
    public int getUserValidCouponCount(int uid) {
        this.checkInvalidCoupon(uid);
        return this.lambdaQuery()
                .eq(YxStoreCouponUser::getStatus, CouponEnum.STATUS_0.getValue())
                .eq(YxStoreCouponUser::getUid, uid)
                .count();
    }

    @Override
    public List<StoreCouponUserVo> beUsableCouponList(int uid, String cartIds) {
        Map<String, Object> cartGroup = yxStoreCartService.getUserProductCartList(uid,
                cartIds, ShopConstants.JMSHOP_ONE_NUM);

        List<YxStoreCartQueryVo> cartInfo = (List<YxStoreCartQueryVo>) cartGroup.get("valid");

        BigDecimal sumPrice = BigDecimal.ZERO;
        for (YxStoreCartQueryVo storeCart : cartInfo) {
            sumPrice = NumberUtil.add(sumPrice, NumberUtil.mul(storeCart.getCartNum(), storeCart.getTruePrice()));
        }

        Map<Integer,List<YxStoreCartQueryVo>> productList= cartInfo.stream().collect(Collectors.groupingBy(YxStoreCartQueryVo::getProductId));
        Map<Integer,BigDecimal> proPriceMap=new HashMap<>();
        productList.keySet().forEach(pid->{
            List<YxStoreCartQueryVo> groupList=productList.get(pid);
            BigDecimal productSumPrice = BigDecimal.ZERO;
            for (YxStoreCartQueryVo cart : groupList) {
                productSumPrice = NumberUtil.add(productSumPrice, NumberUtil.mul(cart.getCartNum(), cart.getTruePrice()));
            }
            proPriceMap.put(pid,productSumPrice);
        });

        List<String> productIds = cartInfo.stream()
                .map(YxStoreCartQueryVo::getProductId)
                .map(Object::toString)
                .collect(Collectors.toList());


        return this.getUsableCouponList(uid, sumPrice.doubleValue(), productIds,proPriceMap);
    }

    /**
     * 获取下单时候满足的优惠券
     *
     * @param uid        uid
     * @param price      总价格
     * @param productIds list
     * @return list
     */
    @Override
    public List<StoreCouponUserVo> getUsableCouponList(int uid, double price, List<String> productIds,Map<Integer,BigDecimal> proPriceMap) {
        int nowTime = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(new Date()));
        List<StoreCouponUserVo> storeCouponUsers = yxStoreCouponUserMapper.selectCouponList(nowTime, price, uid);
        List<StoreCouponUserVo> useableCoupons=new ArrayList<>();
        for (StoreCouponUserVo s : storeCouponUsers) {
            List<String> cateList= new ArrayList<>();
            if(s.getCategoryId()!=null){
                cateList= yxStoreCategoryService.getAllChilds(s.getCategoryId());
            }
            if(cateList.size()==0) {
                if (Double.doubleToLongBits(s.getUseMinPrice()) <= Double.doubleToLongBits(price)) {
                    s.setIsUse(true);
                } else {
                    s.setIsUse(false);
                }
                useableCoupons.add(s);
            }else {
                String cateIds = "";
                for (String cid : cateList) {
                    if (StringUtils.isEmpty(cateIds)) {
                        cateIds = cid;
                    } else {
                        cateIds += "," + cid;
                    }
                }
                List<YxStoreProductQueryVo> productList = yxStoreProductMapper.getYxStoreProductByCateIds(cateIds);
                proPriceMap.keySet().forEach(pid -> {
                    YxStoreProductQueryVo pvo = productList.stream().filter(pro -> pro.getId().intValue() == pid.intValue()).findFirst().orElse(null);
                    if (pvo != null) {
                        if (Double.doubleToLongBits(s.getUseMinPrice()) <= Double.doubleToLongBits(proPriceMap.get(pid).doubleValue())) {
                            s.setIsUse(true);
                        } else {
                            s.setIsUse(false);
                        }
                        useableCoupons.add(s);
                    }
                });
            }
        }
        return useableCoupons;

        /*return storeCouponUsers.stream()
                .filter(coupon ->
                        CouponEnum.TYPE_2.getValue().equals(coupon.getType()) ||
                                CouponEnum.TYPE_0.getValue().equals(coupon.getType())
                                || (CouponEnum.TYPE_1.getValue().equals(coupon.getType())
                               ))
                .collect(Collectors.toList());*/
    }

    /**
     * 判断两个list是否有相同值
     *
     * @param list1 list
     * @param list2 list
     * @return boolean
     */
    private boolean isSame(List<String> list1, List<String> list2) {
        if (list2.isEmpty()) {
            return true;
        }
        list1 = new ArrayList<>(list1);
        list2 = new ArrayList<>(list2);
        list1.addAll(list2);
        int total = list1.size();

        List<String> newList = new ArrayList<>(new HashSet<>(list1));

        int newTotal = newList.size();


        return total > newTotal;
    }

    /**
     * 获取可用优惠券
     *
     * @param uid
     * @param price
     * @return
     */
    @Override
    public YxStoreCouponUser beUsableCoupon(int uid, double price, List<String> productIds, Map<Integer, BigDecimal> proPriceMap) {
        int nowTime = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(new Date()));
        List<YxStoreCouponUser> yxStoreCouponUsers = yxStoreCouponUserMapper.beUsableCoupon(nowTime, uid);

        for (YxStoreCouponUser yxStoreCouponUser : yxStoreCouponUsers) {
            List<String> cateList=new ArrayList<>();
            if(ObjectUtil.isNotNull(yxStoreCouponUser.getCategoryId())) {
                cateList=yxStoreCategoryService.getAllChilds(yxStoreCouponUser.getCategoryId());
            }
            if(cateList.size()==0) {
                if (yxStoreCouponUser.getUseMinPrice().doubleValue() <= price) {
                    return yxStoreCouponUser;
                }
            }else {
                String cateIds = "";
                for (String cid : cateList) {
                    if (StringUtils.isEmpty(cateIds)) {
                        cateIds = cid;
                    } else {
                        cateIds += "," + cid;
                    }
                }
                List<YxStoreProductQueryVo> productList = yxStoreProductMapper.getYxStoreProductByCateIds(cateIds);
                Iterator<Integer> pidList= proPriceMap.keySet().iterator();
                YxStoreCouponUser checkUser=null;
                while(pidList.hasNext()){
                    Integer pid = pidList.next();
                    YxStoreProductQueryVo pvo = productList.stream().filter(pro -> pro.getId().intValue() == pid.intValue()).findFirst().orElse(null);
                    if (pvo != null) {
                        if (yxStoreCouponUser.getUseMinPrice().doubleValue() <= proPriceMap.get(pid).doubleValue()) {
                            checkUser= yxStoreCouponUser;
                            break;
                        }
                    }
                }
                if(checkUser!=null){
                    return checkUser;
                }
            }
        }


        return new YxStoreCouponUser();

        //        LambdaQueryWrapper<YxStoreCouponUser> wrapper = new LambdaQueryWrapper<>();
        //        wrapper.eq(YxStoreCouponUser::getIsFail, CouponEnum.FALI_0.getValue())
        //                .eq(YxStoreCouponUser::getStatus, CouponEnum.STATUS_0.getValue()).eq(YxStoreCouponUser::getUid, uid)
        //                .le(YxStoreCouponUser::getUseMinPrice, price)
        //                .orderByDesc(YxStoreCouponUser::getCouponPrice).last("limit 1");
        //        return getOne(wrapper);
        //更新替换
        //        int nowTime = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(new Date()));
        //        List<YxStoreCouponUser> yxStoreCouponUsers = yxStoreCouponUserMapper.beUsableCoupon(nowTime, uid);
        //        return yxStoreCouponUsers.size() > 0 ? yxStoreCouponUsers.get(0) : new YxStoreCouponUser();
    }

    @Override
    public YxStoreCouponUser getCoupon(int id, int uid) {
        LambdaQueryWrapper<YxStoreCouponUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YxStoreCouponUser::getIsFail, CouponEnum.FALI_0.getValue())
                .eq(YxStoreCouponUser::getStatus, CouponEnum.STATUS_0.getValue()).eq(YxStoreCouponUser::getUid, uid)
                .eq(YxStoreCouponUser::getId, id);
        return getOne(wrapper);
    }

    @Override
    public void useCoupon(int id) {
        YxStoreCouponUser couponUser = new YxStoreCouponUser();
        couponUser.setId(id);
        couponUser.setStatus(CouponEnum.STATUS_1.getValue());
        couponUser.setUseTime(OrderUtil.getSecondTimestampTwo());
        yxStoreCouponUserMapper.updateById(couponUser);
    }

    /**
     * 检查优惠券状态
     *
     * @param uid
     */
    @Override
    public void checkInvalidCoupon(int uid) {
        int nowTime = OrderUtil.getSecondTimestampTwo();
        LambdaQueryWrapper<YxStoreCouponUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(YxStoreCouponUser::getEndTime, nowTime).eq(YxStoreCouponUser::getStatus, CouponEnum.STATUS_0.getValue());
        YxStoreCouponUser couponUser = new YxStoreCouponUser();
        couponUser.setStatus(CouponEnum.STATUS_2.getValue());
        yxStoreCouponUserMapper.update(couponUser, wrapper);

    }

    /**
     * 获取用户优惠券
     *
     * @param uid uid
     * @return
     */
    @Override
    public List<YxStoreCouponUserQueryVo> getUserCoupon(int uid) {

        //checkInvalidCoupon(uid);
        LambdaQueryWrapper<YxStoreCouponUser> wrapper = new LambdaQueryWrapper<>();
        //默认获取所有
        wrapper.eq(YxStoreCouponUser::getUid, uid);
        List<YxStoreCouponUser> storeCouponUsers = yxStoreCouponUserMapper.selectList(wrapper);

        List<YxStoreCouponUserQueryVo> storeCouponUserQueryVoList = new ArrayList<>();
        int nowTime = OrderUtil.getSecondTimestampTwo();
        for (YxStoreCouponUser couponUser : storeCouponUsers) {
            YxStoreCouponUserQueryVo queryVo = couponMap.toDto(couponUser);
            if (couponUser.getIsFail() == 1) {
                queryVo.set_type(0);
                queryVo.set_msg("已失效");
            } else if (couponUser.getStatus() == 1) {
                queryVo.set_type(0);
                queryVo.set_msg("已使用");
            } else if (couponUser.getStatus() == 2) {
                queryVo.set_type(0);
                queryVo.set_msg("已过期");
                continue;
            } else if (couponUser.getAddTime() > nowTime || couponUser.getEndTime() < nowTime) {
                queryVo.set_type(0);
                queryVo.set_msg("已过期");
                continue;
            } else {
                if (couponUser.getAddTime() + 3600 * 24 > nowTime) {
                    queryVo.set_type(2);
                    queryVo.set_msg("可使用");
                } else {
                    queryVo.set_type(1);
                    queryVo.set_msg("可使用");
                }
            }

            storeCouponUserQueryVoList.add(queryVo);

        }
        storeCouponUserQueryVoList.sort((o1,o2) -> -1 * o1.getEndTime().compareTo(o2.getEndTime()));
        return storeCouponUserQueryVoList;
    }

    @Override
    public void addUserCoupon(int uid, YxStoreCouponIssueQueryVo queryVo) {
        YxStoreCouponQueryVo storeCouponQueryVo = storeCouponService.
                getYxStoreCouponById(queryVo.getCid());
        if (ObjectUtil.isNull(storeCouponQueryVo)) {
            throw new ErrorRequestException("优惠劵不存在");
        }

        YxStoreCouponUser couponUser = new YxStoreCouponUser();
        couponUser.setCid(queryVo.getCid());
        couponUser.setUid(uid);
        couponUser.setCouponTitle(storeCouponQueryVo.getTitle());
        couponUser.setCouponPrice(storeCouponQueryVo.getCouponPrice());
        couponUser.setUseMinPrice(storeCouponQueryVo.getUseMinPrice());
        //int addTime = OrderUtil.getSecondTimestampTwo();
        couponUser.setAddTime(OrderUtil.getSecondTimestampTwo());
        //int endTime = addTime + storeCouponQueryVo.getCouponTime() * 86400;
        couponUser.setTimeType(storeCouponQueryVo.getTimeType());
        couponUser.setTimeNum(storeCouponQueryVo.getTimeNum());
        if(storeCouponQueryVo.getTimeType()!=null && storeCouponQueryVo.getTimeType().intValue()==0){
            couponUser.setEndTime(storeCouponQueryVo.getCouponTime());
        }else{
            int endTime= OrderUtil.getSecondTimestampTwo()+storeCouponQueryVo.getTimeNum().intValue()*86400;
            couponUser.setEndTime(endTime);
        }

        couponUser.setType("get");
        couponUser.setCategoryId(queryVo.getCategoryId());
        couponUser.setCategoryName(queryVo.getCategoryName());
        save(couponUser);

    }

    @Override
    public YxStoreCouponUserQueryVo getYxStoreCouponUserById(Serializable id) throws Exception {
        return yxStoreCouponUserMapper.getYxStoreCouponUserById(id);
    }

    @Override
    public Paging<YxStoreCouponUserQueryVo> getYxStoreCouponUserPageList(YxStoreCouponUserQueryParam yxStoreCouponUserQueryParam) throws Exception {
        Page page = setPageParam(yxStoreCouponUserQueryParam, OrderItem.desc("create_time"));
        IPage<YxStoreCouponUserQueryVo> iPage = yxStoreCouponUserMapper.getYxStoreCouponUserPageList(page, yxStoreCouponUserQueryParam);
        return new Paging(iPage);
    }

}
