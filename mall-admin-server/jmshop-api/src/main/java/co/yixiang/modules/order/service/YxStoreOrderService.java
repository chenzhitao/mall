/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.order.service;

import co.yixiang.modules.manage.web.dto.OrderDataDTO;
import co.yixiang.modules.manage.web.dto.OrderTimeDataDTO;
import co.yixiang.modules.manage.web.param.OrderDeliveryParam;
import co.yixiang.modules.manage.web.param.OrderPriceParam;
import co.yixiang.modules.manage.web.param.OrderRefundParam;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.order.web.dto.*;
import co.yixiang.modules.order.web.param.OrderParam;
import co.yixiang.modules.order.web.param.RefundParam;
import co.yixiang.modules.order.web.param.YxStoreOrderQueryParam;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;



import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
public interface YxStoreOrderService extends BaseService<YxStoreOrder> {

    Map<String,Object> chartCount(int cate,int type);

    void orderRefund(OrderRefundParam param);

    void orderDelivery(OrderDeliveryParam param);

    void editOrderPrice(OrderPriceParam param);

    List<OrderDataDTO> getOrderDataPriceCount(int page, int limit);

    OrderTimeDataDTO getOrderTimeData();

    YxStoreOrder getOrderPink(int pid,int uid,int type);

    void regressionCoupon(YxStoreOrderQueryVo order);

    void regressionStock(YxStoreOrderQueryVo order);

    void regressionIntegral(YxStoreOrderQueryVo order);

    void cancelOrder(String orderId,int uid);

    void cancelOrderByTask(int id);

    void orderApplyRefund(RefundParam param,int uid);

    void removeOrder(String orderId,int uid);

    void gainUserIntegral(YxStoreOrderQueryVo order);

    void takeOrder(String orderId,int uid);

    void verificOrder(String orderId);

    List<YxStoreOrderQueryVo> orderList(int uid,int type,int page,int limit);

    //@WebMethod
    OrderCountDTO orderData(int uid);

    YxStoreOrderQueryVo handleOrder(YxStoreOrderQueryVo order);

    void paySuccess(String orderId,String payType);

    void yuePay(String orderId,int uid);

    WxPayMpOrderResult wxAppPay(String orderId) throws WxPayException;

    WxPayMpOrderResult wxPay(String orderId) throws WxPayException;

    WxPayMwebOrderResult wxH5Pay(String orderId) throws WxPayException;

    WxPayAppOrderResult appPay(String orderId) throws WxPayException;

    String aliPay(String orderId) throws Exception;

    void delCacheOrderInfo(int uid, String key);

    YxStoreOrder createOrder(int uid, String key, OrderParam param);

    ComputeDTO computedOrder(int uid, String key, int couponId,
                             int useIntegral, int shippingType);

    YxStoreOrderQueryVo getOrderInfo(String unique,int uid);

    String cacheOrderInfo(int uid,List<YxStoreCartQueryVo> cartInfo,
                          PriceGroupDTO priceGroup,OtherDTO other);

    CacheDTO getCacheOrderInfo(int uid, String key);

    PriceGroupDTO getOrderPriceGroup(List<YxStoreCartQueryVo> cartInfo);

    Double getOrderSumPrice(List<YxStoreCartQueryVo> cartInfo,String key);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreOrderQueryVo getYxStoreOrderById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreOrderQueryParam
     * @return
     */
    Paging<YxStoreOrderQueryVo> getYxStoreOrderPageList(YxStoreOrderQueryParam yxStoreOrderQueryParam) throws Exception;

}
