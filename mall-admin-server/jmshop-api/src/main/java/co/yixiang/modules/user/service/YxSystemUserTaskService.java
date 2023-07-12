/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service;

import co.yixiang.modules.user.entity.YxSystemUserTask;
import co.yixiang.common.service.BaseService;
import co.yixiang.modules.user.web.dto.TaskDTO;
import co.yixiang.modules.user.web.dto.UserLevelInfoDTO;
import co.yixiang.modules.user.web.param.YxSystemUserTaskQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserTaskQueryVo;
import co.yixiang.common.web.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 等级任务设置 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
public interface YxSystemUserTaskService extends BaseService<YxSystemUserTask> {

    List<YxSystemUserTaskQueryVo> tidyTask(List<YxSystemUserTaskQueryVo> task,int uid);

    int getTaskComplete(int levelId,int uid);

    TaskDTO getTaskList(int levelId, int uid, UserLevelInfoDTO level);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemUserTaskQueryVo getYxSystemUserTaskById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxSystemUserTaskQueryParam
     * @return
     */
    Paging<YxSystemUserTaskQueryVo> getYxSystemUserTaskPageList(YxSystemUserTaskQueryParam yxSystemUserTaskQueryParam) throws Exception;

}
