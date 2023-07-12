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
import co.yixiang.modules.shop.entity.YxProductTemplate;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.entity.YxVideoType;
import co.yixiang.modules.shop.mapper.*;
import co.yixiang.modules.shop.service.YxStoreProductAttrService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.YxStoreTemplateService;
import co.yixiang.modules.shop.service.YxVideoTypeService;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.vo.YxProductTemplateVo;
import co.yixiang.modules.shop.web.vo.YxStoreCategoryQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class YxVideoTypeImpl extends BaseServiceImpl<YxVideoTypeMapper, YxVideoType> implements YxVideoTypeService {

    private final YxVideoTypeMapper yxVideoTypeMapper;


    @Override
    public List<YxVideoType> selectVideoTypeList() {
        return yxVideoTypeMapper.selectVideoTypeList();
    }
}
