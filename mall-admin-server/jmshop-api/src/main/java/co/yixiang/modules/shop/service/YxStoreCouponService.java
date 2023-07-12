/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.YxStoreCoupon;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.web.param.YxStoreCouponQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
public interface YxStoreCouponService extends BaseService<YxStoreCoupon> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponQueryVo getYxStoreCouponById(Serializable id);

    /**
     * 获取分页对象
     * @param yxStoreCouponQueryParam
     * @return
     */
    Paging<YxStoreCouponQueryVo> getYxStoreCouponPageList(YxStoreCouponQueryParam yxStoreCouponQueryParam) throws Exception;

}
