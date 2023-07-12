/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.wechat.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.enums.OrderInfoEnum;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.user.entity.YxUserRecharge;
import co.yixiang.modules.user.service.YxUserRechargeService;
import co.yixiang.mp.config.WxMpConfiguration;
import co.yixiang.mp.config.WxPayConfiguration;
import co.yixiang.utils.BigNum;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName WechatController
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/5
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "微信模块", tags = "微信:微信模块", description = "微信模块")
public class WechatController extends BaseController {

    private final YxStoreOrderService orderService;
    private final YxSystemConfigService systemConfigService;
    private final YxUserRechargeService userRechargeService;

    private final PasswordEncoder passwordEncoder;


    /**
     * 微信分享配置
     */
    @AnonymousAccess
    @GetMapping("/test")
    @ApiOperation(value = "test 测试", notes = "test 测试")
    public ApiResult test() {
        return ApiResult.ok(passwordEncoder.encode("123456"));
    }


    /**
     * 微信分享配置
     */
    @AnonymousAccess
    @GetMapping("/share")
    @ApiOperation(value = "微信分享配置", notes = "微信分享配置")
    public ApiResult<Object> share() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("img", systemConfigService.getData("wechat_share_img"));
        map.put("title", systemConfigService.getData("wechat_share_title"));
        map.put("synopsis", systemConfigService.getData("wechat_share_synopsis"));
        Map<String, Object> mapt = new LinkedHashMap<>();
        mapt.put("data", map);
        return ApiResult.ok(mapt);
    }

    /**
     * jssdk配置
     */
    @AnonymousAccess
    @GetMapping("/wechat/config")
    @ApiOperation(value = "jssdk配置", notes = "jssdk配置")
    public ApiResult<Object> jsConfig(@RequestParam(value = "url") String url) throws WxErrorException {
        WxMpService wxService = WxMpConfiguration.getWxMpService();
        WxJsapiSignature jsapiSignature = wxService.createJsapiSignature(url);
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("appId", jsapiSignature.getAppId());
        map.put("jsApiList", new String[]{"updateAppMessageShareData", "openLocation", "scanQRCode",
                "chooseWXPay", "updateAppMessageShareData", "updateTimelineShareData",
                "openAddress", "editAddress", "getLocation"});
        map.put("nonceStr", jsapiSignature.getNonceStr());
        map.put("signature", jsapiSignature.getSignature());
        map.put("timestamp", jsapiSignature.getTimestamp());
        map.put("url", jsapiSignature.getUrl());
        return ApiResult.ok(map);
    }


    /**
     * 微信支付/充值回调
     */
    @AnonymousAccess
    @PostMapping("/wechat/notify")
    @ApiOperation(value = "微信支付充值回调", notes = "微信支付充值回调")
    public String renotify(@RequestBody String xmlData) {
        try {
            System.out.println("微信支付回调------------");
            WxPayService wxPayService = WxPayConfiguration.getPayService();
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            String orderId = notifyResult.getOutTradeNo();
            String attach = notifyResult.getAttach();
            if (BillDetailEnum.TYPE_3.getValue().equals(attach)) {
                //处理订单
                YxStoreOrderQueryVo orderInfo = orderService.getOrderInfo(orderId, 0);
                if (orderInfo == null) {
                    return WxPayNotifyResponse.success("处理成功!");
                }
                if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(orderInfo.getPaid())) {
                    return WxPayNotifyResponse.success("处理成功!");
                }
                orderService.paySuccess(orderInfo.getOrderId(), "weixin");
            } else if (BillDetailEnum.TYPE_1.getValue().equals(attach)) {
                //处理充值
                YxUserRecharge userRecharge = userRechargeService.getInfoByOrderId(orderId);
                if (userRecharge == null) {
                    return WxPayNotifyResponse.success("处理成功!");
                }
                if (OrderInfoEnum.PAY_STATUS_1.getValue().equals(userRecharge.getPaid())) {
                    return WxPayNotifyResponse.success("处理成功!");
                }
                userRechargeService.updateRecharge(userRecharge);
            }

            return WxPayNotifyResponse.success("处理成功!");
        } catch (WxPayException e) {
            log.error(e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }

    }

    /**
     * 微信退款回调
     *
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    @AnonymousAccess
    @ApiOperation(value = "退款回调通知处理", notes = "退款回调通知处理")
    @PostMapping("/notify/refund")
    public String parseRefundNotifyResult(@RequestBody String xmlData) {
        try {
            System.out.println("xmlData" + xmlData);
            WxPayService wxPayService = WxPayConfiguration.getPayService();
            WxPayRefundNotifyResult result = wxPayService.parseRefundNotifyResult(xmlData);
            String orderId = result.getReqInfo().getOutTradeNo();
            BigDecimal refundFee = BigNum.div(result.getReqInfo().getRefundFee(), 100);
            YxStoreOrderQueryVo orderInfo = orderService.getOrderInfo(orderId, 0);
            if (orderInfo.getRefundStatus() == 2) {
                return WxPayNotifyResponse.success("处理成功!");
            }
            YxStoreOrder storeOrder = new YxStoreOrder();
            //修改状态
            storeOrder.setId(orderInfo.getId());
            storeOrder.setRefundStatus(2);
            storeOrder.setRefundPrice(refundFee);
            orderService.updateById(storeOrder);
            return WxPayNotifyResponse.success("处理成功!");
        } catch (WxPayException | IllegalAccessException e) {
            log.error(e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }


    /**
     * 微信验证消息
     */
    @AnonymousAccess
    @GetMapping(value = "/wechat/serve", produces = "text/plain;charset=utf-8")
    @ApiOperation(value = "微信验证消息", notes = "微信验证消息")
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        final WxMpService wxService = WxMpConfiguration.getWxMpService();
        if (wxService == null) {
            throw new IllegalArgumentException("未找到对应配置的服务，请核实！");
        }

        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "fail";
    }


    @AnonymousAccess
    @PostMapping("/wechat/serve")
    @ApiOperation(value = "微信获取消息", notes = "微信获取消息")
    public void post(@RequestBody String requestBody,
                     @RequestParam("signature") String signature,
                     @RequestParam("timestamp") String timestamp,
                     @RequestParam("nonce") String nonce,
                     @RequestParam("openid") String openid,
                     @RequestParam(name = "encrypt_type", required = false) String encType,
                     @RequestParam(name = "msg_signature", required = false) String msgSignature,
                     HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        WxMpService wxService = WxMpConfiguration.getWxMpService();

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return;
            }
            out = outMessage.toXml();
            ;
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return;
            }

            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(out);
        writer.close();
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return WxMpConfiguration.getWxMpMessageRouter().route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }


}
