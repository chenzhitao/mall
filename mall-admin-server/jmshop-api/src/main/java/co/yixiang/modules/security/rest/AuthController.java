/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.security.rest;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.aop.log.Log;
import co.yixiang.common.api.ApiCode;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.notify.NotifyService;
import co.yixiang.modules.notify.NotifyType;
import co.yixiang.modules.notify.SmsResult;
import co.yixiang.modules.security.config.SecurityProperties;
import co.yixiang.modules.security.rest.param.LoginParam;
import co.yixiang.modules.security.rest.param.RegParam;
import co.yixiang.modules.security.rest.param.VerityParam;
import co.yixiang.modules.security.security.TokenProvider;
import co.yixiang.modules.security.security.vo.AuthUser;
import co.yixiang.modules.security.security.vo.JwtUser;
import co.yixiang.modules.security.service.OnlineUserService;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.service.*;
import co.yixiang.modules.user.web.param.YxSystemUserLevelQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.modules.user.web.vo.YxWechatUserQueryVo;
import co.yixiang.mp.config.WxMpConfiguration;
import co.yixiang.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonResponse;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hupeng
 * @date 2020/01/12
 */
@Slf4j
@RestController
@Api(tags = "授权:用户授权中心")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    @Value("${single.login}")
    private Boolean singleLogin;
    @Value("${jmshop.notify.sms.enable}")
    private Boolean enableSms;

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final UserDetailsService userDetailsService;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final YxUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final YxWechatUserService wechatUserService;
    private final WxMaService wxMaService;
    private final NotifyService notifyService;
    private final YxSystemAttachmentService systemAttachmentService;
    private final YxSystemUserLevelService yxSystemUserLevelService;
    private final YxUserLevelService yxUserLevelService;


    @Log("H5/APP用户登录")
    @ApiOperation("H5/APP登录授权")
    @AnonymousAccess
    @PostMapping(value = "/login")
    public ApiResult<Map<String, String>> login(@Validated @RequestBody AuthUser authUser,
                                                HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);

        Date expiresTime = tokenProvider.getExpirationDateFromToken(token);
        String expiresTimeStr = DateUtil.formatDateTime(expiresTime);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", token);
            put("expires_time", expiresTimeStr);
        }};
        if (singleLogin) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }

        //设置推广关系
        if (StrUtil.isNotEmpty(authUser.getSpread()) && !authUser.getSpread().equals("NaN")) {
            userService.setSpread(Integer.valueOf(authUser.getSpread()),
                    jwtUser.getId().intValue());
        }

        // 返回 token
        return ApiResult.ok(authInfo);
    }

    /**
     * 微信公众号授权
     */
    @AnonymousAccess
    @GetMapping("/wechat/auth")
    @ApiOperation(value = "微信公众号授权", notes = "微信公众号授权")
    public ApiResult<Object> authLogin(@RequestParam(value = "code") String code,
                                       @RequestParam(value = "spread") String spread,
                                       HttpServletRequest request) {

        /**
         * 公众号与小程序打通说明：
         * 1、打通方式以UnionId方式，需要去注册微信开放平台
         * 2、目前登陆授权打通方式适用于新项目（也就是你yx_user、yx_wechat_user都是空的）
         * 3、如果你以前已经有数据请自行处理
         */

        try {
            WxMpService wxService = WxMpConfiguration.getWxMpService();
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            String openid = wxMpUser.getOpenId();

            //如果开启了UnionId
            if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                openid = wxMpUser.getUnionId();
            }
            YxUser yxUser = userService.findByName(openid);

            String username = "";
            if (ObjectUtil.isNull(yxUser)) {
                //过滤掉表情
                String nickname = EmojiParser.removeAllEmojis(wxMpUser.getNickname());
                log.info("昵称：{}", nickname);
                //用户保存
                YxUser user = new YxUser();
                user.setAccount(nickname);
                //如果开启了UnionId
                if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    username = wxMpUser.getUnionId();
                    user.setUsername(wxMpUser.getUnionId());
                } else {
                    username = wxMpUser.getOpenId();
                    user.setUsername(wxMpUser.getOpenId());
                }
                user.setPassword(passwordEncoder.encode(ShopConstants.JMSHOP_DEFAULT_PWD));
                user.setPwd(passwordEncoder.encode(ShopConstants.JMSHOP_DEFAULT_PWD));
                user.setPhone("");
                user.setUserType(AppFromEnum.WECHAT.getValue());
                user.setLoginType(AppFromEnum.WECHAT.getValue());
                user.setAddTime(OrderUtil.getSecondTimestampTwo());
                user.setLastTime(OrderUtil.getSecondTimestampTwo());
                user.setNickname(nickname);
                user.setAvatar(wxMpUser.getHeadImgUrl());
                user.setNowMoney(BigDecimal.ZERO);
                user.setBrokeragePrice(BigDecimal.ZERO);
                user.setIntegral(BigDecimal.ZERO);

                int isCheck = 0;
                int isPass = 0;
                if (yxSystemUserLevelService.getById(1).getName().indexOf("普通") >= 0) {
                    isCheck = 1;
                    isPass = 1;
                }
                user.setLevel(0);
                user.setApplyLevel(0);
                user.setCertInfo("");
                user.setIsCheck(isCheck);
                user.setIsPass(isPass);
                userService.save(user);

                //保存微信用户
                YxWechatUser yxWechatUser = new YxWechatUser();
                yxWechatUser.setAddTime(OrderUtil.getSecondTimestampTwo());
                yxWechatUser.setNickname(nickname);
                yxWechatUser.setOpenid(wxMpUser.getOpenId());
                int sub = 0;
                if (ObjectUtil.isNotNull(wxMpUser.getSubscribe()) && wxMpUser.getSubscribe()) {
                    sub = 1;
                }
                yxWechatUser.setSubscribe(sub);
                yxWechatUser.setSex(wxMpUser.getSex());
                yxWechatUser.setLanguage(wxMpUser.getLanguage());
                yxWechatUser.setCity(wxMpUser.getCity());
                yxWechatUser.setProvince(wxMpUser.getProvince());
                yxWechatUser.setCountry(wxMpUser.getCountry());
                yxWechatUser.setHeadimgurl(wxMpUser.getHeadImgUrl());
                if (ObjectUtil.isNotNull(wxMpUser.getSubscribeTime())) {
                    yxWechatUser.setSubscribeTime(wxMpUser.getSubscribeTime().intValue());
                }
                if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    yxWechatUser.setUnionid(wxMpUser.getUnionId());
                }
                if (StrUtil.isNotEmpty(wxMpUser.getRemark())) {
                    yxWechatUser.setUnionid(wxMpUser.getRemark());
                }
                if (ObjectUtil.isNotEmpty(wxMpUser.getGroupId())) {
                    yxWechatUser.setGroupid(wxMpUser.getGroupId());
                }
                yxWechatUser.setUid(user.getUid());

                wechatUserService.save(yxWechatUser);

            } else {
                username = yxUser.getUsername();
                if (StrUtil.isNotBlank(wxMpUser.getOpenId()) || StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    YxWechatUser wechatUser = new YxWechatUser();
                    wechatUser.setUid(yxUser.getUid());
                    wechatUser.setUnionid(wxMpUser.getUnionId());
                    wechatUser.setOpenid(wxMpUser.getOpenId());

                    wechatUserService.updateById(wechatUser);
                }
            }


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username,
                            ShopConstants.JMSHOP_DEFAULT_PWD);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 生成令牌
            String token = tokenProvider.createToken(authentication);
            final JwtUser jwtUserT = (JwtUser) authentication.getPrincipal();
            // 保存在线信息
            onlineUserService.save(jwtUserT, token, request);

            Date expiresTime = tokenProvider.getExpirationDateFromToken(token);
            String expiresTimeStr = DateUtil.formatDateTime(expiresTime);

            Map<String, String> map = new LinkedHashMap<>();
            map.put("token", token);
            map.put("expires_time", expiresTimeStr);

            if (singleLogin) {
                //踢掉之前已经登录的token
                onlineUserService.checkLoginOnUser(jwtUserT.getUsername(), token);
            }

            //设置推广关系
            if (StrUtil.isNotEmpty(spread) && !spread.equals("NaN")) {
                userService.setSpread(Integer.valueOf(spread),
                        jwtUserT.getId().intValue());
            }
            //默认注册普通会员
