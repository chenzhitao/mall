package co.yixiang.mp.config;

import cn.hutool.core.util.StrUtil;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.mp.handler.*;
import co.yixiang.utils.RedisUtil;
import com.google.common.collect.Maps;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * 公众号配置
 * @author hupeng
 * @date 2020/01/20
 */
@Configuration
public class WxMpConfiguration {

    private static Map<String, WxMpService> mpServices = Maps.newHashMap();
    private static Map<String, WxMpMessageRouter> routers = Maps.newHashMap();

    private static LogHandler logHandler;
    private static NullHandler nullHandler;
    private static KfSessionHandler kfSessionHandler;
    private static StoreCheckNotifyHandler storeCheckNotifyHandler;
    private static LocationHandler locationHandler;
    private static MenuHandler menuHandler;
    private static MsgHandler msgHandler;
    private static UnsubscribeHandler unsubscribeHandler;
    private static SubscribeHandler subscribeHandler;
    private static ScanHandler scanHandler;
    private static RedisHandler redisHandler;

    public WxMpConfiguration(LogHandler logHandler,NullHandler nullHandler,KfSessionHandler kfSessionHandler,
                             StoreCheckNotifyHandler storeCheckNotifyHandler,LocationHandler locationHandler,
                             MenuHandler menuHandler,MsgHandler msgHandler,UnsubscribeHandler unsubscribeHandler,
                             SubscribeHandler subscribeHandler,ScanHandler scanHandler,
                             RedisHandler redisHandler){
        WxMpConfiguration.logHandler = logHandler;
        WxMpConfiguration.nullHandler = nullHandler;
        WxMpConfiguration.kfSessionHandler = kfSessionHandler;
        WxMpConfiguration.storeCheckNotifyHandler = storeCheckNotifyHandler;
        WxMpConfiguration.locationHandler = locationHandler;
        WxMpConfiguration.menuHandler = menuHandler;
        WxMpConfiguration.msgHandler = msgHandler;
        WxMpConfiguration.unsubscribeHandler = unsubscribeHandler;
        WxMpConfiguration.subscribeHandler = subscribeHandler;
        WxMpConfiguration.scanHandler = scanHandler;
        WxMpConfiguration.redisHandler = redisHandler;
    }


    /**
     * 获取WxMpService
     * @return
     */
    public static WxMpService getWxMpService() {

        WxMpService wxMpService = mpServices.get(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE);
        //增加一个redis标识
        if(wxMpService == null || RedisUtil.get(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE) == null) {
            WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
            configStorage.setAppId(RedisUtil.get(RedisKeyEnum.WECHAT_APPID.getValue()));
            configStorage.setSecret(RedisUtil.get(RedisKeyEnum.WECHAT_APPSECRET.getValue()));
            configStorage.setToken(RedisUtil.get(RedisKeyEnum.WECHAT_TOKEN.getValue()));
            configStorage.setAesKey(RedisUtil.get(RedisKeyEnum.WECHAT_ENCODINGAESKEY.getValue()));
            wxMpService = new WxMpServiceImpl();
            wxMpService.setWxMpConfigStorage(configStorage);
            mpServices.put(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE, wxMpService);
            routers.put(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE, newRouter(wxMpService));

            //增加标识
            RedisUtil.set(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE,"jmshop");
        }
        return wxMpService;
    }

    /**
     * 移除WxMpService
     */
    public static void removeWxMpService(){
        RedisUtil.del(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE);
        mpServices.remove(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE);
        routers.remove(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE);
    }

    /**
     *  获取WxMpMessageRouter
     */
    public static WxMpMessageRouter getWxMpMessageRouter() {
        WxMpMessageRouter wxMpMessageRouter = routers.get(ShopConstants.JMSHOP_WEIXIN_MP_SERVICE);
        return wxMpMessageRouter;
    }

    private static WxMpMessageRouter newRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
                .handler(kfSessionHandler).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
                .handler(kfSessionHandler)
                .end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
                .handler(kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.POI_CHECK_NOTIFY)
                .handler(storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.CLICK).handler(menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxConsts.MenuButtonType.VIEW).handler(menuHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.SCANCODE_WAITMSG).handler(menuHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.SUBSCRIBE).handler(subscribeHandler)
                .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.UNSUBSCRIBE)
                .handler(unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.LOCATION).handler(locationHandler)
                .end();


        // 默认
        newRouter.rule().async(false).handler(msgHandler).end();

        return newRouter;
    }

}
