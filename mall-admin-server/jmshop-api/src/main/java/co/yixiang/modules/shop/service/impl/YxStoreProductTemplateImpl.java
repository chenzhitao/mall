/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxArticle;
import co.yixiang.modules.shop.entity.YxProductTemplate;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.mapper.*;
import co.yixiang.modules.shop.service.ArticleService;
import co.yixiang.modules.shop.service.YxStoreProductAttrService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.YxStoreTemplateService;
import co.yixiang.modules.shop.web.param.YxArticleQueryParam;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 文章管理表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-02
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreProductTemplateImpl extends BaseServiceImpl<YxProductTemplateMapper, YxProductTemplate> implements YxStoreTemplateService {

    private final YxProductTemplateMapper productTemplateMapper;
    private final YxStoreProductMapper storeProductMapper;

    @Autowired
    private YxStoreProductAttrValueMapper storeProductAttrValueMapper;

    @Autowired
    private YxStoreProductAttrService storeProductAttrService;

    @Autowired
    private YxStoreCategoryMapper yxStoreCategoryMapper;
    @Autowired
    private YxStoreProductService yxStoreProductService;

    @Override
    public List<YxProductTemplateVo> selectProductTemplateList(){
        List<YxProductTemplateVo> templateList= productTemplateMapper.selectProductTemplateList();
        templateList.stream().forEach(template->{
            List<YxStoreProductQueryVo> productList= storeProductMapper.getYxStoreProductByTemplateId(template.getId());
            for (YxStoreProductQueryVo vo : productList) {
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
                    List<YxStoreProductQueryVo> plist= yxStoreProductService.getCategoryGoodsList(queryParam);
                    plist.sort((o1, o2) -> o1.getSalesNum().compareTo(o2.getSalesNum()));
                    YxStoreProductQueryVo checkVo= plist.stream().filter(pro->pro.getId().intValue()==vo.getId().intValue()).findFirst().orElse(null);
                    int index=plist.indexOf(checkVo);
                    if(index>0){
                        vo.setRankInfo(category.getCateName()+"品类榜 第"+(index+1)+"名");
                    }
                }
            }
            template.setProductList(productList);
            template.setProductQueryVo(storeProductMapper.getYxStoreProductById(template.getProductId()));
        });
        return templateList;
    }

}
