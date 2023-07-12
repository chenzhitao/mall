/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.web.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.service.YxSystemUserLevelService;
import co.yixiang.modules.user.service.YxUserRechargeService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.modules.user.web.param.RechargeParam;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import co.yixiang.mp.config.WxPayConfiguration;
import co.yixiang.mp.service.YxPayService;
import co.yixiang.utils.SecurityUtils;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 用户充值 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-03-01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户充值", tags = "用户:用户充值", description = "用户充值")
public class UserRechargeController extends BaseController {

    private final YxUserRechargeService userRechargeService;
    private final YxSystemConfigService systemConfigService;
    private final YxPayService payService;
    private final YxWechatUserService wechatUserService;
    private final YxSystemGroupDataService systemGroupDataService;
    private final YxSystemUserLevelService systemUserLevelService;

    /**
     * 充值方案
     */
    @GetMapping("/recharge/index")
    @ApiOperation(value = "充值方案", notes = "充值方案", response = ApiResult.class)
    public ApiResult<Object> getWays() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("recharge_price_ways", systemGroupDataService.getDatas(ShopConstants.JMSHOP_RECHARGE_PRICE_WAYS));
        return ApiResult.ok(map);
    }

    /**
     * 公众号充值/H5充值
     */
    @PostMapping("/recharge/wechat")
    @ApiOperation(value = "公众号充值/H5充值", notes = "公众号充值/H5充值", response = ApiResult.class)
    public ApiResult<Map<String, Object>> add(@Valid @RequestBody RechargeParam param) {
        int uid = SecurityUtils.getUserId().intValue();
        String money = systemConfigService.getData("store_user_min_recharge");
        Double newMoney = 0d;
        if (StrUtil.isNotEmpty(money)) {
            newMoney = Double.valueOf(money);
        }
        if (newMoney > param.getPrice()) {
            throw new ErrorRequestException("充值金额不能低于" + newMoney);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", param.getFrom());

        //生成分布式唯一值
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();

        param.setOrderSn(orderSn);
        userRechargeService.addRecharge(param, uid);

        BigDecimal bigDecimal = new BigDecimal(100);
        int price = bigDecimal.multiply(BigDecimal.valueOf(param.getPrice())).intValue();
        try {
            if (param.getFrom().equals("weixinh5")) {
                WxPayMwebOrderResult result = payService.wxH5Pay(orderSn, "H5充值", price,
                        BillDetailEnum.TYPE_1.getValue());
                map.put("data", result.getMwebUrl());
            } else {
                YxWechatUser wechatUser = wechatUserService.getById(uid);
                if (ObjectUtil.isNull(wechatUser)) {
                    throw new ErrorRequestException("用户错误");
                }
                WxPayMpOrderResult result = payService.wxPay(orderSn, wechatUser.getOpenid(),
                        "公众号充值", price, BillDetailEnum.TYPE_1.getValue());
                Map<String, String> jsConfig = new HashMap<>();
                jsConfig.put("appId", result.getAppId());
                jsConfig.put("timestamp", result.getTimeStamp());
                jsConfig.put("nonceStr", result.getNonceStr());
                jsConfig.put("package", result.getPackageValue());
                jsConfig.put("signType", result.getSignType());
                jsConfig.put("paySign", result.getPaySign());
                map.put("data", jsConfig);
            }
        } catch (WxPayException e) {
            return ApiResult.fail(e.getMessage());
        }
        return ApiResult.ok(map);
    }

    /**
     * 购买会员
     */
    @PostMapping("/purchase/member")
    @ApiOperation(value = "购买会员", notes = "购买会员", response = ApiResult.class)
    public ApiResult<Map<String, Object>> purchaseMember(@RequestBody RechargeParam param) {
        //当前购买等级
        YxSystemUserLevelQueryVo yxSystemUserLevelById = systemUserLevelService.getYxSystemUserLevelById(param.getSystemUserLevelId());
        if (yxSystemUserLevelById == null) {
            throw new ErrorRequestException("不存在该会员等级");
        }
        int uid = SecurityUtils.getUserId().intValue();
        //支付金额
        param.setPrice(yxSystemUserLevelById.getMoney().doubleValue());
        //生成分布式唯一值
        String orderSn = IdUtil.getSnowflake(0, 0).nextIdStr();
        //购买会员
        param.setType(2);
        //等级ID
        param.setSystemUserLevelId(yxSystemUserLevelById.getId());
        //订单编号
        param.setOrderSn(orderSn);
        //生成支付记录
        userRechargeService.addRecharge(param, uid);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", param.getFrom());
        try {
            //金额
            BigDecimal bigDecimal = new BigDecimal(100);
            //元转分
            int price = bigDecimal.multiply(BigDecimal.valueOf(param.getPrice())).intValue();
            //获取微信用户
            YxWechatUser wechatUser = wechatUserService.getById(uid);
            //
            if (ObjectUtil.isNull(wechatUser)) {
                throw new ErrorRequestException("微信用户支付异常，请联系客服！");
            }
            //小程序支付
            WxPayMpOrderResult result = payService.wxPaySamll(orderSn, wechatUser.getRoutineOpenid(), "小程序购买会员", price, BillDetailEnum.TYPE_1.getValue());
            Map<String, String> jsConfig = new HashMap<>();
            jsConfig.put("appId", result.getAppId());
            jsConfig.put("timestamp", result.getTimeStamp());
            jsConfig.put("nonceStr", result.getNonceStr());
            jsConfig.put("package", result.getPackageValue());
            jsConfig.put("signType", result.getSignType());
            jsConfig.put("paySign", result.getPaySign());
            map.put("data", jsConfig);
        } catch (WxPayException e) {
            return ApiResult.fail(e.getMessage());
        }
        return ApiResult.ok(map);
    }

}

