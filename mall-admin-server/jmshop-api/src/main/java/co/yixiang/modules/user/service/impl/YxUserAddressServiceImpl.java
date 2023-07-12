/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxUserAddress;
import co.yixiang.modules.user.mapper.YxUserAddressMapper;
import co.yixiang.modules.user.service.YxUserAddressService;
import co.yixiang.modules.user.web.param.YxUserAddressQueryParam;
import co.yixiang.modules.user.web.vo.YxUserAddressQueryVo;
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
 * 用户地址表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-28
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxUserAddressServiceImpl extends BaseServiceImpl<YxUserAddressMapper, YxUserAddress> implements YxUserAddressService {

    private final YxUserAddressMapper yxUserAddressMapper;

    @Override
    public YxUserAddressQueryVo getYxUserAddressById(Serializable id){
        return yxUserAddressMapper.getYxUserAddressById(id);
    }

    @Override
    public YxUserAddress getUserDefaultAddress(int uid) {
        QueryWrapper<YxUserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("is_default",1).eq("uid",uid).eq("is_del",0).last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public Paging<YxUserAddressQueryVo> getYxUserAddressPageList(YxUserAddressQueryParam yxUserAddressQueryParam){
        Page page = setPageParam(yxUserAddressQueryParam,OrderItem.desc("add_time"));
        IPage<YxUserAddressQueryVo> iPage = yxUserAddressMapper.getYxUserAddressPageList(page,yxUserAddressQueryParam);
        return new Paging(iPage);
    }

}
