/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service;

import co.yixiang.modules.activity.entity.YxStoreSeckill;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.activity.web.dto.StoreSeckillDTO;
import co.yixiang.modules.activity.web.param.YxStoreSeckillQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreSeckillQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品秒杀产品表 服务类
 * </p>
 *
 * @author xuwenbo
 * @since 2019-12-14
 */
public interface YxStoreSeckillService extends BaseService<YxStoreSeckill> {

    void incStockDecSales(int num,int seckillId);

    void decStockIncSales(int num,int seckillId);

    YxStoreSeckill getSeckill(int id);

    StoreSeckillDTO getDetail(int id) throws Exception;


    /**
     * 分页获取产品详情
     * @param page
     * @param limit
     * @return
     */
    List<YxStoreSeckillQueryVo> getList(int page, int limit, int time);
    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreSeckillQueryVo getYxStoreSeckillById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxStoreSeckillQueryParam
     * @return
     */
    Paging<YxStoreSeckillQueryVo> getYxStoreSeckillPageList(YxStoreSeckillQueryParam yxStoreSeckillQueryParam) throws Exception;

}
