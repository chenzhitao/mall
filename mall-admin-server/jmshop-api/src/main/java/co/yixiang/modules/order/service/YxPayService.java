//package co.yixiang.modules.order.service;
//
//import cn.hutool.core.util.StrUtil;
//import co.yixiang.exception.ErrorRequestException;
//import co.yixiang.modules.shop.service.YxSystemConfigService;
//import co.yixiang.mp.config.WxPayConfiguration;
//import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
//import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
//import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
//import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
//import com.github.binarywang.wxpay.exception.WxPayException;
//import com.github.binarywang.wxpay.service.WxPayService;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//
///**
// * @ClassName YxPayService
// * @Author hupeng <610796224@qq.com>
// * @Date 2020/3/1
// **/
//@Service
//public class YxPayService {
//
//    @Autowired
//    private YxSystemConfigService systemConfigService;
//
//
//    /**
//     * 微信公众号支付/小程序支付
//     *
//     * @param orderId
//     * @param openId   公众号/小程序openid
//     * @param body
//     * @param totalFee
//     * @return
//     * @throws WxPayException
//     */
//    public WxPayMpOrderResult wxPay(String orderId, String openId, String body,
//                                    Integer totalFee,int type) throws WxPayException {
//
//        String apiUrl = systemConfigService.getData("api_url");
//        if (StrUtil.isBlank(apiUrl)) throw new ErrorRequestException("请配置api地址");
//
//        WxPayService wxPayService = WxPayConfiguration.getPayService();
//        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
//
//        orderRequest.setTradeType("JSAPI");
//        orderRequest.setOpenid(openId);
//        orderRequest.setBody(body);
//        orderRequest.setOutTradeNo(orderId);
//        orderRequest.setTotalFee(totalFee);
//        orderRequest.setSpbillCreateIp("127.0.0.1");
//        if(type == 2){
//            orderRequest.setNotifyUrl(apiUrl + "/api/wechat/renotify");
//        }else{
//            orderRequest.setNotifyUrl(apiUrl + "/api/wechat/notify");
//        }
//
//
//        WxPayMpOrderResult orderResult = wxPayService.createOrder(orderRequest);
//
//        return orderResult;
//
//    }
//
//
//    /**
//     * 微信H5支付
//     *
//     * @param orderId
//     * @param body
//     * @param totalFee
//     * @return
//     * @throws WxPayException
//     */
//    public WxPayMwebOrderResult wxH5Pay(String orderId, String body,
//                                        Integer totalFee,int type) throws WxPayException {
//
//        String apiUrl = systemConfigService.getData("api_url");
//        if (StrUtil.isBlank(apiUrl)) throw new ErrorRequestException("请配置api地址");
//
//        WxPayService wxPayService = WxPayConfiguration.getPayService();
//        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
//
//        orderRequest.setTradeType("MWEB");
//        orderRequest.setBody(body);
//        orderRequest.setOutTradeNo(orderId);
//        orderRequest.setTotalFee(totalFee);
//        orderRequest.setSpbillCreateIp("127.0.0.1");
//        if(type == 2){
//            orderRequest.setNotifyUrl(apiUrl + "/api/wechat/renotify");
//        }else{
//            orderRequest.setNotifyUrl(apiUrl + "/api/wechat/notify");
//        }
//
//        WxPayMwebOrderResult orderResult = wxPayService.createOrder(orderRequest);
//
//        return orderResult;
//
//    }
//
//
//    /**
//     * 退款
//     * @param orderId
//     * @param totalFee
//     * @throws WxPayException
//     */
//    public void refundOrder(String orderId, Integer totalFee) throws WxPayException {
//        String apiUrl = systemConfigService.getData("api_url");
//        if (StrUtil.isBlank(apiUrl)) throw new ErrorRequestException("请配置api地址");
//
//        WxPayService wxPayService = WxPayConfiguration.getPayService();
//        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
//
//        wxPayRefundRequest.setTotalFee(totalFee);//订单总金额
//        wxPayRefundRequest.setOutTradeNo(orderId);
//        wxPayRefundRequest.setOutRefundNo(orderId);
//        wxPayRefundRequest.setRefundFee(totalFee);//退款金额
//        wxPayRefundRequest.setNotifyUrl(apiUrl + "/api/notify/refund");
//
//        wxPayService.refund(wxPayRefundRequest);
//    }
//
//}
