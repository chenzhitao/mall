/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.entity.YxSystemStoreStaff;
import co.yixiang.modules.shop.mapper.YxSystemStoreStaffMapper;
import co.yixiang.modules.shop.service.YxSystemStoreStaffService;
import co.yixiang.modules.shop.web.param.YxSystemStoreStaffQueryParam;
import co.yixiang.modules.shop.web.vo.YxSystemStoreStaffQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxUserRecharge;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;


/**
 * <p>
 * 门店店员表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-03-23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxSystemStoreStaffServiceImpl extends BaseServiceImpl<YxSystemStoreStaffMapper, YxSystemStoreStaff> implements YxSystemStoreStaffService {

    @Autowired
    private YxSystemStoreStaffMapper yxSystemStoreStaffMapper;

    @Override
    public boolean checkStatus(int uid,int storeId) {
        YxSystemStoreStaff storeStaff = new YxSystemStoreStaff();
        storeStaff.setUid(uid);
        if(storeId > 0) {
            storeStaff.setStoreId(storeId);
        }
        return yxSystemStoreStaffMapper.selectCount(Wrappers.query(storeStaff)) > 0;
    }

    @Override
    public YxSystemStoreStaffQueryVo getYxSystemStoreStaffById(Serializable id) throws Exception{
        return yxSystemStoreStaffMapper.getYxSystemStoreStaffById(id);
    }

    @Override
    public Paging<YxSystemStoreStaffQueryVo> getYxSystemStoreStaffPageList(YxSystemStoreStaffQueryParam yxSystemStoreStaffQueryParam) throws Exception{
        Page page = setPageParam(yxSystemStoreStaffQueryParam,OrderItem.desc("create_time"));
        IPage<YxSystemStoreStaffQueryVo> iPage = yxSystemStoreStaffMapper.getYxSystemStoreStaffPageList(page,yxSystemStoreStaffQueryParam);
        return new Paging(iPage);
    }

}
