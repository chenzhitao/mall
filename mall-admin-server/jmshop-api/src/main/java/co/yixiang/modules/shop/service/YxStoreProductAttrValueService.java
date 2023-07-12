/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrValueQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrValueQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 商品属性值表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
public interface YxStoreProductAttrValueService extends BaseService<YxStoreProductAttrValue> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductAttrValueQueryVo getYxStoreProductAttrValueById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreProductAttrValueQueryParam
     * @return
     */
    Paging<YxStoreProductAttrValueQueryVo> getYxStoreProductAttrValuePageList(YxStoreProductAttrValueQueryParam yxStoreProductAttrValueQueryParam) throws Exception;

}
