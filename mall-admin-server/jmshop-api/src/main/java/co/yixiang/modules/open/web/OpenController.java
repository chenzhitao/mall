package co.yixiang.modules.open.web;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.modules.manage.entity.YxExpress;
import co.yixiang.modules.manage.service.YxExpressService;
import co.yixiang.modules.manage.web.vo.YxExpressQueryVo;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.entity.YxStoreOrderCartInfo;
import co.yixiang.modules.order.service.YxStoreOrderCartInfoService;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.shop.entity.YxSystemGroupData;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author : godly.strong
 * mail : huangjunquan1109@163.com
 * @since : 2021/4/2 17:05
 * describe ：进行第三方
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "开放模块", tags = "开放API:订单API", description = "订单API")
public class OpenController {

    private final YxStoreOrderService storeOrderService;
    private final YxStoreOrderCartInfoService yxStoreOrderCartInfoService;
    private final YxSystemConfigService systemConfigService;
    private final YxExpressService expressService;


    /**
     * 获取订单列表
     */
    @AnonymousAccess
    @GetMapping("/open/findPageOrder")
    @ApiOperation(value = "获取订单列表", notes = "获取订单列表")
    public ApiResult index(String appId, Integer status, Long pageNo, Long pageSize, String startDate, String endDate) {
        String WXAPP_APPID = systemConfigService.getData(RedisKeyEnum.WXAPP_APPID.getValue());
        if (!appId.equals(WXAPP_APPID)) {
            return ApiResult.fail("匹配错误，无法请求数据");
        }
        //页码
        if (pageNo == null) {
            pageNo = 1L;
        }
        //每页多少个
        if (pageSize == null) {
            pageSize = 100L;
        }
        //查询订单
        LambdaQueryWrapper<YxStoreOrder> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(YxStoreOrder::getStatus, status);
        }

        //开始时间
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.apply(" add_time > '" + startDate + "'");
        }
        //结束时间
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.apply(" add_time < '" + endDate + "' ");
        }
        wrapper.orderByDesc(YxStoreOrder::getAddTime);
        IPage<YxStoreOrder> pages = storeOrderService.page(new Page<>(pageNo, pageSize), wrapper);
        //获取详情
        pages.getRecords().forEach(x -> {
                    x.setYxStoreOrderCartInfoList(yxStoreOrderCartInfoService.list(new QueryWrapper<YxStoreOrderCartInfo>().lambda().eq(YxStoreOrderCartInfo::getOid, x.getId())));
                }
        );
        return ApiResult.ok(pages);
    }


    /**
     * 订单发货
     *
     * @param appId
     * @param orderId
     * @param expressCompanyName   物流公司名称
     * @param expressCompanyNumber 物流公司单号
     * @return
     */
    @AnonymousAccess
    @GetMapping("/open/saveLogistics")
    @ApiOperation(value = "订单发货", notes = "订单发货")
    public ApiResult saveLogistics(String appId, String orderId, String expressCompanyName, String expressCompanyNumber) {
        String WXAPP_APPID = systemConfigService.getData(RedisKeyEnum.WXAPP_APPID.getValue());
        if (!appId.equals(WXAPP_APPID)) {
            return ApiResult.fail("匹配错误，无法请求数据!");
        }
        YxStoreOrder yxStoreOrder = storeOrderService.getOne(new QueryWrapper<YxStoreOrder>().lambda().eq(YxStoreOrder::getOrderId, orderId));
        if (yxStoreOrder != null) {
            List<YxExpress> yxExpress = expressService.list(new QueryWrapper<YxExpress>().lambda().eq(YxExpress::getName, expressCompanyName));
            if (yxExpress.size() <= 0) {
                return ApiResult.fail("匹配错误，物流公司未找到!");
            }
            yxStoreOrder.setDeliveryId(expressCompanyNumber);
            yxStoreOrder.setDeliveryName(yxExpress.get(0).getName());
            yxStoreOrder.setDeliverySn(yxExpress.get(0).getCode());
            yxStoreOrder.setStatus(1);
            storeOrderService.updateById(yxStoreOrder);

            Map<String, String> map = new HashMap<String, String>();
            map.put("falg", "true");
            return ApiResult.ok(map);
        }
        return ApiResult.fail("订单未找到!");
    }

}
