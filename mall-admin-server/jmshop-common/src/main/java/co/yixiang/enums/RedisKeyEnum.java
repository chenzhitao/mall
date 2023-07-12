package co.yixiang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * rediskey 相关枚举
 */
@Getter
@AllArgsConstructor
public enum RedisKeyEnum {

    WXAPP_APPID("wxapp_appId","微信小程序id"),
    WXAPP_SECRET("wxapp_secret","微信小程序秘钥"),
    WX_NATIVE_APP_APPID("wx_native_app_appId","支付appId"),
    WXPAY_MCHID("wxpay_mchId","商户号"),
    WXPAY_MCHKEY("wxpay_mchKey","商户秘钥"),
    WXPAY_KEYPATH("wxpay_keyPath","商户证书路径"),
    WECHAT_APPID("wechat_appid","微信公众号id"),
    WECHAT_APPSECRET("wechat_appsecret","微信公众号secret"),
    WECHAT_TOKEN("wechat_token","微信公众号验证token"),
    WECHAT_ENCODINGAESKEY("wechat_encodingaeskey","EncodingAESKey"),
    CCB_URL("ccb_URL","建行支付URL"),
    CCB_MERCHANTID("ccb_merchantId","建行支付商户代码"),
    CCB_POSID("ccb_postId","建行支付商户柜台代码"),
    CCB_BRANCHID("ccb_bankchId","建行支付分行代码"),
    TENGXUN_MAP_KEY("tengxun_map_key","腾讯mapkey"),
    API_URL("api_url","支付回调")

    ;
    private String value;
    private String desc;
}
