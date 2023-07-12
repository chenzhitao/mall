/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.mapper.YxStoreProductAttrValueMapper;
import co.yixiang.modules.shop.service.YxStoreProductAttrValueService;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrValueQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrValueQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
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
 * 商品属性值表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreProductAttrValueServiceImpl extends BaseServiceImpl<YxStoreProductAttrValueMapper, YxStoreProductAttrValue> implements YxStoreProductAttrValueService {

    private final YxStoreProductAttrValueMapper yxStoreProductAttrValueMapper;

    @Override
    public YxStoreProductAttrValueQueryVo getYxStoreProductAttrValueById(Serializable id) throws Exception{
        return yxStoreProductAttrValueMapper.getYxStoreProductAttrValueById(id);
    }

    @Override
    public Paging<YxStoreProductAttrValueQueryVo> getYxStoreProductAttrValuePageList(YxStoreProductAttrValueQueryParam yxStoreProductAttrValueQueryParam) throws Exception{
        Page page = setPageParam(yxStoreProductAttrValueQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreProductAttrValueQueryVo> iPage = yxStoreProductAttrValueMapper.getYxStoreProductAttrValuePageList(page,yxStoreProductAttrValueQueryParam);
        return new Paging(iPage);
    }

}
