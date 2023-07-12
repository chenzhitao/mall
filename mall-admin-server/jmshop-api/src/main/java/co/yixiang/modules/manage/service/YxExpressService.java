/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.manage.service;

import co.yixiang.modules.manage.entity.YxExpress;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.manage.web.param.YxExpressQueryParam;
import co.yixiang.modules.manage.web.vo.YxExpressQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 快递公司表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-13
 */
public interface YxExpressService extends BaseService<YxExpress> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxExpressQueryVo getYxExpressById(Serializable id);

    /**
     * 获取分页对象
     * @param yxExpressQueryParam
     * @return
     */
    Paging<YxExpressQueryVo> getYxExpressPageList(YxExpressQueryParam yxExpressQueryParam) throws Exception;

}
