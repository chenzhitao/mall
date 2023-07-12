/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service;

import co.yixiang.modules.activity.entity.YxStoreCombination;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.activity.web.dto.StoreCombinationDTO;
import co.yixiang.modules.activity.web.param.YxStoreCombinationQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreCombinationQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 拼团产品表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-19
 */
public interface YxStoreCombinationService extends BaseService<YxStoreCombination> {

    YxStoreCombinationQueryVo getCombinationT(int id);
    void decStockIncSales(int num,int combinationId);

    void incStockDecSales(int num,int combinationId);

    YxStoreCombination getCombination(int id);

    boolean judgeCombinationStock(int combinationId,int cartNum);

    List<YxStoreCombinationQueryVo> getList(int page, int limit);

    StoreCombinationDTO getDetail(int id,int uid);


    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCombinationQueryVo getYxStoreCombinationById(Serializable id);

    /**
     * 获取分页对象
     * @param yxStoreCombinationQueryParam
     * @return
     */
    Paging<YxStoreCombinationQueryVo> getYxStoreCombinationPageList(YxStoreCombinationQueryParam yxStoreCombinationQueryParam) throws Exception;

}
