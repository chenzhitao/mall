/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service;

import co.yixiang.modules.activity.entity.YxStoreBargain;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.activity.web.dto.BargainCountDTO;
import co.yixiang.modules.activity.web.dto.BargainDTO;
import co.yixiang.modules.activity.web.dto.TopCountDTO;
import co.yixiang.modules.activity.web.param.YxStoreBargainQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 砍价表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
public interface YxStoreBargainService extends BaseService<YxStoreBargain> {

    void incStockDecSales(int num,int bargainId);

    void decStockIncSales(int num,int bargainId);

    YxStoreBargain getBargain(int bargainId);

    void doHelp(int bargainId,int bargainUserUid,int uid);

    TopCountDTO topCount(int bargainId);

    BargainCountDTO helpCount(int bargainId, int uid,int myUid);

    int getBargainPayCount(int bargainId);

    void addBargainShare(int id);

    void addBargainLook(int id);

    BargainDTO getDetail(int id, int uid);

    List<YxStoreBargainQueryVo> getList(int page, int limit);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreBargainQueryVo getYxStoreBargainById(Serializable id);

    /**
     * 获取分页对象
     * @param yxStoreBargainQueryParam
     * @return
     */
    Paging<YxStoreBargainQueryVo> getYxStoreBargainPageList(YxStoreBargainQueryParam yxStoreBargainQueryParam) throws Exception;

}
