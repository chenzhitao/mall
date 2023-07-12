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
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.CommonEnum;
import co.yixiang.enums.ProductEnum;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.enums.ShopCommonEnum;
import co.yixiang.enums.SortEnum;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.mapper.YxStoreCategoryMapper;
import co.yixiang.modules.shop.mapper.YxStoreProductAttrValueMapper;
import co.yixiang.modules.shop.mapper.YxStoreProductMapper;
import co.yixiang.modules.shop.mapping.YxStoreProductMap;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.web.dto.ProductDTO;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCategoryQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import co.yixiang.modules.shop.web.vo.YxSystemStoreQueryVo;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxSystemLevelProduct;
import co.yixiang.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiniu.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings("unchecked")
public class YxStoreProductServiceImpl extends BaseServiceImpl<YxStoreProductMapper, YxStoreProduct> implements YxStoreProductService {

    @Autowired
    private YxStoreProductMapper yxStoreProductMapper;
    @Autowired
    private YxStoreProductAttrValueMapper storeProductAttrValueMapper;

    @Autowired
    private YxStoreProductAttrService storeProductAttrService;
    @Autowired
    private YxStoreProductRelationService relationService;
    @Autowired
    private YxStoreProductReplyService replyService;
    @Autowired
    private YxUserService userService;
    @Autowired
    private YxSystemStoreService systemStoreService;

    @Autowired
    private YxStoreProductMap storeProductMap;

    @Autowired
    private YxStoreCategoryMapper yxStoreCategoryMapper;


    /**
     * 增加库存 减少销量
     *
     * @param num
     * @param productId
     * @param unique
     */
    @Override
    public void incProductStock(int num, int productId, String unique) {
        if (StrUtil.isNotEmpty(unique)) {
            storeProductAttrService.incProductAttrStock(num, productId, unique);
            yxStoreProductMapper.decSales(num, productId);
        } else {
            yxStoreProductMapper.incStockDecSales(num, productId);
        }
    }

    /**
     * 库存与销量
     *
     * @param num
     * @param productId
     * @param unique
     */
    @Override
    public void decProductStock(int num, int productId, String unique) {
        if (StrUtil.isNotEmpty(unique)) {
            storeProductAttrService.decProductAttrStock(num, productId, unique);
            yxStoreProductMapper.incSales(num, productId);
        } else {
            yxStoreProductMapper.decStockIncSales(num, productId);
        }
    }

    /**
     * 返回商品库存
     *
     * @param productId
     * @param unique
     * @return
     */
    @Override
    public int getProductStock(int productId, String unique) {
        if (StrUtil.isEmpty(unique)) {
            return getYxStoreProductById(productId).getStock();
        } else {
            return storeProductAttrService.uniqueByStock(unique);
        }

    }