//            yxUserLevelService.setUserLevel(jwtUserT.getId().intValue(), 1);

            // 返回 token
            return ApiResult.ok(map);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ApiResult.fail("授权失败");
        }

    }


    /**
     * 小程序登陆接口
     */
    @AnonymousAccess
    @PostMapping("/wxapp/auth")
    @ApiOperation(value = "小程序登陆", notes = "小程序登陆")
    public ApiResult<Object> login(@Validated @RequestBody LoginParam loginParam,
                                   HttpServletRequest request) {
        /**
         * 公众号与小程序打通说明：
         * 1、打通方式以UnionId方式，需要去注册微信开放平台
         * 2、目前登陆授权打通方式适用于新项目（也就是你yx_user、yx_wechat_user都是空的）
         * 3、如果你以前已经有数据请自行处理
         */
        String code = loginParam.getCode();
        String encryptedData = loginParam.getEncryptedData();
        String iv = loginParam.getIv();
        String spread = loginParam.getSpread();
        try {
            //读取redis配置
            //  String appId = RedisUtil.get(RedisKeyEnum.WXAPP_APPID.getValue());
            //  String secret = RedisUtil.get(RedisKeyEnum.WXAPP_SECRET.getValue());
            //防止体验用户乱改导致登录无法体验，这里写死处理
            String appId = "wx9984e170383a3108";
            String secret = "89a6e5ed406bb33bfa648f8e878d4edd";

            if (StrUtil.isBlank(appId) || StrUtil.isBlank(secret)) {
                throw new ErrorRequestException("请先配置小程序");
            }
            WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
            wxMaConfig.setAppid(appId);
            wxMaConfig.setSecret(secret);

            wxMaService.setWxMaConfig(wxMaConfig);
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            //解密数据,防止 session 无 unionId
            WxMaUserInfo wxMpUser = wxMaService.getUserService()
                    .getUserInfo(session.getSessionKey(), encryptedData, iv);

            String openid = session.getOpenid();
            if (StringUtils.isEmpty(session.getOpenid())) {
                //如果开启了UnionId
                openid = wxMpUser.getOpenId();
                if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    openid = wxMpUser.getUnionId();
                }
            }
            if (StrUtil.isBlank(openid)) {
                throw new ErrorRequestException("登录失败，获取登录信息有误！");
            }
            YxUser yxUser = userService.findByName(openid);
            String username = openid;
            if (ObjectUtil.isNull(yxUser)) {
                //过滤掉表情
                String nickname = EmojiParser.removeAllEmojis(wxMpUser.getNickName());
                //用户保存
                YxUser user = new YxUser();
                user.setAccount(nickname);
                //昵称存入openId
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(ShopConstants.JMSHOP_DEFAULT_PWD));
                user.setPwd(passwordEncoder.encode(ShopConstants.JMSHOP_DEFAULT_PWD));
                user.setPhone("");
                user.setUserType(AppFromEnum.ROUNTINE.getValue());
                user.setAddTime(OrderUtil.getSecondTimestampTwo());
                user.setLastTime(OrderUtil.getSecondTimestampTwo());
                user.setNickname(nickname);
                user.setAvatar(wxMpUser.getAvatarUrl());
                user.setNowMoney(BigDecimal.ZERO);
                user.setBrokeragePrice(BigDecimal.ZERO);
                user.setIntegral(BigDecimal.ZERO);

                user.setLevel(0);
                user.setApplyLevel(0);
                user.setCertInfo("");
                user.setIsCheck(1);
                user.setIsPass(1);
                userService.save(user);


                //保存微信用户
                YxWechatUser yxWechatUser = new YxWechatUser();
                // System.out.println("wxMpUser:"+wxMpUser);
                yxWechatUser.setAddTime(OrderUtil.getSecondTimestampTwo());
                yxWechatUser.setNickname(nickname);
                yxWechatUser.setRoutineOpenid(openid);
                int sub = 0;
                yxWechatUser.setSubscribe(sub);
                yxWechatUser.setSex(Integer.valueOf(wxMpUser.getGender()));
                yxWechatUser.setLanguage(wxMpUser.getLanguage());
                yxWechatUser.setCity(wxMpUser.getCity());
                yxWechatUser.setProvince(wxMpUser.getProvince());
                yxWechatUser.setCountry(wxMpUser.getCountry());
                yxWechatUser.setHeadimgurl(wxMpUser.getAvatarUrl());
                if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    yxWechatUser.setUnionid(wxMpUser.getUnionId());
                }
                yxWechatUser.setUid(user.getUid());

                wechatUserService.save(yxWechatUser);
            } else {
                username = yxUser.getUsername();
                if (StrUtil.isNotBlank(openid)) {
                    YxWechatUser wechatUser = wechatUserService.getById(yxUser.getUid());
                    if (wechatUser != null) {
                        wechatUser.setUnionid(wxMpUser.getUnionId());
                        wechatUser.setRoutineOpenid(openid);
                        wechatUserService.updateById(wechatUser);
                    }
                }
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username,
                            ShopConstants.JMSHOP_DEFAULT_PWD);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 生成令牌
            String token = tokenProvider.createToken(authentication);
            final JwtUser jwtUserT = (JwtUser) authentication.getPrincipal();
            // 保存在线信息
            onlineUserService.save(jwtUserT, token, request);

            Date expiresTime = tokenProvider.getExpirationDateFromToken(token);
            String expiresTimeStr = DateUtil.formatDateTime(expiresTime);


            Map<String, String> map = new LinkedHashMap<>();
            map.put("token", token);
            map.put("expires_time", expiresTimeStr);

            if (singleLogin) {
                //踢掉之前已经登录的token
                onlineUserService.checkLoginOnUser(jwtUserT.getUsername(), token);
            }

            //设置推广关系
            if (StrUtil.isNotEmpty(spread)) {
                userService.setSpread(Integer.valueOf(spread),
                        jwtUserT.getId().intValue());
            }

            //默认注册普通会员
