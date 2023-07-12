/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.order.mapper.YxStoreOrderMapper;
import co.yixiang.modules.user.entity.YxSystemUserTask;
import co.yixiang.modules.user.entity.YxUserTaskFinish;
import co.yixiang.modules.user.mapper.YxSystemUserTaskMapper;
import co.yixiang.modules.user.mapper.YxUserBillMapper;
import co.yixiang.modules.user.mapper.YxUserTaskFinishMapper;
import co.yixiang.modules.user.mapping.SystemUserTaskMap;
import co.yixiang.modules.user.service.YxSystemUserTaskService;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserTaskFinishService;
import co.yixiang.modules.user.web.dto.TaskDTO;
import co.yixiang.modules.user.web.dto.UserLevelInfoDTO;
import co.yixiang.modules.user.web.param.YxSystemUserTaskQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserTaskQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 等级任务设置 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxSystemUserTaskServiceImpl extends BaseServiceImpl<YxSystemUserTaskMapper, YxSystemUserTask> implements YxSystemUserTaskService {

    private final YxSystemUserTaskMapper yxSystemUserTaskMapper;
    private final YxUserTaskFinishMapper yxUserTaskFinishMapper;
    private final YxUserBillMapper userBillMapper;
    private final YxStoreOrderMapper storeOrderMapper;

    private final YxUserTaskFinishService userTaskFinishService;
    private final YxUserBillService userBillService;

    private final SystemUserTaskMap systemUserTaskMap;

    /**
     * 设置任务内容完成情况
     * @param task
     * @return
     */
    @Override
    public List<YxSystemUserTaskQueryVo> tidyTask(List<YxSystemUserTaskQueryVo> task,int uid) {
        QueryWrapper<YxUserTaskFinish> wrapper = new QueryWrapper<>();
        for (YxSystemUserTaskQueryVo taskQueryVo : task) {
            wrapper.in("task_id",taskQueryVo.getId()).eq("uid",uid);
            int count = yxUserTaskFinishMapper.selectCount(wrapper);
            if(count > 0){
                taskQueryVo.setNewNumber(taskQueryVo.getNumber());
                taskQueryVo.setSpeed(100);
                taskQueryVo.setFinish(1);
                taskQueryVo.setTaskTypeTitle("");
            }else{
                double sumNumber = 0d;
                String title = "";
                switch (taskQueryVo.getTaskType()){
                    case "SatisfactionIntegral":
                        sumNumber = userBillMapper.sumIntegral(uid);
                        title = "还需要{0}经验";
                        break;
                    case "ConsumptionAmount":
                        sumNumber = storeOrderMapper.sumPrice(uid);
                        title = "还需消费{0}元";
                        break;
                    case "CumulativeAttendance":
                        sumNumber = userBillService.cumulativeAttendance(uid);
                        title = "还需签到{0}天";
                        break;
                }

                //System.out.println("sumNumber:"+sumNumber);
                //System.out.println("sumNumber2:"+taskQueryVo.getNumber());
                if(sumNumber >= taskQueryVo.getNumber()){
                    userTaskFinishService.setFinish(uid,taskQueryVo.getId());
                    taskQueryVo.setFinish(1);
                    taskQueryVo.setSpeed(100);
                    taskQueryVo.setTaskTypeTitle("");
                    taskQueryVo.setNewNumber(taskQueryVo.getNumber());
                }else{
                    double numdata = NumberUtil.sub(taskQueryVo.getNumber().doubleValue(),sumNumber);
                    taskQueryVo.setTaskTypeTitle(MessageFormat.format(title,numdata));
                    double speed = NumberUtil.div(sumNumber,taskQueryVo.getNumber().doubleValue());
                    taskQueryVo.setSpeed(Double.valueOf(NumberUtil.mul(speed,100)).intValue());
                    taskQueryVo.setFinish(0);
                    taskQueryVo.setNewNumber(Double.valueOf(sumNumber).intValue());
                }
            }
        }

        return task;
    }

    /**
     * 后去已经完成的任务数量
     * @param levelId
     * @param uid
     * @return
     */
    @Override
    public int getTaskComplete(int levelId, int uid) {
        QueryWrapper<YxSystemUserTask> wrapper = new QueryWrapper<>();
        wrapper.eq("level_id",levelId).eq("is_show",1);
        List<YxSystemUserTask> list = yxSystemUserTaskMapper.selectList(wrapper);
        List<Integer> taskIds = list.stream().map(YxSystemUserTask::getId)
                .collect(Collectors.toList());
        if(taskIds.isEmpty()) {
            return 0;
        }

        QueryWrapper<YxUserTaskFinish> wrapperT = new QueryWrapper<>();
        wrapperT.in("task_id",taskIds).eq("uid",uid);
        int count = yxUserTaskFinishMapper.selectCount(wrapperT);
        //System.out.println("count:"+count);
        return count;
    }

    /**
     * 获取等级会员任务列表
     * @param levelId
     * @param uid
     * @param level
     * @return
     */
    @Override
    public TaskDTO getTaskList(int levelId, int uid, UserLevelInfoDTO level) {
        QueryWrapper<YxSystemUserTask> wrapper = new QueryWrapper<>();
        wrapper.eq("level_id",levelId).eq("is_show",1)
                .orderByDesc("sort");
        List<YxSystemUserTaskQueryVo> list = systemUserTaskMap.toDto(yxSystemUserTaskMapper
                .selectList(wrapper));

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setList(list);
        taskDTO.setReachCount(getTaskComplete(levelId,uid));
        taskDTO.setTask(tidyTask(list,uid));

        return taskDTO;
    }

    @Override
    public YxSystemUserTaskQueryVo getYxSystemUserTaskById(Serializable id) throws Exception{
        return yxSystemUserTaskMapper.getYxSystemUserTaskById(id);
    }

    @Override
    public Paging<YxSystemUserTaskQueryVo> getYxSystemUserTaskPageList(YxSystemUserTaskQueryParam yxSystemUserTaskQueryParam) throws Exception{
        Page page = setPageParam(yxSystemUserTaskQueryParam,OrderItem.desc("create_time"));
        IPage<YxSystemUserTaskQueryVo> iPage = yxSystemUserTaskMapper.getYxSystemUserTaskPageList(page,yxSystemUserTaskQueryParam);
        return new Paging(iPage);
    }

}
