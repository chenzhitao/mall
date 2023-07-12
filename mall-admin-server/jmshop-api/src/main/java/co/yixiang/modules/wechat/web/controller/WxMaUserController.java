/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.wechat.web.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.notify.NotifyType;
import co.yixiang.modules.notify.SmsResult;
import co.yixiang.modules.security.rest.param.VerityParam;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.modules.wechat.web.param.BindPhoneParam;
import co.yixiang.modules.wechat.web.param.WxPhoneParam;
import co.yixiang.mp.utils.JsonUtils;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.RedisUtils;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hupeng
 * @date 2020/02/07
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "微信其他", tags = "微信:微信其他", description = "微信其他")
public class WxMaUserController {

    private final WxMaService wxMaService;
    private final YxWechatUserService wechatUserService;
    private final YxUserService userService;
    private final RedisUtils redisUtils;



    @PostMapping("/binding")
    @ApiOperation(value = "公众号绑定手机号", notes = "公众号绑定手机号")
    public ApiResult<String> verify(@Validated @RequestBody BindPhoneParam param) {

        Object codeObj = redisUtils.get("code_" + param.getPhone());
        if(codeObj == null){
            return ApiResult.fail("请先获取验证码");
        }
        String code = codeObj.toString();


        if (!StrUtil.equals(code, param.getCaptcha())) {
            return ApiResult.fail("验证码错误");
        }

        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo userQueryVo = userService.getYxUserById(uid);
        if(StrUtil.isNotBlank(userQueryVo.getPhone())){
            return ApiResult.fail("您的账号已经绑定过手机号码");
        }

        YxUser yxUser = new YxUser();
        yxUser.setPhone(param.getPhone());
        yxUser.setUid(uid);
        userService.updateById(yxUser);

        return ApiResult.ok("绑定成功");

    }



    @PostMapping("/wxapp/binding")
    @ApiOperation(value = "小程序绑定手机号", notes = "小程序绑定手机号")
    public ApiResult<String> phone(@Validated @RequestBody WxPhoneParam param) {

        int uid = SecurityUtils.getUserId().intValue();
        YxUserQueryVo userQueryVo = userService.getYxUserById(uid);
        if(StrUtil.isNotBlank(userQueryVo.getPhone())){
            return ApiResult.fail("您的账号已经绑定过手机号码");
        }

        //读取redis配置
        String appId = RedisUtil.get(RedisKeyEnum.WXAPP_APPID.getValue());
        String secret = RedisUtil.get(RedisKeyEnum.WXAPP_SECRET.getValue());
        if (StrUtil.isBlank(appId) || StrUtil.isBlank(secret)) {
            throw new ErrorRequestException("请先配置小程序");
        }
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(appId);
        wxMaConfig.setSecret(secret);
        wxMaService.setWxMaConfig(wxMaConfig);
        String phone = "";
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService()
                    .getSessionInfo(param.getCode());

            // 解密
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService()
                    .getPhoneNoInfo(session.getSessionKey(), param.getEncryptedData(), param.getIv());

            phone = phoneNoInfo.getPhoneNumber();
            YxUser yxUser = new YxUser();
            yxUser.setPhone(phone);
            yxUser.setUid(uid);
            userService.updateById(yxUser);
        } catch (WxErrorException e) {
            return ApiResult.fail(e.getMessage());
            //e.printStackTrace();
        }
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("phone",phone);

        return ApiResult.ok(map,"绑定成功");
    }



}
