/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.activity.entity.YxStoreBargainUser;
import co.yixiang.modules.activity.entity.YxStoreBargainUserHelp;
import co.yixiang.modules.activity.mapper.YxStoreBargainUserMapper;
import co.yixiang.modules.activity.mapping.StoreBargainUserMap;
import co.yixiang.modules.activity.service.YxStoreBargainService;
import co.yixiang.modules.activity.service.YxStoreBargainUserHelpService;
import co.yixiang.modules.activity.service.YxStoreBargainUserService;
import co.yixiang.modules.activity.web.param.YxStoreBargainUserQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainQueryVo;
import co.yixiang.modules.activity.web.vo.YxStoreBargainUserQueryVo;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 用户参与砍价表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
@Slf4j
@Service
public class YxStoreBargainUserServiceImpl extends BaseServiceImpl<YxStoreBargainUserMapper, YxStoreBargainUser> implements YxStoreBargainUserService {

    @Autowired
    private YxStoreBargainUserMapper yxStoreBargainUserMapper;
    @Autowired
    private StoreBargainUserMap storeBargainUserMap;

    @Autowired
    private YxStoreBargainService storeBargainService;
    @Autowired
    private YxStoreBargainUserHelpService storeBargainUserHelpService;


    /**
     * 修改用户砍价状态
     * @param bargainId 砍价产品id
     * @param uid 用户id
     */
    @Override
    public void setBargainUserStatus(int bargainId, int uid) {
        YxStoreBargainUser storeBargainUser = getBargainUserInfo(bargainId,uid);
        if(ObjectUtil.isNull(storeBargainUser)) {
            return;
        }

        if(storeBargainUser.getStatus() != 1) {
            return;
        }
        double price = NumberUtil.sub(NumberUtil.sub(storeBargainUser.getBargainPrice(),
                storeBargainUser.getBargainPriceMin()),storeBargainUser.getPrice()).doubleValue();
        if(price > 0) {
            return;
        }

        storeBargainUser.setStatus(3);

        yxStoreBargainUserMapper.updateById(storeBargainUser);
    }

    /**
     * 砍价取消
     * @param bargainId
     * @param uid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bargainCancel(int bargainId, int uid) {
        YxStoreBargainUser storeBargainUser = getBargainUserInfo(bargainId,uid);
        if(ObjectUtil.isNull(storeBargainUser)) {
            throw new ErrorRequestException("数据不存在");
        }

        if(storeBargainUser.getStatus() != 1) {
            throw new ErrorRequestException("状态错误");
        }

        storeBargainUser.setIsDel(1);

        yxStoreBargainUserMapper.updateById(storeBargainUser);
    }

    /**
     * 获取用户的砍价产品
     * @param bargainUserUid
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<YxStoreBargainUserQueryVo> bargainUserList(int bargainUserUid, int page, int limit) {
        Page<YxStoreBargainUser> pageModel = new Page<>(page, limit);
        return yxStoreBargainUserMapper.getBargainUserList(bargainUserUid,pageModel);
    }

    /**
     * 判断用户是否还可以砍价
     * @param bargainId 砍价产品id
     * @param bargainUserUid 开启砍价用户id
     * @param uid  当前用户id
     * @return
     */
    @Override
    public  boolean isBargainUserHelp(int bargainId, int bargainUserUid, int uid) {
        YxStoreBargainUser storeBargainUser = getBargainUserInfo(bargainId,bargainUserUid);
        YxStoreBargainQueryVo storeBargainQueryVo = storeBargainService
                .getYxStoreBargainById(bargainId);
        if(ObjectUtil.isNull(storeBargainUser) || ObjectUtil.isNull(storeBargainQueryVo)){
            return false;
        }

        int count = storeBargainUserHelpService.count(new QueryWrapper<YxStoreBargainUserHelp>()
                .eq("bargain_id",bargainId)
                .eq("bargain_user_id",storeBargainUser.getId())
                .eq("uid",uid));

        if(count == 0) {
            return true;
        }


        return false;
    }

    /**
     * 添加砍价记录
     * @param bargainId
     * @param uid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setBargain(Integer bargainId, Integer uid) {
        if(ObjectUtil.isNotNull(getBargainUserInfo(bargainId,uid))) {
            return;
        }
        YxStoreBargainQueryVo storeBargainQueryVo = storeBargainService.getYxStoreBargainById(bargainId);
        YxStoreBargainUser storeBargainUser = YxStoreBargainUser
                .builder()
                .bargainId(bargainId)
                .uid(uid)
                .bargainPrice(storeBargainQueryVo.getPrice())
                .bargainPriceMin(storeBargainQueryVo.getMinPrice())
                .price(BigDecimal.ZERO)
                .status(1)
                .isDel(0)
                .addTime(OrderUtil.getSecondTimestampTwo())
                .build();

        yxStoreBargainUserMapper.insert(storeBargainUser);


    }

    /**
     * 获取用户可以砍掉的价格
     * @param id
     * @return
     */
    @Override
    public double getBargainUserDiffPrice(int id) {
        YxStoreBargainUserQueryVo storeBargainUserQueryVo = getYxStoreBargainUserById(id);
        return NumberUtil.sub(storeBargainUserQueryVo.getBargainPrice()
                ,storeBargainUserQueryVo.getBargainPriceMin()).doubleValue();
    }



    /**
     * 获取某个用户参与砍价信息
     * @param bargainId
     * @param uid
     * @return
     */
    @Override
    public YxStoreBargainUser getBargainUserInfo(int bargainId, int uid) {
        QueryWrapper<YxStoreBargainUser> wrapper = new QueryWrapper<>();
        wrapper.eq("bargain_id",bargainId).eq("uid",uid).eq("is_del",0).last("limit 1");
        return yxStoreBargainUserMapper.selectOne(wrapper);
    }

    /**
     * 获取参与砍价的用户列表
     * @param bargainId 砍价id
     * @param status  状态  1 进行中  2 结束失败  3结束成功
     * @return
     */
    @Override
    public List<YxStoreBargainUserQueryVo> getBargainUserList(int bargainId, int status) {
        QueryWrapper<YxStoreBargainUser> wrapper = new QueryWrapper<>();
        wrapper.eq("bargain_id",bargainId).eq("status",status);
        return storeBargainUserMap.toDto(yxStoreBargainUserMapper.selectList(wrapper));
    }

    @Override
    public YxStoreBargainUserQueryVo getYxStoreBargainUserById(Serializable id){
        return yxStoreBargainUserMapper.getYxStoreBargainUserById(id);
    }

    @Override
    public Paging<YxStoreBargainUserQueryVo> getYxStoreBargainUserPageList(YxStoreBargainUserQueryParam yxStoreBargainUserQueryParam) throws Exception{
        Page page = setPageParam(yxStoreBargainUserQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreBargainUserQueryVo> iPage = yxStoreBargainUserMapper.getYxStoreBargainUserPageList(page,yxStoreBargainUserQueryParam);
        return new Paging(iPage);
    }

}