//            yxUserLevelService.setUserLevel(jwtUserT.getId().intValue(), 1);

            // 返回 token
            return ApiResult.ok(map);
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return ApiResult.fail(e.toString());
        }
    }


    @AnonymousAccess
    @PostMapping("/register/verify")
    @ApiOperation(value = "验证码发送", notes = "验证码发送")
    public ApiResult<String> verify(@Validated @RequestBody VerityParam param) {
        Boolean isTest = true;
        YxUser yxUser = userService.findByName(param.getPhone());
        if (param.getType() == null) {
            param.setType("bind");
        }
        if (param.getType().equals("register") && ObjectUtil.isNotNull(yxUser)) {
            return ApiResult.fail("手机号已注册");
        }
        if (param.getType().equals("login") && ObjectUtil.isNull(yxUser)) {
            return ApiResult.fail("账号不存在");
        }
        String codeKey = "code_" + param.getPhone();
        if (ObjectUtil.isNotNull(redisUtils.get(codeKey))) {
            if (!enableSms) {
                return ApiResult.fail("10分钟内有效:" + redisUtils.get(codeKey).toString());
            }
            return ApiResult.fail("验证码10分钟内有效,请查看手机短信");

        }
        String code = RandomUtil.randomNumbers(6);

        redisUtils.set(codeKey, code, 600L);
        if (!enableSms) {
            return ApiResult.fail("测试阶段验证码:" + code);
        }
        //发送阿里云短信
        SmsResult smsResult = notifyService.notifySmsTemplateSync(param.getPhone(),
                NotifyType.CAPTCHA, new String[]{code});
        CommonResponse commonResponse = (CommonResponse) smsResult.getResult();
        if (smsResult.isSuccessful()) {
            log.info("详情：{}", commonResponse.getData());
            return ApiResult.ok("发送成功，请注意查收");
        } else {
            JSONObject jsonObject = JSON.parseObject(commonResponse.getData());
            log.info("错误详情：{}", commonResponse.getData());
            //删除redis存储
            redisUtils.del(codeKey);
            return ApiResult.ok("发送失败：" + jsonObject.getString("Message"));
        }


    }

    @AnonymousAccess
    @GetMapping("/systemlevel")
    @ApiOperation(value = "获取会员级别系统表", notes = "获取会员级别系统表")
    public ApiResult<Object> findSystemLevel() throws Exception {
        Paging<YxSystemUserLevelQueryVo> yxSystemUserLevelPageList = yxSystemUserLevelService.getYxSystemUserLevelPageList(new YxSystemUserLevelQueryParam());
        return ApiResult.ok(yxSystemUserLevelPageList.getRecords());
    }

    @AnonymousAccess
    @PostMapping("/register")
    @ApiOperation(value = "H5/APP注册新用户", notes = "H5/APP5注册新用户")
    public ApiResult<String> register(@Validated @RequestBody RegParam param) {

        Object codeObj = redisUtils.get("code_" + param.getAccount());
        if (codeObj == null) {
            return ApiResult.fail("请先获取验证码");
        }
        String code = codeObj.toString();

        if (!StrUtil.equals(code, param.getCaptcha())) {
            return ApiResult.fail("验证码错误");
        }

        YxUser yxUser = userService.findByName(param.getAccount());
        if (ObjectUtil.isNotNull(yxUser)) {
            return ApiResult.fail("用户已存在");
        }

        YxUser user = new YxUser();
        user.setAccount(param.getAccount());
        user.setUsername(param.getAccount());
        user.setPassword(passwordEncoder.encode(param.getPassword()));
        user.setPwd(passwordEncoder.encode(param.getPassword()));
        user.setPhone(param.getAccount());
        if (StrUtil.isNotBlank(param.getInviteCode())) {
            user.setUserType(AppFromEnum.APP.getValue());
        } else {
            user.setUserType(AppFromEnum.H5.getValue());
        }
        user.setAddTime(OrderUtil.getSecondTimestampTwo());
        user.setLastTime(OrderUtil.getSecondTimestampTwo());
        user.setNickname(param.getAccount());
        user.setAvatar(ShopConstants.JMSHOP_DEFAULT_AVATAR);
        user.setNowMoney(BigDecimal.ZERO);
        user.setBrokeragePrice(BigDecimal.ZERO);
        user.setIntegral(BigDecimal.ZERO);

        int level = param.getUserLevel();
        int isCheck = 0;
        int isPass = 0;
        // YxSystemUserLevel userLevel = yxSystemUserLevelService.getById(level);

        if (level == 1) {
            isCheck = 1;
            isPass = 1;
        }
        user.setLevel(0);
        user.setApplyLevel(level);
        user.setCertInfo(param.getCertInfo());
        user.setIsCheck(isCheck);
        user.setIsPass(isPass);

        userService.save(user);

        //设置推广关系
        if (StrUtil.isNotBlank(param.getInviteCode())) {
            YxSystemAttachment systemAttachment = systemAttachmentService.getByCode(param.getInviteCode());
            if (systemAttachment != null) {
                userService.setSpread(systemAttachment.getUid(),
                        user.getUid());
            }
        }

//        yxUserLevelService.setUserLevel(user.getUid(), 1);

        return ApiResult.ok("注册成功");
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ApiResult<Object> getUserInfo() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return ApiResult.ok(jwtUser);
    }


    @ApiOperation(value = "退出登录", notes = "退出登录")
    @AnonymousAccess
    @PostMapping(value = "/auth/logout")
    public ApiResult<Object> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return ApiResult.ok("退出成功");
    }
}
