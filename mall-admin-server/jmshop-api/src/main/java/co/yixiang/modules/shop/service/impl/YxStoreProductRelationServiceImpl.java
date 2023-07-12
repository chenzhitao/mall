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
import co.yixiang.modules.shop.entity.YxStoreProductRelation;
import co.yixiang.modules.shop.mapper.YxStoreProductRelationMapper;
import co.yixiang.modules.shop.service.YxStoreProductRelationService;
import co.yixiang.modules.shop.web.param.YxStoreProductRelationQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductRelationQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 商品点赞和收藏表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreProductRelationServiceImpl extends BaseServiceImpl<YxStoreProductRelationMapper, YxStoreProductRelation> implements YxStoreProductRelationService {

    private final YxStoreProductRelationMapper yxStoreProductRelationMapper;

    @Override
    public YxStoreProductRelationQueryVo getYxStoreProductRelationById(Serializable id) throws Exception{
        return yxStoreProductRelationMapper.getYxStoreProductRelationById(id);
    }

    @Override
    public Paging<YxStoreProductRelationQueryVo> getYxStoreProductRelationPageList(YxStoreProductRelationQueryParam yxStoreProductRelationQueryParam) throws Exception{
        Page page = setPageParam(yxStoreProductRelationQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreProductRelationQueryVo> iPage = yxStoreProductRelationMapper.getYxStoreProductRelationPageList(page,yxStoreProductRelationQueryParam);
        return new Paging(iPage);
    }

    @Override
    public List<YxStoreProductRelationQueryVo> userCollectProduct(int page, int limit, int uid) {
        Page<YxStoreProductRelation> pageModel = new Page<>(page, limit);
        List<YxStoreProductRelationQueryVo> list = baseMapper.selectList(pageModel,uid);
        return list;
    }

    @Override
    public void addRroductRelation(YxStoreProductRelationQueryParam param
            ,int uid,String relationType) {
        YxStoreProductRelation storeProductRelation = new YxStoreProductRelation();
        if(isProductRelation(param.getId(),param.getCategory(),uid,relationType)) {
            throw new ErrorRequestException("已收藏");
        }

        storeProductRelation.setCategory(param.getCategory());
        storeProductRelation.setProductId(param.getId());
        storeProductRelation.setType(relationType);
        storeProductRelation.setUid(uid);
        storeProductRelation.setAddTime(OrderUtil.getSecondTimestampTwo());

        yxStoreProductRelationMapper.insert(storeProductRelation);
    }

    @Override
    public void delRroductRelation(YxStoreProductRelationQueryParam param, int uid, String relationType) {
        QueryWrapper<YxStoreProductRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).eq("product_id",param.getId())
                .eq("type",relationType).eq("category",param.getCategory());
        YxStoreProductRelation productRelation = yxStoreProductRelationMapper
                .selectOne(wrapper);
        if(ObjectUtil.isNull(productRelation)) {
            throw new ErrorRequestException("已取消");
        }

        yxStoreProductRelationMapper.deleteById(productRelation.getId());
    }

    @Override
    public Boolean isProductRelation(int productId, String category,
                                     int uid, String relationType) {
        QueryWrapper<YxStoreProductRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).eq("product_id",productId)
                .eq("type",relationType).eq("category",category);
        YxStoreProductRelation productRelation = yxStoreProductRelationMapper
                .selectOne(wrapper);
        if(ObjectUtil.isNotNull(productRelation)) {
            return true;
        }

        return false;
    }
}
