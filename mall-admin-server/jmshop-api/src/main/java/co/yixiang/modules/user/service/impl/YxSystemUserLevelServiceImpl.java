/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.modules.user.entity.YxUserLevel;
import co.yixiang.modules.user.mapper.YxSystemUserLevelMapper;
import co.yixiang.modules.user.mapping.SystemUserLevelMap;
import co.yixiang.modules.user.service.YxSystemUserLevelService;
import co.yixiang.modules.user.service.YxSystemUserTaskService;
import co.yixiang.modules.user.service.YxUserLevelService;
import co.yixiang.modules.user.web.dto.TaskDTO;
import co.yixiang.modules.user.web.dto.UserLevelDTO;
import co.yixiang.modules.user.web.dto.UserLevelInfoDTO;
import co.yixiang.modules.user.web.param.YxSystemUserLevelQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 设置用户等级表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxSystemUserLevelServiceImpl extends BaseServiceImpl<YxSystemUserLevelMapper, YxSystemUserLevel> implements YxSystemUserLevelService {

    @Autowired
    private YxSystemUserLevelMapper yxSystemUserLevelMapper;
    @Autowired
    private SystemUserLevelMap systemUserLevelMap;
    @Autowired
    private YxUserLevelService userLevelService;
    @Autowired
    private YxSystemUserTaskService systemUserTaskService;

    /**
     * 获取当前的下一个会员id
     * @param levelId
     * @return
     */
    @Override
    public int getNextLevelId(int levelId) {
        QueryWrapper<YxSystemUserLevel> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del",0).eq("is_show",1).orderByAsc("grade");
        List<YxSystemUserLevel> list = yxSystemUserLevelMapper.selectList(wrapper);
        int grade = 0;
        for (YxSystemUserLevel userLevel : list) {
            if(userLevel.getId() == levelId) {
                grade = userLevel.getGrade();
            }
        }

        QueryWrapper<YxSystemUserLevel> wrapperT = new QueryWrapper<>();
        wrapperT.eq("is_del",0).eq("is_show",1).orderByAsc("grade")
                .gt("grade",grade).last("limit 1");
        YxSystemUserLevel userLevel = yxSystemUserLevelMapper.selectOne(wrapperT);
        if(ObjectUtil.isNull(userLevel)) {
            return 0;
        }
        return userLevel.getId();
    }

    @Override
    public boolean getClear(int levelId) {
        List<YxSystemUserLevelQueryVo> systemUserLevelQueryVos = getLevelListAndGrade(
                levelId,false);
        for (YxSystemUserLevelQueryVo userLevelQueryVo : systemUserLevelQueryVos) {
            if(userLevelQueryVo.getId() == levelId) {
                return userLevelQueryVo.getIsClear();
            }
        }
        return false;
    }

    /**
     * 获取会员等级列表
     * @param levelId
     * @param isTask
     * @return
     */
    @Override
    public List<YxSystemUserLevelQueryVo> getLevelListAndGrade(Integer levelId, boolean isTask) {
        int grade = 0;

        // QueryWrapper<YxSystemUserLevel> wrapper = new QueryWrapper<>();
        // wrapper.eq("is_del",0).eq("is_show",1).orderByAsc("grade").last("LIMIT 1");

//        if(levelId == 0){
//            //levelId = yxSystemUserLevelMapper.selectOne(wrapper).getId();
//        }else{
//            levelId = 0;
//        }

        QueryWrapper<YxSystemUserLevel> wrapperT = new QueryWrapper<>();
        wrapperT.eq("is_del",0).eq("is_show",1).orderByAsc("grade");
        List<YxSystemUserLevel> list = yxSystemUserLevelMapper.selectList(wrapperT);
        List<YxSystemUserLevelQueryVo> newList = systemUserLevelMap.toDto(list);
        for (YxSystemUserLevelQueryVo userLevelQueryVo : newList) {
            if(userLevelQueryVo.getId().equals(levelId)) {
                grade = userLevelQueryVo.getGrade();
            }

            if(grade < userLevelQueryVo.getGrade()){
                userLevelQueryVo.setIsClear(true);
            }else{
                userLevelQueryVo.setIsClear(false);//开启会员解锁
            }
        }
        return newList;
    }

    /**
     * 获取会员等级列表
     * @return
     */
    @Override
    public UserLevelDTO getLevelInfo(int uid,boolean isTask) {
        int id = 0; //用户当前等级id
        YxUserLevel userLevel = userLevelService.getUserLevel(uid, 0);
        if(userLevel.getId()!=null){
            id = userLevel.getId();
        }

        UserLevelInfoDTO userLevelInfoDTO = null;
        if(id > 0) {
            userLevelInfoDTO = userLevelService.getUserLevelInfo(id);
        }
        int levelId = 0;
        if(ObjectUtil.isNotNull(userLevelInfoDTO)) {
            levelId = userLevelInfoDTO.getId();
        }
        List<YxSystemUserLevelQueryVo> list = getLevelListAndGrade(levelId,false);
        TaskDTO taskDTO = systemUserTaskService.getTaskList(list.get(0).getId(),uid,null);

        UserLevelDTO userLevelDTO = new UserLevelDTO();
        userLevelDTO.setList(list);
        userLevelDTO.setTask(taskDTO);

        return userLevelDTO;
    }

    @Override
    public YxSystemUserLevelQueryVo getYxSystemUserLevelById(Serializable id){
        return yxSystemUserLevelMapper.getYxSystemUserLevelById(id);
    }

    @Override
    public Paging<YxSystemUserLevelQueryVo> getYxSystemUserLevelPageList(YxSystemUserLevelQueryParam yxSystemUserLevelQueryParam) throws Exception{
        Page page = setPageParam(yxSystemUserLevelQueryParam,OrderItem.asc("grade"));
        IPage<YxSystemUserLevelQueryVo> iPage = yxSystemUserLevelMapper.getYxSystemUserLevelPageList(page,yxSystemUserLevelQueryParam);
        return new Paging(iPage);
    }

}
