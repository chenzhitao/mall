/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxUserTaskFinish;
import co.yixiang.modules.user.mapper.YxUserTaskFinishMapper;
import co.yixiang.modules.user.service.YxUserTaskFinishService;
import co.yixiang.modules.user.web.param.YxUserTaskFinishQueryParam;
import co.yixiang.modules.user.web.vo.YxUserTaskFinishQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;


/**
 * <p>
 * 用户任务完成记录表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-07
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxUserTaskFinishServiceImpl extends BaseServiceImpl<YxUserTaskFinishMapper, YxUserTaskFinish> implements YxUserTaskFinishService {

    private final YxUserTaskFinishMapper yxUserTaskFinishMapper;


    /**
     * 设置任务完成
     * @param uid
     * @param taskId
     */
    @Override
    public void setFinish(int uid, int taskId) {
        QueryWrapper<YxUserTaskFinish> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).eq("task_id",taskId);
        int count = yxUserTaskFinishMapper.selectCount(wrapper);
        if(count == 0){
            YxUserTaskFinish userTaskFinish = new YxUserTaskFinish();
            userTaskFinish.setAddTime(OrderUtil.getSecondTimestampTwo());
            userTaskFinish.setUid(uid);
            userTaskFinish.setTaskId(taskId);
            userTaskFinish.setStatus(0);

            yxUserTaskFinishMapper.insert(userTaskFinish);
        }

    }

    @Override
    public YxUserTaskFinishQueryVo getYxUserTaskFinishById(Serializable id) throws Exception{
        return yxUserTaskFinishMapper.getYxUserTaskFinishById(id);
    }

    @Override
    public Paging<YxUserTaskFinishQueryVo> getYxUserTaskFinishPageList(YxUserTaskFinishQueryParam yxUserTaskFinishQueryParam) throws Exception{
        Page page = setPageParam(yxUserTaskFinishQueryParam,OrderItem.desc("create_time"));
        IPage<YxUserTaskFinishQueryVo> iPage = yxUserTaskFinishMapper.getYxUserTaskFinishPageList(page,yxUserTaskFinishQueryParam);
        return new Paging(iPage);
    }

}
