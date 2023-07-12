/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.aop.log.Log;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.shop.entity.YxSystemGroupData;
import co.yixiang.modules.shop.service.YxStoreProductRelationService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.service.*;
import co.yixiang.modules.user.web.param.UserEditParam;
import co.yixiang.modules.user.web.param.YxUserQueryParam;
import co.yixiang.modules.user.web.param.YxUserSignQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import co.yixiang.modules.user.web.vo.YxUserLevelQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-10-16
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户中心", tags = "用户:用户中心", description = "用户中心")
public class UserController extends BaseController {

    private final YxUserService yxUserService;
    private final YxSystemGroupDataService systemGroupDataService;
    private final YxStoreOrderService orderService;
    private final YxStoreProductRelationService relationService;
    private final YxUserSignService userSignService;
    private final YxUserBillService userBillService;
    private final YxSystemUserLevelService systemUserLevelService;
    private final YxUserLevelService yxUserLevelService;

    @Autowired
    private YxSystemConfigService systemConfigService;

    private static Lock lock = new ReentrantLock(false);

    /**
     * 用户资料
     */
    @GetMapping("/userinfo")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", response = YxUserQueryVo.class)
    public ApiResult<Object> userInfo() {
        int uid = SecurityUtils.getUserId().intValue();
        //update count
        yxUserService.setUserSpreadCount(uid);
        return ApiResult.ok(yxUserService.getNewYxUserById(uid));
    }

    @GetMapping("/getBuyInfo")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", response = YxUserQueryVo.class)
    public ApiResult<Object> getBuyInfo() {
        String buyInfo = systemConfigService.getData("store_buy_info");
        return ApiResult.ok(buyInfo);
    }

    @GetMapping("/getNoticeInfo/{id}")
    @ApiOperation(value = "获取公告信息", notes = "获取公告信息", response = YxUserQueryVo.class)
    public ApiResult<Object> getNoticeInfo(@PathVariable Integer id) {
        YxSystemGroupData data = systemGroupDataService.findData(id);
        return ApiResult.ok(data);
    }

    @GetMapping("/getJoinGroupInfo")
    @ApiOperation(value = "获取入群信息", notes = "获取入群信息", response = YxUserQueryVo.class)
    public ApiResult<Object> getJoinGroupInfo() {
        String joinGroup = systemConfigService.getData("store_join_group");
        return ApiResult.ok(joinGroup);
    }



