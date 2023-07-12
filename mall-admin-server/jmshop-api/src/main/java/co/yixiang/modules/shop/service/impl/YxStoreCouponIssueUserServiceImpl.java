/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.entity.YxStoreCouponIssueUser;
import co.yixiang.modules.shop.mapper.YxStoreCouponIssueUserMapper;
import co.yixiang.modules.shop.service.YxStoreCouponIssueUserService;
import co.yixiang.modules.shop.web.param.YxStoreCouponIssueUserQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueUserQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.utils.OrderUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;


/**
 * <p>
 * 优惠券前台用户领取记录表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreCouponIssueUserServiceImpl extends BaseServiceImpl<YxStoreCouponIssueUserMapper, YxStoreCouponIssueUser> implements YxStoreCouponIssueUserService {

    private final YxStoreCouponIssueUserMapper yxStoreCouponIssueUserMapper;


    @Override
    public void addUserIssue(int uid, int id) {
        YxStoreCouponIssueUser couponIssueUser = new YxStoreCouponIssueUser();
        couponIssueUser.setAddTime(OrderUtil.getSecondTimestampTwo());
        couponIssueUser.setIssueCouponId(id);
        couponIssueUser.setUid(uid);
        save(couponIssueUser);
    }

    @Override
    public YxStoreCouponIssueUserQueryVo getYxStoreCouponIssueUserById(Serializable id) throws Exception{
        return yxStoreCouponIssueUserMapper.getYxStoreCouponIssueUserById(id);
    }

    @Override
    public Paging<YxStoreCouponIssueUserQueryVo> getYxStoreCouponIssueUserPageList(YxStoreCouponIssueUserQueryParam yxStoreCouponIssueUserQueryParam) throws Exception{
        Page page = setPageParam(yxStoreCouponIssueUserQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreCouponIssueUserQueryVo> iPage = yxStoreCouponIssueUserMapper.getYxStoreCouponIssueUserPageList(page,yxStoreCouponIssueUserQueryParam);
        return new Paging(iPage);
    }

}
