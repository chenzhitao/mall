package co.yixiang.mp.service;

import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.utils.CcbSignUtil;
import co.yixiang.utils.EscapeUnescape;
import co.yixiang.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.WxPayApiData;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponInfoQueryRequest;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponInfoQueryResult;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponSendRequest;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponSendResult;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponStockQueryRequest;
import com.github.binarywang.wxpay.bean.coupon.WxPayCouponStockQueryResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxScanPayNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayAuthcode2OpenidRequest;
import com.github.binarywang.wxpay.bean.request.WxPayDownloadBillRequest;
import com.github.binarywang.wxpay.bean.request.WxPayDownloadFundFlowRequest;
import com.github.binarywang.wxpay.bean.request.WxPayFaceAuthInfoRequest;
import com.github.binarywang.wxpay.bean.request.WxPayFacepayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderCloseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderReverseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayQueryCommentRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRedpackQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayReportRequest;
import com.github.binarywang.wxpay.bean.request.WxPaySendRedpackRequest;
import com.github.binarywang.wxpay.bean.request.WxPayShorturlRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayBillResult;
import com.github.binarywang.wxpay.bean.result.WxPayFaceAuthInfoResult;
import com.github.binarywang.wxpay.bean.result.WxPayFacepayResult;
import com.github.binarywang.wxpay.bean.result.WxPayFundFlowResult;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderReverseResult;
import com.github.binarywang.wxpay.bean.result.WxPayRedpackQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPaySendRedpackResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.ProfitSharingService;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 建行微信支付
 *
 * @author taozi on 2020-11-18
 */
@Slf4j
@Service
public class CcbPayService implements WxPayService {
    /** redis中存储订单号支付url的KEY */
    private static final String REDIS_KEY_PREFIX = "wx:order:url:";
    /** 订单通知状态 */
    private static final Map<String, String> ORDER_STATUS_MAP = new HashMap<>();

    static {
        ORDER_STATUS_MAP.put("1", "待支付");
        ORDER_STATUS_MAP.put("2", "已支付");
        ORDER_STATUS_MAP.put("3", "支付失败");
        ORDER_STATUS_MAP.put("4", "撤单中");
        ORDER_STATUS_MAP.put("5", "交易关闭");
    }

    @Autowired
    private CcbSignUtil ccbSignUtil;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String getPayBaseUrl() {
        return null;
    }

    @Override
    public byte[] postForBytes(String s, String s1, boolean b) throws WxPayException {
        return new byte[0];
    }

    @Override
    public String post(String s, String s1, boolean b) throws WxPayException {
        return null;
    }

    @Override
    public EntPayService getEntPayService() {
        return null;
    }

    /**
     * 获取分账服务类.
     *
     * @return the ent pay service
     */
    @Override
    public ProfitSharingService getProfitSharingService() {
        return null;
    }

    @Override
    public void setEntPayService(EntPayService entPayService) {

    }

    @Override
    public WxPayOrderQueryResult queryOrder(String s, String s1) throws WxPayException {
        return null;
    }

