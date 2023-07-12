/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.shop.entity.YxStoreCouponIssue;
import co.yixiang.modules.shop.entity.YxStoreCouponIssueUser;
import co.yixiang.modules.shop.mapper.YxStoreCouponIssueMapper;
import co.yixiang.modules.shop.mapper.YxStoreCouponIssueUserMapper;
import co.yixiang.modules.shop.service.YxStoreCouponIssueService;
import co.yixiang.modules.shop.service.YxStoreCouponIssueUserService;
import co.yixiang.modules.shop.service.YxStoreCouponUserService;
import co.yixiang.modules.shop.web.param.YxStoreCouponIssueQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 优惠券前台领取表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreCouponIssueServiceImpl extends BaseServiceImpl<YxStoreCouponIssueMapper, YxStoreCouponIssue> implements YxStoreCouponIssueService {

    @Autowired
    private YxStoreCouponIssueMapper yxStoreCouponIssueMapper;
    @Autowired
    private YxStoreCouponIssueUserMapper storeCouponIssueUserMapper;
    @Autowired
    private YxStoreCouponUserService storeCouponUserService;
    @Autowired
    private YxStoreCouponIssueUserService storeCouponIssueUserService;

    /**
     * 领取优惠券
     *
     * @param id  id
     * @param uid uid
     */
    @Override
    public void issueUserCoupon(int id, int uid) {
        YxStoreCouponIssueQueryVo couponIssueQueryVo = yxStoreCouponIssueMapper
                .selectOne(id);
        if (ObjectUtil.isNull(couponIssueQueryVo)) {
            throw new ErrorRequestException("领取的优惠劵已领完或已过期");
        }

        int count = couponCount(id, uid);
        if (count > 0) {
            //限量限制
            if (couponIssueQueryVo.getIsPermanent() == null || couponIssueQueryVo.getIsPermanent() == 0) {
                throw new ErrorRequestException("已领取过该优惠劵");
            }
        }

        if (couponIssueQueryVo.getRemainCount() <= 0) {
            throw new ErrorRequestException("抱歉优惠卷已经领取完了");
        }

        storeCouponUserService.addUserCoupon(uid, couponIssueQueryVo);

        storeCouponIssueUserService.addUserIssue(uid, id);

        if (couponIssueQueryVo.getTotalCount() > 0) {
            yxStoreCouponIssueMapper.decCount(id);
        }

    }

    @Override
    public int couponCount(int id, int uid) {
        QueryWrapper<YxStoreCouponIssueUser> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("issue_coupon_id", id);
        int count = storeCouponIssueUserMapper.selectCount(wrapper);
        return count;
    }

    /**
     * 优惠券列表
     *
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public List<YxStoreCouponIssueQueryVo> getCouponList(int page, int limit, int uid) {
        Page<YxStoreCouponIssue> pageModel = new Page<>(page, limit);
        List<YxStoreCouponIssueQueryVo> list = yxStoreCouponIssueMapper
                .selectList(pageModel);
        for (YxStoreCouponIssueQueryVo couponIssue : list) {
            int count = couponCount(couponIssue.getId(), uid);
            if (count > 0) {
                couponIssue.setIsUse(true);
            } else {
                couponIssue.setIsUse(false);
            }
        }
        return list;
    }

    @Override
    public YxStoreCouponIssueQueryVo getYxStoreCouponIssueById(Serializable id) throws Exception {
        return yxStoreCouponIssueMapper.getYxStoreCouponIssueById(id);
    }

    @Override
    public Paging<YxStoreCouponIssueQueryVo> getYxStoreCouponIssuePageList(YxStoreCouponIssueQueryParam yxStoreCouponIssueQueryParam) throws Exception {
        Page page = setPageParam(yxStoreCouponIssueQueryParam, OrderItem.desc("create_time"));
        IPage<YxStoreCouponIssueQueryVo> iPage = yxStoreCouponIssueMapper.getYxStoreCouponIssuePageList(page, yxStoreCouponIssueQueryParam);
        return new Paging(iPage);
    }

}
