/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.modules.shop.domain.YxSystemUserLevel;
import co.yixiang.modules.shop.domain.YxUserLevel;
import co.yixiang.modules.shop.repository.YxSystemUserLevelRepository;
import co.yixiang.modules.shop.repository.YxUserLevelRepository;
import co.yixiang.modules.shop.repository.YxUserRepository;
import co.yixiang.modules.shop.service.YxUserBillService;
import co.yixiang.modules.shop.service.YxUserLevelService;
import co.yixiang.modules.shop.service.YxWechatUserService;
import co.yixiang.modules.shop.service.dto.YxSystemUserLevelDTO;
import co.yixiang.modules.shop.service.dto.YxUserLvelCriteria;
import co.yixiang.modules.shop.service.dto.YxWechatUserDTO;
import co.yixiang.modules.shop.service.mapper.YxSystemUserLevelMapper;
import co.yixiang.modules.shop.service.mapper.YxUserMapper;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.QueryHelp;
import co.yixiang.utils.TencentMsgUtil;
import co.yixiang.utils.ValidationUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;


/**
 * <p>
 * 用户等级记录表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxUserLevelServiceImpl implements YxUserLevelService {

    private final YxUserLevelRepository yxUserLevelRepository;
    private final YxSystemUserLevelRepository yxSystemUserLevelRepository;
    private final YxSystemUserLevelMapper yxSystemUserLevelMapper;

    @Autowired
    private YxUserLevelService userLevelService;

    private final YxWechatUserService wechatUserService;

    public YxUserLevelServiceImpl(YxUserLevelRepository yxUserLevelRepository,
                                  YxSystemUserLevelRepository yxSystemUserLevelRepository,
                                  YxSystemUserLevelMapper yxSystemUserLevelMapper,
                                  YxWechatUserService wechatUserService) {
        this.yxUserLevelRepository = yxUserLevelRepository;
        this.yxSystemUserLevelRepository = yxSystemUserLevelRepository;
        this.yxSystemUserLevelMapper = yxSystemUserLevelMapper;
        this.wechatUserService = wechatUserService;
    }

    /**
     * 设置会员等级
     * @param uid
     * @param levelId
     */
    @Override
    public void setUserLevel(Integer uid, Integer levelId){
        Optional<YxSystemUserLevel> yxSystemUserLevel = yxSystemUserLevelRepository.findById(levelId);
        ValidationUtil.isNull(yxSystemUserLevel,"YxSystemUserLevel","id",levelId);
        YxSystemUserLevelDTO yxSystemUserLevelDTO = yxSystemUserLevelMapper.toDto(yxSystemUserLevel.get());

        if(ObjectUtil.isNull(yxSystemUserLevelDTO)) {
            return;
        }

        YxUserLevel yxUserLevel = yxUserLevelRepository.selectOne(uid);
        String mark = "恭喜你成为了"+yxSystemUserLevelDTO.getName();
        if(ObjectUtil.isNotNull(yxUserLevel)){
            yxUserLevelRepository.updateUserLevel(levelId,yxSystemUserLevelDTO.getGrade(),(yxSystemUserLevelDTO.getDiscount()).intValue(),mark, uid);
        }else{
            YxUserLevel userLevel = new YxUserLevel();
            userLevel.setIsForever(yxSystemUserLevelDTO.getIsForever());
            userLevel.setStatus(1);
            userLevel.setIsDel(0);
            userLevel.setGrade(yxSystemUserLevelDTO.getGrade());
            userLevel.setUid(uid);
            userLevel.setMerId(0);
            userLevel.setRemind(0);
            userLevel.setAddTime(OrderUtil.getSecondTimestampTwo());
            userLevel.setLevelId(levelId);
            userLevel.setDiscount(yxSystemUserLevelDTO.getDiscount().intValue());

            //有效期
            int validTime = yxSystemUserLevelDTO.getValidDate() * 86400;
            if (yxSystemUserLevelDTO.getIsForever() == 1) {
                userLevel.setValidTime(0);
            } else {
                userLevel.setValidTime(validTime + OrderUtil.getSecondTimestampTwo());
            }
            userLevel.setMark("恭喜你成为了" + yxSystemUserLevelDTO.getName());
            yxUserLevelRepository.save(userLevel);
        }
    }

    @Override
    public void autoNoticeExpireUser() {
        YxUserLvelCriteria levelParam=new YxUserLvelCriteria();
        levelParam.setStatus(1);
        List<YxUserLevel> userLevelList= yxUserLevelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,levelParam,criteriaBuilder));
        userLevelList.stream().forEach(userLevel->{
            int currentTIme=OrderUtil.getSecondTimestampTwo();
            if(userLevel.getValidTime()> currentTIme){
                int hour= (userLevel.getValidTime()-currentTIme)/86400;
                if(hour>0 && hour<=1){
                    YxWechatUserDTO wechatUser = wechatUserService.findById(userLevel.getUid());
                    //发送小程序消息
                    Map<String, Object> data = new HashMap<>();

                    Map<String, Object> value = new HashMap();
                    value.put("value","您的会员资格即将到期");
                    data.put("thing1", value);


                    Map<String, Object> value3 = new HashMap();
                    value3.put("value", OrderUtil.stampToDate(String.valueOf(userLevel.getValidTime())));
                    data.put("time3", value3);
                    //下单成功发送消息
                    TencentMsgUtil.sendTemplateMsg(data, "a92w0a8hCOup2kZhRLMz7wkq_GIsXK_wps05iW5Bcs8",wechatUser.getRoutineOpenid());
                    log.info("用户会员到期提醒结束：您的会员资格即将到期："+wechatUser.getOpenid());
                }
            }
        });
    }

    public static void main(String[] args) {
        int currentTIme=OrderUtil.getSecondTimestampTwo();
        int hour= (1707463846-currentTIme)/86400;
        System.out.println(hour);
       String str= OrderUtil.stampToDate(String.valueOf(1707463846));
        Date valDate=DateUtil.parseDateTime("2023-06-30 15:30:46");
        Long day= (valDate.getTime()/1000-currentTIme)/86400;
        System.out.println(valDate.getTime()/1000);
    }
}