    @Override
    public WxPayOrderQueryResult queryOrder(WxPayOrderQueryRequest wxPayOrderQueryRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayOrderCloseResult closeOrder(String s) throws WxPayException {
        return null;
    }

    @Override
    public WxPayOrderCloseResult closeOrder(WxPayOrderCloseRequest wxPayOrderCloseRequest) throws WxPayException {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T createOrder(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest) {
        String feeType = wxPayUnifiedOrderRequest.getFeeType();
        String redisKey = RedisKeyEnum.CCB_MERCHANTID + feeType + ":" + wxPayUnifiedOrderRequest.getOutTradeNo();
        String mwebUrl = redisUtils.getY(redisKey);
        if (mwebUrl != null && !mwebUrl.isEmpty()) {
            return (T) new WxPayMwebOrderResult(mwebUrl);
        }
        Map<Object, Object> param = new HashMap<>();
        //请求参数--开始
        param.put("MERCHANTID", "105001159989338");
        param.put("POSID", "051415924");
        param.put("BRANCHID", "650000000");
        param.put("ORDERID", wxPayUnifiedOrderRequest.getOutTradeNo());
        param.put("PAYMENT", new BigDecimal(wxPayUnifiedOrderRequest.getTotalFee()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        param.put("CURCODE", "01");
        param.put("TXCODE", "530590");
        //需加密算法
        param.put("TYPE", "1");
        param.put("GATEWAY", "0");
        param.put("TRADE_TYPE", wxPayUnifiedOrderRequest.getTradeType());
        param.put("SUB_APPID", wxPayUnifiedOrderRequest.getAppid());
        param.put("SUB_OPENID", wxPayUnifiedOrderRequest.getOpenid());
        param.put("PROINFO", EscapeUnescape.escape("澳洋小程序商品购买"));
        String resultStr = ccbSignUtil.invokeMethod(param);
        log.info("返回微信支付结果：{}", resultStr);
        JSONObject result = JSONObject.parseObject(resultStr);
        if ("10000".equals(result.getString("code"))) {
            String data = result.getString("data");
            JSONObject dataJson = JSONObject.parseObject(data);
            if ("3".equals(feeType)) {

            } else {
                // 微信支付-下单
                mwebUrl = dataJson.getString("url");
                if (!mwebUrl.startsWith("http")) {
                    int indexOf = ccbSignUtil.getUrl().indexOf("/", 10);
                    String baseUrl = indexOf >= 0 ? ccbSignUtil.getUrl().substring(0, indexOf) : ccbSignUtil.getUrl();
                    mwebUrl = baseUrl + (mwebUrl.startsWith("/") ? mwebUrl : "/" + mwebUrl);
                }
                return (T) new WxPayMwebOrderResult(mwebUrl);
            }
        }
        throw new RuntimeException(result.getString("message"));
    }

    @Override
    public WxPayUnifiedOrderResult unifiedOrder(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest) throws WxPayException {
        return null;
    }

    @Override
    public Map<String, String> getPayInfo(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayConfig getConfig() {
        return null;
    }

    @Override
    public void setConfig(WxPayConfig wxPayConfig) {

    }

    @Override
    public WxPayRefundResult refund(WxPayRefundRequest wxPayRefundRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayRefundQueryResult refundQuery(String s, String s1, String s2, String s3) throws WxPayException {
        return null;
    }

    @Override
    public WxPayRefundQueryResult refundQuery(WxPayRefundQueryRequest wxPayRefundQueryRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayOrderNotifyResult parseOrderNotifyResult(String s) throws WxPayException {
        // 回调通知处理
        JSONObject jsonObject = JSONObject.parseObject(s);
        String orderId = jsonObject.getString("orderId");// 订单ID
        String orderStatus = jsonObject.getString("orderStatus");// 支付状态 (1:待支付 2:已支付 3:支付失败 4:撤单中 5:交易关闭）
        String tradeNo = jsonObject.getString("tradeNo"); // 第三方交易流水号
        String price = jsonObject.getString("price");
        if (!"1".equals(orderStatus)) {
            String redisKey = REDIS_KEY_PREFIX + tradeNo;
            redisUtils.del(redisKey);
            // 更新支付状态

        }
        if (!"2".equals(orderStatus)) {

        }
        WxPayOrderNotifyResult notifyResult = new WxPayOrderNotifyResult();
        notifyResult.setOutTradeNo(tradeNo);
        notifyResult.setAttach(tradeNo);
        return notifyResult;
    }

    @Override
    public WxPayRefundNotifyResult parseRefundNotifyResult(String s) throws WxPayException {
        return null;
    }

    @Override
    public WxScanPayNotifyResult parseScanPayNotifyResult(String s) throws WxPayException {
        return null;
    }

    @Override
    public WxPaySendRedpackResult sendRedpack(WxPaySendRedpackRequest wxPaySendRedpackRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayRedpackQueryResult queryRedpack(String s) throws WxPayException {
        return null;
    }

    @Override
    public WxPayRedpackQueryResult queryRedpack(WxPayRedpackQueryRequest wxPayRedpackQueryRequest) throws WxPayException {
        return null;
    }

    @Override
    public byte[] createScanPayQrcodeMode1(String s, File file, Integer integer) {
        return new byte[0];
    }

    @Override
    public String createScanPayQrcodeMode1(String s) {
        return null;
    }

    @Override
    public byte[] createScanPayQrcodeMode2(String s, File file, Integer integer) {
        return new byte[0];
    }

    @Override
    public void report(WxPayReportRequest wxPayReportRequest) throws WxPayException {

    }

    @Override
    public String downloadRawBill(String s, String s1, String s2, String s3) throws WxPayException {
        return null;
    }

    @Override
    public String downloadRawBill(WxPayDownloadBillRequest wxPayDownloadBillRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayBillResult downloadBill(String s, String s1, String s2, String s3) throws WxPayException {
        return null;
    }

    @Override
    public WxPayBillResult downloadBill(WxPayDownloadBillRequest wxPayDownloadBillRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayFundFlowResult downloadFundFlow(String s, String s1, String s2) throws WxPayException {
        return null;
    }

    @Override
    public WxPayFundFlowResult downloadFundFlow(WxPayDownloadFundFlowRequest wxPayDownloadFundFlowRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayMicropayResult micropay(WxPayMicropayRequest wxPayMicropayRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayOrderReverseResult reverseOrder(WxPayOrderReverseRequest wxPayOrderReverseRequest) throws WxPayException {
        return null;
    }

    @Override
    public String shorturl(WxPayShorturlRequest wxPayShorturlRequest) throws WxPayException {
        return null;
    }

    @Override
    public String shorturl(String s) throws WxPayException {
        return null;
    }

    @Override
    public String authcode2Openid(WxPayAuthcode2OpenidRequest wxPayAuthcode2OpenidRequest) throws WxPayException {
        return null;
    }

    @Override
    public String authcode2Openid(String s) throws WxPayException {
        return null;
    }

    @Override
    public String getSandboxSignKey() throws WxPayException {
        return null;
    }

    @Override
    public WxPayCouponSendResult sendCoupon(WxPayCouponSendRequest wxPayCouponSendRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayCouponStockQueryResult queryCouponStock(WxPayCouponStockQueryRequest wxPayCouponStockQueryRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayCouponInfoQueryResult queryCouponInfo(WxPayCouponInfoQueryRequest wxPayCouponInfoQueryRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayApiData getWxApiData() {
        return null;
    }

    @Override
    public String queryComment(Date date, Date date1, Integer integer, Integer integer1) throws WxPayException {
        return null;
    }

    @Override
    public String queryComment(WxPayQueryCommentRequest wxPayQueryCommentRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayFaceAuthInfoResult getWxPayFaceAuthInfo(WxPayFaceAuthInfoRequest wxPayFaceAuthInfoRequest) throws WxPayException {
        return null;
    }

    @Override
    public WxPayFacepayResult facepay(WxPayFacepayRequest wxPayFacepayRequest) throws WxPayException {
        return null;
    }
}
