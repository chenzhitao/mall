/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.YxStoreCouponIssue;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.web.param.YxStoreCouponIssueQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 优惠券前台领取表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
public interface YxStoreCouponIssueService extends BaseService<YxStoreCouponIssue> {

    int couponCount(int id,int uid);

    void issueUserCoupon(int id,int uid);

    List<YxStoreCouponIssueQueryVo> getCouponList(int page, int limit, int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponIssueQueryVo getYxStoreCouponIssueById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreCouponIssueQueryParam
     * @return
     */
    Paging<YxStoreCouponIssueQueryVo> getYxStoreCouponIssuePageList(YxStoreCouponIssueQueryParam yxStoreCouponIssueQueryParam) throws Exception;

}
