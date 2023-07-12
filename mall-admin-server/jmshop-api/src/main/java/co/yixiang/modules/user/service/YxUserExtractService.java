/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxUserExtract;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.param.UserExtParam;
import co.yixiang.modules.user.web.param.YxUserExtractQueryParam;
import co.yixiang.modules.user.web.vo.YxUserExtractQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;

/**
 * <p>
 * 用户提现表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
public interface YxUserExtractService extends BaseService<YxUserExtract> {

    void userExtract(int uid, UserExtParam param);

    double extractSum(int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserExtractQueryVo getYxUserExtractById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxUserExtractQueryParam
     * @return
     */
    Paging<YxUserExtractQueryVo> getYxUserExtractPageList(YxUserExtractQueryParam yxUserExtractQueryParam) throws Exception;

}
