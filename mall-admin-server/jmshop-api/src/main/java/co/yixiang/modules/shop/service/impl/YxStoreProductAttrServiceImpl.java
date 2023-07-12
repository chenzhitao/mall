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
import co.yixiang.modules.shop.entity.YxStoreProductAttr;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.mapper.YxStoreProductAttrMapper;
import co.yixiang.modules.shop.mapper.YxStoreProductAttrValueMapper;
import co.yixiang.modules.shop.mapping.ProductAttrMap;
import co.yixiang.modules.shop.service.YxStoreProductAttrService;
import co.yixiang.modules.shop.web.dto.AttrValueDTO;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * <p>
 * 商品属性表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreProductAttrServiceImpl extends BaseServiceImpl<YxStoreProductAttrMapper, YxStoreProductAttr> implements YxStoreProductAttrService {

    private final YxStoreProductAttrMapper yxStoreProductAttrMapper;
    private final YxStoreProductAttrValueMapper yxStoreProductAttrValueMapper;

    private final ProductAttrMap productAttrMap;

    @Override
    public void incProductAttrStock(int num, int productId, String unique) {
        yxStoreProductAttrValueMapper.incStockDecSales(num,productId,unique);
    }

    @Override
    public void decProductAttrStock(int num, int productId, String unique) {
        yxStoreProductAttrValueMapper.decStockIncSales(num,productId,unique);
    }

    @Override
    public Map<String, Object> getProductAttrDetail(int productId,int uid,int type) {
        QueryWrapper<YxStoreProductAttr> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",productId).orderByAsc("attr_values");
        List<YxStoreProductAttr>  storeProductAttrs = yxStoreProductAttrMapper
                .selectList(wrapper);

        QueryWrapper<YxStoreProductAttrValue> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("product_id",productId);
        List<YxStoreProductAttrValue>  productAttrValues = yxStoreProductAttrValueMapper
                .selectList(wrapper2);

        List<Map<String, YxStoreProductAttrValue>> mapList = new ArrayList<>();

        Map<String, YxStoreProductAttrValue> map = new LinkedHashMap<>();
        for (YxStoreProductAttrValue value : productAttrValues) {

            map.put(value.getSuk(),value);
           // mapList.add(map);
        }

        List<YxStoreProductAttrQueryVo> yxStoreProductAttrQueryVoList = new ArrayList<>();

        for (YxStoreProductAttr attr : storeProductAttrs) {
            List<String> stringList = Arrays.asList(attr.getAttrValues().split(","));
            List<AttrValueDTO> attrValueDTOS = new ArrayList<>();
            for (String str : stringList) {
                AttrValueDTO attrValueDTO = new AttrValueDTO();
                attrValueDTO.setAttr(str);
                attrValueDTO.setCheck(false);

                attrValueDTOS.add(attrValueDTO);
            }
            YxStoreProductAttrQueryVo attrQueryVo = productAttrMap.toDto(attr);
            attrQueryVo.setAttrValue(attrValueDTOS);
            attrQueryVo.setAttrValueArr(stringList);

            yxStoreProductAttrQueryVoList.add(attrQueryVo);

        }

        //System.out.println(yxStoreProductAttrQueryVoList);
        //System.out.println(map);
        Map<String, Object> returnMap = new LinkedHashMap<>();


        returnMap.put("productAttr",yxStoreProductAttrQueryVoList);
        returnMap.put("productValue",map);
        return returnMap;
    }

    /**
     * 库存
     * @param unique
     * @return
     */
    @Override
    public int uniqueByStock(String unique) {
        QueryWrapper<YxStoreProductAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("`unique`",unique);
        return yxStoreProductAttrValueMapper.selectOne(wrapper).getStock();
    }

    @Override
    public Boolean issetProductUnique(int productId, String unique) {
        return null;
    }

    @Override
    public YxStoreProductAttrValue uniqueByAttrInfo(String unique) {
        QueryWrapper<YxStoreProductAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("`unique`",unique);
        return yxStoreProductAttrValueMapper.selectOne(wrapper);
    }
}
