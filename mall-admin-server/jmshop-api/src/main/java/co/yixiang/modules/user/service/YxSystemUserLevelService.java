/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.dto.UserLevelDTO;
import co.yixiang.modules.user.web.param.YxSystemUserLevelQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 设置用户等级表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
public interface YxSystemUserLevelService extends BaseService<YxSystemUserLevel> {

    int getNextLevelId(int levelId);

    boolean getClear(int levelId);

    List<YxSystemUserLevelQueryVo> getLevelListAndGrade(Integer levelId, boolean isTask);

    UserLevelDTO getLevelInfo(int uid, boolean isTask);

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxSystemUserLevelQueryVo getYxSystemUserLevelById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param yxSystemUserLevelQueryParam
     * @return
     */
    Paging<YxSystemUserLevelQueryVo> getYxSystemUserLevelPageList(YxSystemUserLevelQueryParam yxSystemUserLevelQueryParam) throws Exception;

}
