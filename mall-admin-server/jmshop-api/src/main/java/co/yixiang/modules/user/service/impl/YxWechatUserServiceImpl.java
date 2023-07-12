/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.mapper.YxWechatUserMapper;
import co.yixiang.modules.user.service.YxWechatUserService;
import co.yixiang.modules.user.web.param.YxWechatUserQueryParam;
import co.yixiang.modules.user.web.vo.YxWechatUserQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 微信用户表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxWechatUserServiceImpl extends BaseServiceImpl<YxWechatUserMapper, YxWechatUser> implements YxWechatUserService {

    private final YxWechatUserMapper yxWechatUserMapper;

    @Override
    public YxWechatUser getUserAppInfo(String openid) {
        QueryWrapper<YxWechatUser> wrapper = new QueryWrapper<>();
        wrapper.eq("routine_openid",openid).last("limit 1");
        return yxWechatUserMapper.selectOne(wrapper);
    }

    @Override
    public YxWechatUser getUserInfo(String openid) {
        QueryWrapper<YxWechatUser> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid).last("limit 1");
        return yxWechatUserMapper.selectOne(wrapper);
    }

    @Override
    public YxWechatUserQueryVo getYxWechatUserById(Serializable id){
        return yxWechatUserMapper.getYxWechatUserById(id);
    }

    @Override
    public Paging<YxWechatUserQueryVo> getYxWechatUserPageList(YxWechatUserQueryParam yxWechatUserQueryParam) throws Exception{
        Page page = setPageParam(yxWechatUserQueryParam,OrderItem.desc("create_time"));
        IPage<YxWechatUserQueryVo> iPage = yxWechatUserMapper.getYxWechatUserPageList(page,yxWechatUserQueryParam);
        return new Paging(iPage);
    }

}
