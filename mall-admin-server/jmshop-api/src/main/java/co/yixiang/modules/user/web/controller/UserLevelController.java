/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.web.controller;

import co.yixiang.aop.log.Log;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.user.service.YxSystemUserLevelService;
import co.yixiang.modules.user.service.YxSystemUserTaskService;
import co.yixiang.modules.user.service.YxUserLevelService;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户等级 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户等级", tags = "用户:用户等级", description = "用户等级")
public class UserLevelController extends BaseController {

    private final YxUserLevelService userLevelService;
    private final YxSystemUserLevelService systemUserLevelService;
    private final YxSystemUserTaskService systemUserTaskService;

    /**
    * 会员等级列表
    */
    @Log(value = "进入会员中心",type = 1)
    @GetMapping("/user/level/grade")
    @ApiOperation(value = "会员等级列表",notes = "会员等级列表")
    public ApiResult<Object> getLevelInfo(){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(systemUserLevelService.getLevelInfo(uid,true));
    }

    /**
     * 获取等级任务
     */
    @GetMapping("/user/level/task/{id}")
    @ApiOperation(value = "获取等级任务",notes = "获取等级任务")
    public ApiResult<Object> getTask(@PathVariable Integer id){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(systemUserTaskService.getTaskList(id,uid,null));
    }

    /**
     * 检测用户是否可以成为会员
     */
    @GetMapping("/user/level/detection")
    @ApiOperation(value = "检测用户是否可以成为会员",notes = "检测用户是否可以成为会员")
    public ApiResult<Object> detection(){
        int uid = SecurityUtils.getUserId().intValue();
        boolean res = userLevelService.setLevelComplete(uid);
        if(res){
            return ApiResult.ok("升级成功!");
        }else{
            return ApiResult.fail("还不符合升级条件哦!");
        }

    }



}