    @Override
    public YxStoreProduct getProductInfo(int id) {
        LambdaQueryWrapper<YxStoreProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YxStoreProduct::getIsDel, CommonEnum.DEL_STATUS_0.getValue()).eq(YxStoreProduct::getIsShow, CommonEnum.SHOW_STATUS_1.getValue())
                .eq(YxStoreProduct::getId, id);
        YxStoreProduct storeProduct = yxStoreProductMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(storeProduct)) {
            throw new ErrorRequestException("商品不存在或已下架");
        }

        return storeProduct;
    }

    /**
     * 获取商品详情
     *
     * @param id        商品ID
     * @param type      无用
     * @param uid       设置VIP价格
     * @param latitude  经度-无用
     * @param longitude 纬度-无用
     * @return
     */
    @Override
    public ProductDTO goodsDetail(int id, int type, int uid, String latitude, String longitude) {
        LambdaQueryWrapper<YxStoreProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YxStoreProduct::getIsDel, CommonEnum.DEL_STATUS_0.getValue()).eq(YxStoreProduct::getIsShow, CommonEnum.SHOW_STATUS_1.getValue())
                .eq(YxStoreProduct::getId, id);
        YxStoreProduct storeProduct = yxStoreProductMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(storeProduct)) {
            throw new ErrorRequestException("商品不存在或已下架");
        }
        Map<String, Object> returnMap = storeProductAttrService.getProductAttrDetail(id, 0, 0);
        ProductDTO productDTO = new ProductDTO();
        YxStoreProductQueryVo storeProductQueryVo = storeProductMap.toDto(storeProduct);

        //处理库存
        Integer newStock = storeProductAttrValueMapper.sumStock(id);
        if (newStock != null) {
            storeProductQueryVo.setStock(newStock);
        }

        //设置销量
        storeProductQueryVo.setSales(storeProductQueryVo.getSales() + storeProductQueryVo.getFicti());

        //设置商品最低价格
        BigDecimal lowestPrice = storeProductAttrValueMapper.lowestPrice(id);
        if (lowestPrice != null) {
            storeProductQueryVo.setPrice(lowestPrice);
        }

        BigDecimal maxOtPrice = storeProductAttrValueMapper.maxOtPrice(id);
        if (maxOtPrice != null) {
            storeProductQueryVo.setOtPrice(maxOtPrice);
        }

        //设置商品最高价格
        BigDecimal maxPrice = storeProductAttrValueMapper.maxPrice(id);
        if (maxPrice != null) {
            storeProductQueryVo.setMaxPrice(maxPrice);
        }

//        //设置VIP价格
//        double vipPrice = userService.setLevelPrice(
//                storeProductQueryVo.getPrice().doubleValue(), uid, storeProductQueryVo.getId());
//        storeProductQueryVo.setVipPrice(BigDecimal.valueOf(vipPrice));

        //获取最优VIP价格-V2调整
        YxSystemLevelProduct yxSystemLevelProduct = userService.setYxSystemLevelProduct(storeProductQueryVo.getPrice().doubleValue());
        productDTO.setYxSystemLevelProduct(yxSystemLevelProduct);

        storeProductQueryVo.setUserCollect(relationService.isProductRelation(id, "product", uid, "collect"));

        productDTO.setProductAttr((List<YxStoreProductAttrQueryVo>) returnMap.get("productAttr"));
        productDTO.setProductValue((Map<String, YxStoreProductAttrValue>) returnMap.get("productValue"));

        productDTO.setStoreInfo(storeProductQueryVo);

        productDTO.setReply(replyService.getReply(id));
        int replyCount = replyService.productReplyCount(id);
        productDTO.setReplyCount(replyCount);
        //百分比
        productDTO.setReplyChance(replyService.doReply(id, replyCount));
        //门店-去除门店
