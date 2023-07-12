package co.yixiang.constant;

/**
 * 商城统一常量
 * @author hupeng
 * @since 2020-02-27
 */
public interface ShopConstants {

	/**
	 * 订单自动取消时间（分钟）
	 */
	long ORDER_OUTTIME_UNPAY = 2880;
	/**
	 * 订单自动收货时间（天）
	 */
	long ORDER_OUTTIME_UNCONFIRM = 7;
	/**
	 * redis订单未付款key
	 */
	String REDIS_ORDER_OUTTIME_UNPAY = "order:unpay:";
	/**
	 * redis订单收货key
	 */
	String REDIS_ORDER_OUTTIME_UNCONFIRM = "order:unconfirm:";

	/**
	 * redis拼团key
	 */
	String REDIS_PINK_CANCEL_KEY = "pink:cancel:";

	/**
	 * 零标识
	 */
	String PUSHMALL_ZERO =  "0";

	/**
	 * 一标识
	 */
	String PUSHMALL_ONE =  "1";

	int JMSHOP_ONE_NUM = 1;

	/**
	 * 微信支付service
	 */
	String JMSHOP_WEIXIN_PAY_SERVICE = "jmshop_weixin_pay_service";

	/**
	 * 微信支付小程序service
	 */
	String JMSHOP_WEIXIN_MINI_PAY_SERVICE = "jmshop_weixin_mini_pay_service";

	/**
	 * 微信支付app service
	 */
	String JMSHOP_WEIXIN_APP_PAY_SERVICE = "jmshop_weixin_app_pay_service";

	/**
	 * 微信公众号service
	 */
	String JMSHOP_WEIXIN_MP_SERVICE = "jmshop_weixin_mp_service";

	/**
	 * 建行支付service
	 */
	String JMSHOP_CCB_MP_SERVICE = "jmshop_ccb_mp_service";

	//零标识
	String JMSHOP_ZERO =  "0";

	//业务标识标识
	String JMSHOP_ONE =  "1";

	/**
	 * 商城默认密码
	 */
	String JMSHOP_DEFAULT_PWD = "123456";

	/**
	 * 商城默认注册图片
	 */
	String JMSHOP_DEFAULT_AVATAR = "https://pic.aoyangsp.com/file/pic/gh_0c2b15942f10_1280(1)-20201008010432189.jpg";

	/**
	 * 腾讯地图地址解析
	 */
	String QQ_MAP_URL = "https://apis.map.qq.com/ws/geocoder/v1/";

	/**
	 * redis首页键
	 */
	String JMSHOP_REDIS_INDEX_KEY = "jmshop:index_data";

	/**
	 * 充值方案
	 */
	String JMSHOP_RECHARGE_PRICE_WAYS = "jmshop_recharge_price_ways";
	/**
	 * 首页banner
	 */
	String JMSHOP_HOME_BANNER = "jmshop_home_banner";
	/**
	 * 首页菜单
	 */
	String JMSHOP_HOME_MENUS = "jmshop_home_menus";
	String JMSHOP_ADVERTISING_MENUS = "jmshop_advertising_menus";
	/**
	 * 首页滚动新闻
	 */
	String JMSHOP_HOME_ROLL_NEWS = "jmshop_home_roll_news";
	/**
	 * 热门搜索
	 */
	String JMSHOP_HOT_SEARCH = "jmshop_hot_search";
	/**
	 * 个人中心菜单
	 */
	String JMSHOP_MY_MENUES = "jmshop_my_menus";
	/**
	 * 秒杀时间段
	 */
	String JMSHOP_SECKILL_TIME = "jmshop_seckill_time";
	/**
	 * 签到天数
	 */
	String JMSHOP_SIGN_DAY_NUM = "jmshop_sign_day_num";

	/**
	 * 微信消息推送
	 */
	String JMSHOP_WECHAT_PUSH_REMARK = "PUSHmall为您服务！";

}
