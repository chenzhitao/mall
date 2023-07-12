/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.manage.web.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.manage.service.YxExpressService;
import co.yixiang.modules.manage.web.dto.OrderTimeDataDTO;
import co.yixiang.modules.manage.web.param.OrderDeliveryParam;
import co.yixiang.modules.manage.web.param.OrderPriceParam;
import co.yixiang.modules.manage.web.param.OrderRefundParam;
import co.yixiang.modules.manage.web.param.ShoperQueryParam;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.order.web.dto.OrderCountDTO;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShoperController
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/25
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "商家管理", tags = "商城:商家管理", description = "商家管理")
public class ShoperController extends BaseController {

    private final YxStoreOrderService storeOrderService;
    private final YxExpressService expressService;

    /**
     * 订单数据统计
     */
    @GetMapping("/admin/order/statistics")
    @ApiOperation(value = "订单数据统计",notes = "订单数据统计")
    public ApiResult<Object> statistics(){
        OrderCountDTO orderCountDTO  = storeOrderService.orderData(0);
        OrderTimeDataDTO orderTimeDataDTO = storeOrderService.getOrderTimeData();

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("orderCount",orderCountDTO);
        map.put("orderTimeCount",orderTimeDataDTO);
        return ApiResult.ok(map);
    }

    /**
     * 订单每月统计数据
     */
    @GetMapping("/admin/order/data")
    @ApiOperation(value = "订单每月统计数据",notes = "订单每月统计数据")
    public ApiResult<Object> data(ShoperQueryParam queryParam){
        return ApiResult.ok(storeOrderService.getOrderDataPriceCount(queryParam.getPage().intValue(),
                queryParam.getLimit().intValue()));
    }


    /**
     * 订单列表
     */
    @GetMapping("/admin/order/list")
    @ApiOperation(value = "订单列表",notes = "订单列表")
    public ApiResult<Object> orderList(ShoperQueryParam queryParam) {
        return ApiResult.ok(storeOrderService.orderList(0, queryParam.getStatus().intValue(),
                queryParam.getPage().intValue(), queryParam.getLimit().intValue()));
    }

    /**
     * 订单详情
     */
    @GetMapping("/admin/order/detail/{key}")
    @ApiOperation(value = "订单详情",notes = "订单详情")
    public ApiResult<Object> orderDetail(@PathVariable String key){
        if(StrUtil.isEmpty(key)) {
            return ApiResult.fail("参数错误");
        }
        YxStoreOrderQueryVo storeOrder = storeOrderService.getOrderInfo(key,0);
        if(ObjectUtil.isNull(storeOrder)){
            return ApiResult.fail("订单不存在");
        }

        return ApiResult.ok(storeOrderService.handleOrder(storeOrder));
    }

    /**
     * 订单改价
     */
    @PostMapping("/admin/order/price")
    @ApiOperation(value = "订单改价",notes = "订单改价")
    public ApiResult<Object> orderPrice(@Validated @RequestBody OrderPriceParam param){
        storeOrderService.editOrderPrice(param);
        return ApiResult.ok("ok");
    }

    /**
     * 快递公司
     */
    @AnonymousAccess
    @GetMapping("/logistics")
    @ApiOperation(value = "快递公司",notes = "快递公司")
    public ApiResult<Object> express(){
        return ApiResult.ok(expressService.list());
    }


    /**
     * 订单发货
     */
    @PostMapping("/admin/order/delivery/keep")
    @ApiOperation(value = "订单发货",notes = "订单发货")
    public ApiResult<Object> orderDelivery(@Validated @RequestBody OrderDeliveryParam param){
        storeOrderService.orderDelivery(param);
        return ApiResult.ok("ok");
    }

    /**
     * 订单退款
     */
    @PostMapping("/admin/order/refund")
    @ApiOperation(value = "订单退款",notes = "订单退款")
    public ApiResult<Object> orderRefund(@Validated @RequestBody OrderRefundParam param){
        storeOrderService.orderRefund(param);
        return ApiResult.ok("ok");
    }


    /**
     * 订单交易额/订单数量时间chart统计
     */
    @GetMapping("/admin/order/time")
    @ApiOperation(value = "chart统计",notes = "chart统计")
    public ApiResult<Object> chartCount(ShoperQueryParam queryParam){
        return ApiResult.ok(storeOrderService.chartCount(queryParam.getCate().intValue(),
                queryParam.getType().intValue()));
    }






}