//        productDTO.setSystemStore(systemStoreService.getStoreInfo(latitude, longitude));
        productDTO.setSystemStore(new YxSystemStoreQueryVo());
        //去除Key
        productDTO.setMapKey(RedisUtil.get(RedisKeyEnum.TENGXUN_MAP_KEY.getValue()));
        //邮费
        productDTO.setTempName(storeProductQueryVo.getPostage() + "");

        return productDTO;
    }

    /**
     * 商品列表
     *
     * @return
     */
    @Override
    public List<YxStoreProductQueryVo> getGoodsList(YxStoreProductQueryParam productQueryParam) {

        LambdaQueryWrapper<YxStoreProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YxStoreProduct::getIsDel, CommonEnum.DEL_STATUS_0.getValue()).eq(YxStoreProduct::getIsShow, CommonEnum.SHOW_STATUS_1.getValue());

        //多字段模糊查询分类搜索
        if (StrUtil.isNotBlank(productQueryParam.getSid()) &&
                !ShopConstants.PUSHMALL_ZERO.equals(productQueryParam.getSid())) {
            wrapper.eq(YxStoreProduct::getCateId, productQueryParam.getSid());
        }

        //关键字搜索
        if (StrUtil.isNotEmpty(productQueryParam.getKeyword())) {
            wrapper.and(wrapper1 -> {
                wrapper1.or();
                wrapper1.like(YxStoreProduct::getStoreName, productQueryParam.getKeyword());
                wrapper1.or();
                wrapper1.like(YxStoreProduct::getStoreInfo, productQueryParam.getKeyword());
                wrapper1.or();
                wrapper1.like(YxStoreProduct::getKeyword, productQueryParam.getKeyword());
            });
        }

        //新品搜索
        if (StrUtil.isNotBlank(productQueryParam.getNews()) &&
                !ShopConstants.PUSHMALL_ZERO.equals(productQueryParam.getNews())) {
            wrapper.eq(YxStoreProduct::getIsNew, ShopCommonEnum.IS_NEW_1.getValue());
        }

        //销量排序
        if (SortEnum.DESC.getValue().equals(productQueryParam.getSalesOrder())) {
            wrapper.orderByDesc(YxStoreProduct::getSales);
        } else if (SortEnum.ASC.getValue().equals(productQueryParam.getSalesOrder())) {
            wrapper.orderByAsc(YxStoreProduct::getSales);
        }

        //价格排序
        if (SortEnum.DESC.getValue().equals(productQueryParam.getPriceOrder())) {
            wrapper.orderByDesc(YxStoreProduct::getPrice);
        } else if (SortEnum.ASC.getValue().equals(productQueryParam.getPriceOrder())) {
            wrapper.orderByAsc(YxStoreProduct::getPrice);
        }


        wrapper.orderByDesc(YxStoreProduct::getSort);

        //无其他排序条件时,防止因为商品排序导致商品重复
        if (StringUtils.isNullOrEmpty(productQueryParam.getPriceOrder()) && StringUtils.isNullOrEmpty(productQueryParam.getSalesOrder())) {
            wrapper.orderByDesc(YxStoreProduct::getId);
        }

        Page<YxStoreProduct> pageModel = new Page<>(productQueryParam.getPage(),
                productQueryParam.getLimit());

        IPage<YxStoreProduct> pageList = yxStoreProductMapper.selectPage(pageModel, wrapper);

        List<YxStoreProductQueryVo> list = storeProductMap.toDto(pageList.getRecords());

        //处理虚拟销量和规格处理
        for (YxStoreProductQueryVo vo : list) {
            vo.setSales(vo.getSales() + vo.getFicti());
            Map<String, Object> returnMap = storeProductAttrService.getProductAttrDetail(vo.getId(), 0, 0);
            vo.setProductAttr((List<YxStoreProductAttrQueryVo>) returnMap.get("productAttr"));
            vo.setProductValue((Map<String, YxStoreProductAttrValue>) returnMap.get("productValue"));

            BigDecimal maxOtPrice = storeProductAttrValueMapper.maxOtPrice(vo.getId());
            if (maxOtPrice != null) {
                vo.setOtPrice(maxOtPrice);
            }
        }

        return list;
    }

    /**
     * 商品列表
     *
     * @param page
     * @param limit
     * @param order
     * @return
     */
    @Override
    public List<YxStoreProductQueryVo> getList(int page, int limit, int order) {

        LambdaQueryWrapper<YxStoreProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YxStoreProduct::getIsShow, ShopCommonEnum.SHOW_1.getValue()).eq(YxStoreProduct::getIsDel, ShopCommonEnum.DELETE_0.getValue())
                .orderByDesc(YxStoreProduct::getSort);

        // order
        switch (ProductEnum.toType(order)) {
            //精品推荐
            case TYPE_1:
                wrapper.eq(YxStoreProduct::getIsBest,
                        ShopCommonEnum.IS_STATUS_1.getValue());
                break;
            //首发新品
            case TYPE_3:
                wrapper.eq(YxStoreProduct::getIsNew,
                        ShopCommonEnum.IS_STATUS_1.getValue());
                break;
            // 猜你喜欢
            case TYPE_4:
                wrapper.eq(YxStoreProduct::getIsBenefit,
                        ShopCommonEnum.IS_STATUS_1.getValue());
                break;
            // 热门榜单
            case TYPE_2:
                wrapper.eq(YxStoreProduct::getIsHot,
                        ShopCommonEnum.IS_STATUS_1.getValue());
                break;
        }
        Page<YxStoreProduct> pageModel = new Page<>(page, limit);

        IPage<YxStoreProduct> pageList = yxStoreProductMapper.selectPage(pageModel, wrapper);

        List<YxStoreProductQueryVo> list = storeProductMap.toDto(pageList.getRecords());

        for (YxStoreProductQueryVo vo : list) {
            vo.setSales(vo.getSales() + vo.getFicti());
            BigDecimal lowestPrice = storeProductAttrValueMapper.lowestPrice(vo.getId());
            BigDecimal maxtPrice = storeProductAttrValueMapper.maxPrice(vo.getId());
            BigDecimal maxtOtPrice = storeProductAttrValueMapper.maxOtPrice(vo.getId());
            Map<String, Object> returnMap = storeProductAttrService.getProductAttrDetail(vo.getId(), 0, 0);
            vo.setProductAttr((List<YxStoreProductAttrQueryVo>) returnMap.get("productAttr"));
            vo.setProductValue((Map<String, YxStoreProductAttrValue>) returnMap.get("productValue"));
            if (lowestPrice != null) {
                vo.setPrice(lowestPrice);
            }
            if (maxtPrice != null) {
                vo.setMaxPrice(maxtPrice);
            }
            if (maxtOtPrice != null) {
                vo.setOtPrice(maxtOtPrice);
            }
            if(vo.getIsRank()!=null && vo.getIsRank().intValue()==1){
                YxStoreCategoryQueryVo category= yxStoreCategoryMapper.getYxStoreCategoryById(vo.getCateId());
                YxStoreProductQueryParam queryParam=new YxStoreProductQueryParam();
                queryParam.setCateId(vo.getCateId());
                List<YxStoreProductQueryVo> plist= getCategoryGoodsList(queryParam);
                plist.sort((o1, o2) -> o1.getSalesNum().compareTo(o2.getSalesNum()));
                YxStoreProductQueryVo checkVo= plist.stream().filter(pro->pro.getId().intValue()==vo.getId().intValue()).findFirst().orElse(null);
                int index=plist.indexOf(checkVo);
                if(index>0){
                    vo.setRankInfo(category.getCateName()+"品类榜 第"+(index+1)+"名");
                }
            }
        }

        return list;
    }

    @Override
    public YxStoreProductQueryVo getYxStoreProductById(Serializable id) {
        return yxStoreProductMapper.getYxStoreProductById(id);
    }

    @Override
    public YxStoreProductQueryVo getNewStoreProductById(int id) {
        return storeProductMap.toDto(yxStoreProductMapper.selectById(id));
    }

    @Override
    public Paging<YxStoreProductQueryVo> getYxStoreProductPageList(YxStoreProductQueryParam yxStoreProductQueryParam) throws Exception {
        Page page = setPageParam(yxStoreProductQueryParam, OrderItem.desc("create_time"));
        IPage<YxStoreProductQueryVo> iPage = yxStoreProductMapper.getYxStoreProductPageList(page, yxStoreProductQueryParam);
        return new Paging(iPage);
    }

    @Override
    public List<YxStoreProductQueryVo> getCategoryGoodsList(YxStoreProductQueryParam productQueryParam) {
        LambdaQueryWrapper<YxStoreProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YxStoreProduct::getIsDel, CommonEnum.DEL_STATUS_0.getValue()).eq(YxStoreProduct::getIsShow, CommonEnum.SHOW_STATUS_1.getValue());
        //分类搜索
        if (StrUtil.isNotBlank(productQueryParam.getSid()) && !productQueryParam.getSid().equals(ShopConstants.PUSHMALL_ZERO)) {
            String sid = productQueryParam.getSid();
            wrapper.in(YxStoreProduct::getCateId, Arrays.asList(sid.split(",")));
        }
        //关键字搜索
        if (StrUtil.isNotEmpty(productQueryParam.getKeyword())) {
            wrapper.like(YxStoreProduct::getStoreName, productQueryParam.getKeyword());
        }

        //销量排序
        if (SortEnum.DESC.getValue().equals(productQueryParam.getSalesOrder())) {
            wrapper.orderByDesc(YxStoreProduct::getSales);
        } else if (SortEnum.ASC.getValue().equals(productQueryParam.getSalesOrder())) {
            wrapper.orderByAsc(YxStoreProduct::getSales);
        }

        //价格排序
        if (SortEnum.DESC.getValue().equals(productQueryParam.getPriceOrder())) {
            wrapper.orderByDesc(YxStoreProduct::getPrice);
        } else if (SortEnum.ASC.getValue().equals(productQueryParam.getPriceOrder())) {
            wrapper.orderByAsc(YxStoreProduct::getPrice);
        }
        //wrapper.orderByDesc(YxStoreProduct::getSort);

        List<YxStoreProduct> yxStoreProducts = yxStoreProductMapper.selectList(wrapper);

        List<YxStoreProductQueryVo> list = storeProductMap.toDto(yxStoreProducts);
        for (YxStoreProductQueryVo vo : list) {
            vo.setSales(vo.getSales() + vo.getFicti());
            BigDecimal lowestPrice = storeProductAttrValueMapper.lowestPrice(vo.getId());
            BigDecimal maxtPrice = storeProductAttrValueMapper.maxPrice(vo.getId());
            BigDecimal maxtOtPrice = storeProductAttrValueMapper.maxOtPrice(vo.getId());
            Map<String, Object> returnMap = storeProductAttrService.getProductAttrDetail(vo.getId(), 0, 0);
            vo.setProductAttr((List<YxStoreProductAttrQueryVo>) returnMap.get("productAttr"));
            vo.setProductValue((Map<String, YxStoreProductAttrValue>) returnMap.get("productValue"));
            if (lowestPrice != null) {
                vo.setPrice(lowestPrice);
            }
            if (maxtPrice != null) {
                vo.setMaxPrice(maxtPrice);
            }
            if (maxtOtPrice != null) {
                vo.setOtPrice(maxtOtPrice);
            }
        }


        return list;
    }

    @Override
    public List<YxStoreProductQueryVo> getTemplateGoodsList(YxStoreProductQueryParam productQueryParam) {
       return  yxStoreProductMapper.getYxStoreProductByTemplateId(productQueryParam.getCateId());
    }

    @Override
    public List<YxStoreProductQueryVo> getVideoGoodsList(YxStoreProductQueryParam productQueryParam) {
        List<YxStoreProductQueryVo> list =  yxStoreProductMapper.getYxStoreProductByVideoId(productQueryParam.getCateId());
        for (YxStoreProductQueryVo vo : list) {
            vo.setSales(vo.getSales() + vo.getFicti());
            BigDecimal lowestPrice = storeProductAttrValueMapper.lowestPrice(vo.getId());
            BigDecimal maxtPrice = storeProductAttrValueMapper.maxPrice(vo.getId());
            BigDecimal maxtOtPrice = storeProductAttrValueMapper.maxOtPrice(vo.getId());
            Map<String, Object> returnMap = storeProductAttrService.getProductAttrDetail(vo.getId(), 0, 0);
            vo.setProductAttr((List<YxStoreProductAttrQueryVo>) returnMap.get("productAttr"));
            vo.setProductValue((Map<String, YxStoreProductAttrValue>) returnMap.get("productValue"));
            if (lowestPrice != null) {
                vo.setPrice(lowestPrice);
            }
            if (maxtPrice != null) {
                vo.setMaxPrice(maxtPrice);
            }
            if (maxtOtPrice != null) {
                vo.setOtPrice(maxtOtPrice);
            }
        }
        return list;
    }

}
