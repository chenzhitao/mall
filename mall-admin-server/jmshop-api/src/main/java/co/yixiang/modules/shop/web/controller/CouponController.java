/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.web.controller;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.enums.CouponEnum;
import co.yixiang.modules.shop.service.YxStoreCouponIssueService;
import co.yixiang.modules.shop.service.YxStoreCouponService;
import co.yixiang.modules.shop.service.YxStoreCouponUserService;
import co.yixiang.modules.shop.web.param.YxStoreCouponQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreCouponUserQueryVo;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 优惠券 todo
 * </p>
 *
 * @author hupeng
 * @since 2019-10-02
 */
@Slf4j
@RestController
@Api(value = "优惠券", tags = "营销:优惠券", description = "优惠券")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CouponController extends BaseController {

    private final YxStoreCouponIssueService couponIssueService;
    private final YxStoreCouponUserService storeCouponUserService;
    private final YxStoreCouponService storeCouponService;

    /**
     * 可领取优惠券列表
     */
    @Log(value = "查看优惠券",type = 1)
    @GetMapping("/coupons")
    @ApiOperation(value = "可领取优惠券列表",notes = "可领取优惠券列表")
    public ApiResult<Object> getList(YxStoreCouponQueryParam queryParam){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(couponIssueService.getCouponList(queryParam.getPage().intValue(),
                queryParam.getLimit().intValue(),uid));
    }

    /**
     * 领取优惠券
     */
    @Log(value = "领取优惠券",type = 1)
    @PostMapping("/coupon/receive")
    @ApiOperation(value = "领取优惠券",notes = "领取优惠券")
    public ApiResult<Object> receive(@RequestBody String jsonStr){
        int uid = SecurityUtils.getUserId().intValue();
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if(ObjectUtil.isNull(jsonObject.get("couponId"))){
            ApiResult.fail("参数错误");
        }
        couponIssueService.issueUserCoupon(
                Integer.valueOf(jsonObject.get("couponId").toString()),uid);
        return ApiResult.ok("ok");
    }

    /**
     * 用户已领取优惠券
     */
    @GetMapping("/coupons/user/{type}")
    @ApiOperation(value = "用户已领取优惠券",notes = "用户已领取优惠券")
    public ApiResult<Object> getUserList(){
        int uid = SecurityUtils.getUserId().intValue();
        List<YxStoreCouponUserQueryVo> list = storeCouponUserService.getUserCoupon(uid);
        return ApiResult.ok(list);
    }

    /**
     * 优惠券 订单获取
     */
    @GetMapping("/coupons/order/{cartIds}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cartIds", value = "购物车ID,多个用,分割", paramType = "query", dataType = "int")
    })
    @ApiOperation(value = "优惠券订单获取",notes = "优惠券订单获取")
    public ApiResult<Object> orderCoupon(@PathVariable String cartIds){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(storeCouponUserService.beUsableCouponList(uid,cartIds));
    }

    @GetMapping("/coupons/getDetail/{cid}")
    @ApiOperation(value = "获取优惠券详情",notes = "获取优惠券详情")
    public ApiResult<Object> getCouponsDetail(@PathVariable Integer cid){
        YxStoreCouponQueryVo vo = storeCouponService.getYxStoreCouponById(cid);
        return ApiResult.ok(vo);
    }


}

