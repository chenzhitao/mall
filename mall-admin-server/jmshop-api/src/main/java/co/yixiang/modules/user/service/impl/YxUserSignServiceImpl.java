/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxUserSign;
import co.yixiang.modules.user.mapper.YxUserBillMapper;
import co.yixiang.modules.user.mapper.YxUserSignMapper;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserLevelService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.YxUserSignService;
import co.yixiang.modules.user.web.dto.SignDTO;
import co.yixiang.modules.user.web.param.YxUserSignQueryParam;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.modules.user.web.vo.YxUserSignQueryVo;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 签到记录表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-05
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxUserSignServiceImpl extends BaseServiceImpl<YxUserSignMapper, YxUserSign> implements YxUserSignService {

    private YxUserSignMapper yxUserSignMapper;
    private YxUserBillMapper userBillMapper;

    private  YxSystemGroupDataService systemGroupDataService;
    private YxUserService yxUserService;
    private YxUserBillService billService;
    private YxUserLevelService userLevelService;



    /**
     * 用户签到
     * @param uid
     */
    @Override
    public int sign(int uid) {
        List<Map<String,Object>> list = systemGroupDataService.getDatas(ShopConstants.JMSHOP_SIGN_DAY_NUM);
        if(ObjectUtil.isNull(list)) {
            throw new ErrorRequestException("请先配置签到天数");
        }

        YxUserQueryVo userQueryVo = yxUserService.getYxUserById(uid);
        int signNumber = 0; //积分
        int userSignNum = userQueryVo.getSignNum(); //签到次数
        if(getYesterDayIsSign(uid)){
            if(userQueryVo.getSignNum() > (list.size() - 1)){
                userSignNum = 0;
            }
        }else{
            userSignNum = 0;
        }
        int index = 0;
        for (Map<String,Object> map : list) {
            if(index == userSignNum){
                signNumber = Integer.valueOf(map.get("sign_num").toString());
                break;
            }
            index++;
        }

        userSignNum += 1;

        YxUserSign userSign = new YxUserSign();
        userSign.setUid(uid);
        String title = "签到奖励";
        if(userSignNum == list.size()){
            title = "连续签到奖励";
        }
        userSign.setTitle(title);
        userSign.setNumber(signNumber);
        userSign.setBalance(userQueryVo.getIntegral().intValue());
        userSign.setAddTime(OrderUtil.getSecondTimestampTwo());
        yxUserSignMapper.insert(userSign);

        //用户积分增加
        YxUser yxUser = new YxUser();
        yxUser.setIntegral(NumberUtil.add(userQueryVo.getIntegral(),signNumber));
        yxUser.setUid(uid);
        yxUser.setSignNum(userSignNum);
        yxUserService.updateById(yxUser);

        //插入流水
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(uid);
        userBill.setTitle(title);
        userBill.setLinkId("0");
        userBill.setCategory("integral");
        userBill.setType("sign");
        userBill.setNumber(BigDecimal.valueOf(signNumber));
        userBill.setBalance(userQueryVo.getIntegral());
        userBill.setMark("");
        userBill.setStatus(1);
        userBill.setPm(1);
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        billService.save(userBill);

        //检查是否符合会员升级条件
        userLevelService.setLevelComplete(uid);
        return signNumber;
    }

    /**
     * 分页获取用户签到数据
     * @param uid
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<SignDTO> getSignList(int uid, int page, int limit) {
        Page<YxUserBill> pageModel = new Page<>(page, limit);
        return userBillMapper.getSignList(uid,pageModel);
    }

    /**
     * 获取用户昨天是否签到
     * @param uid
     * @return
     */
    @Override
    public boolean getYesterDayIsSign(int uid) {
        int today = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(new Date()));
        int yesterday = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(DateUtil.
                yesterday()));

        QueryWrapper<YxUserSign> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).lt("add_time",today).ge("add_time",yesterday);
        int count = yxUserSignMapper.selectCount(wrapper);
        if(count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户今天是否签到
     * @param uid
     * @return
     */
    @Override
    public boolean getToDayIsSign(int uid) {
        int today = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(new Date()));
        int yesterday = OrderUtil.dateToTimestampT(DateUtil.beginOfDay(DateUtil.
                yesterday()));

        QueryWrapper<YxUserSign> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid).ge("add_time",today);
        int count = yxUserSignMapper.selectCount(wrapper);
        if(count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户累计签到次数
     * @param uid
     * @return
     */
    @Override
    public int getSignSumDay(int uid) {
        QueryWrapper<YxUserSign> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid);
        int count = yxUserSignMapper.selectCount(wrapper);
        return count;
    }

    @Override
    public YxUserSignQueryVo getYxUserSignById(Serializable id) throws Exception{
        return yxUserSignMapper.getYxUserSignById(id);
    }

    @Override
    public Paging<YxUserSignQueryVo> getYxUserSignPageList(YxUserSignQueryParam yxUserSignQueryParam) throws Exception{
        Page page = setPageParam(yxUserSignQueryParam,OrderItem.desc("create_time"));
        IPage<YxUserSignQueryVo> iPage = yxUserSignMapper.getYxUserSignPageList(page,yxUserSignQueryParam);
        return new Paging(iPage);
    }

}
