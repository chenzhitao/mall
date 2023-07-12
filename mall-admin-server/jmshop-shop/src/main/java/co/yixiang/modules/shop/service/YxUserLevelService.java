/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service;

import java.io.Serializable;

/**
 * <p>
 * 用户等级记录表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
public interface YxUserLevelService {

    void setUserLevel(Integer uid, Integer levelId);

    void autoNoticeExpireUser();
}