    /**
     * 获取个人中心菜单
     */
    @Log(value = "进入用户中心", type = 1)
    @GetMapping("/menu/user")
    @ApiOperation(value = "获取个人中心菜单", notes = "获取个人中心菜单")
    public ApiResult<Map<String, Object>> userMenu() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("routine_my_menus", systemGroupDataService.getDatas(ShopConstants.JMSHOP_MY_MENUES));
        return ApiResult.ok(map);
    }

    /**
     * 个人中心
     */
    @GetMapping("/user")
    @ApiOperation(value = "个人中心", notes = "个人中心")
    public ApiResult<Object> user() {
        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo yxUserQueryVo = yxUserService.getNewYxUserById(uid);
        if (yxUserQueryVo.getLevel() > 0) {
            yxUserQueryVo.setVip(true);
            YxSystemUserLevelQueryVo systemUserLevelQueryVo = systemUserLevelService.getYxSystemUserLevelById(yxUserQueryVo.getLevel());
            if (systemUserLevelQueryVo != null) {
                yxUserQueryVo.setVipIcon(systemUserLevelQueryVo.getIcon());
                yxUserQueryVo.setVipId(yxUserQueryVo.getLevel());
                yxUserQueryVo.setVipName(systemUserLevelQueryVo.getName());
            }
        }
        return ApiResult.ok(yxUserQueryVo);
    }

    @PostMapping("/user/applylevel")
    @ApiOperation(value = "个人中心申请会员级别", notes = "个人中心申请会员级别")
    public ApiResult<String> applyLevel(@RequestBody YxUserQueryParam yxUserQueryParam) {
        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo yxUserQueryVo = yxUserService.getYxUserById(uid);
        YxUser yxUser = new YxUser();
        yxUser.setUid(uid);
        yxUser.setApplyLevel(yxUserQueryParam.getApplyLevel());
        yxUser.setCertInfo(yxUserQueryParam.getCertInfo());
        yxUser.setIsCheck(0);
        yxUser.setIsPass(0);
        yxUserService.updateById(yxUser);
//        yxUserLevelService.setUserLevel(uid, yxUserQueryVo.getLevel());

        return ApiResult.ok("纸质已经提交成功，请耐心等待工作人员审核！");
    }

    /**
     * 订单统计数据
     */
    @GetMapping("/order/data")
    @ApiOperation(value = "订单统计数据", notes = "订单统计数据")
    public ApiResult<Object> orderData() {
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(orderService.orderData(uid));
    }

    /**
     * 获取收藏产品
     */
    @GetMapping("/collect/user")
    @ApiOperation(value = "获取收藏产品", notes = "获取收藏产品")
    public ApiResult<Object> collectUser(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit) {
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(relationService.userCollectProduct(page, limit, uid));
    }

    /**
     * 用户资金统计
     */
    @GetMapping("/user/balance")
    @ApiOperation(value = "用户资金统计", notes = "用户资金统计")
    public ApiResult<Object> collectUser() {
        int uid = SecurityUtils.getUserId().intValue();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("now_money", yxUserService.getYxUserById(uid).getNowMoney());
        map.put("orderStatusSum", orderService.orderData(uid).getSumPrice());
        map.put("recharge", 0);
        return ApiResult.ok(map);
    }

    /**
     * 获取活动状态
     */
    @AnonymousAccess
    @GetMapping("/user/activity")
    @ApiOperation(value = "获取活动状态", notes = "获取活动状态")
    @Deprecated
    public ApiResult<Object> activity() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("is_bargin", false);
        map.put("is_pink", false);
        map.put("is_seckill", false);
        return ApiResult.ok(map);
    }

    /**
     * 签到用户信息
     */
    @PostMapping("/sign/user")
    @ApiOperation(value = "签到用户信息", notes = "签到用户信息")
    public ApiResult<Object> sign(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo userQueryVo = yxUserService.getYxUserById(uid);
        int sumSignDay = userSignService.getSignSumDay(uid);
        boolean isDaySign = userSignService.getToDayIsSign(uid);
        boolean isYesterDaySign = userSignService.getYesterDayIsSign(uid);
        userQueryVo.setSumSignDay(sumSignDay);
        userQueryVo.setIsDaySign(isDaySign);
        userQueryVo.setIsYesterDaySign(isYesterDaySign);
        if (!isDaySign && !isYesterDaySign) {
            userQueryVo.setSignNum(0);
        }
        return ApiResult.ok(userQueryVo);
    }

    /**
     * 签到配置
     */
    @GetMapping("/sign/config")
    @ApiOperation(value = "签到配置", notes = "签到配置")
    public ApiResult<Object> signConfig() {
        return ApiResult.ok(systemGroupDataService.getDatas(ShopConstants.JMSHOP_SIGN_DAY_NUM));
    }

    /**
     * 签到列表
     */
    @GetMapping("/sign/list")
    @ApiOperation(value = "签到列表", notes = "签到列表")
    public ApiResult<Object> signList(YxUserSignQueryParam queryParam) {
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(userSignService.getSignList(uid, queryParam.getPage().intValue(),
                queryParam.getLimit().intValue()));
    }

    /**
     * 签到列表（年月）
     */
    @GetMapping("/sign/month")
    @ApiOperation(value = "签到列表（年月）", notes = "签到列表（年月）")
    public ApiResult<Object> signMonthList(YxUserSignQueryParam queryParam) {
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(userBillService.getUserBillList(queryParam.getPage().intValue(),
                queryParam.getLimit().intValue(), uid, 5));
    }

    /**
     * 开始签到
     */
    @PostMapping("/sign/integral")
    @ApiOperation(value = "开始签到", notes = "开始签到")
    public ApiResult<Object> signIntegral() {
        int uid = SecurityUtils.getUserId().intValue();
        boolean isDaySign = userSignService.getToDayIsSign(uid);
        if (isDaySign) {
            return ApiResult.fail("已签到");
        }
        int integral = 0;
        try {
            lock.lock();
            integral = userSignService.sign(uid);
        } finally {
            lock.unlock();
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("integral", integral);
        return ApiResult.ok(map, "签到获得" + integral + "积分");
    }


    @PostMapping("/user/edit")
    @ApiOperation(value = "用户修改信息", notes = "用修改信息")
    public ApiResult<Object> edit(@Validated @RequestBody UserEditParam param) {
        int uid = SecurityUtils.getUserId().intValue();

        YxUser yxUser = new YxUser();
        yxUser.setAvatar(param.getAvatar());
        yxUser.setNickname(param.getNickname());
        yxUser.setUid(uid);

        yxUser.setAddres(param.getAddres());
        yxUser.setQq(param.getQq());
        yxUser.setWeixin(param.getWeixin());
        yxUser.setCardId(param.getCardId());
        yxUser.setMail(param.getMail());
        yxUser.setTelephone(param.getTelephone());
        yxUser.setDingding(param.getDingding());

        yxUserService.updateById(yxUser);

        return ApiResult.ok("修改成功");
    }


}

