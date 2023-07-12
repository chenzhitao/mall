/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.modules.activity.entity.YxStoreBargainUser;
import co.yixiang.modules.activity.entity.YxStoreBargainUserHelp;
import co.yixiang.modules.activity.mapper.YxStoreBargainUserHelpMapper;
import co.yixiang.modules.activity.mapping.StoreBargainHelpMap;
import co.yixiang.modules.activity.service.YxStoreBargainUserHelpService;
import co.yixiang.modules.activity.service.YxStoreBargainUserService;
import co.yixiang.modules.activity.web.param.YxStoreBargainUserHelpQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainUserHelpQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
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
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 砍价用户帮助表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreBargainUserHelpServiceImpl extends BaseServiceImpl<YxStoreBargainUserHelpMapper, YxStoreBargainUserHelp> implements YxStoreBargainUserHelpService {

    @Autowired
    private YxStoreBargainUserHelpMapper yxStoreBargainUserHelpMapper;

    @Autowired
    private YxStoreBargainUserService storeBargainUserService;
    @Autowired
    private YxUserService userService;

    @Autowired
    private StoreBargainHelpMap storeBargainHelpMap;


    /**
     * 获取砍价帮
     * @param bargainId
     * @param bargainUserUid
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<YxStoreBargainUserHelpQueryVo> getList(int bargainId, int bargainUserUid,
                                                       int page, int limit) {

        YxStoreBargainUser storeBargainUser = storeBargainUserService
                .getBargainUserInfo(bargainId,bargainUserUid);
        if(ObjectUtil.isNull(storeBargainUser)) {
            return new ArrayList<>();
        }
        Page<YxStoreBargainUserHelp> pageModel = new Page<>(page, limit);
        QueryWrapper<YxStoreBargainUserHelp> wrapper = new QueryWrapper<>();
        wrapper.eq("bargain_user_id",storeBargainUser.getId()).orderByDesc("id");

        List<YxStoreBargainUserHelpQueryVo> storeBargainUserHelpQueryVos = storeBargainHelpMap
                .toDto(yxStoreBargainUserHelpMapper.selectPage(pageModel,wrapper).getRecords());

        storeBargainUserHelpQueryVos.forEach(item->{
            YxUserQueryVo yxUserQueryVo = userService.getYxUserById(item.getUid());
            item.setAvatar(yxUserQueryVo.getAvatar());
            item.setNickname(yxUserQueryVo.getNickname());
        });

        return storeBargainUserHelpQueryVos;
    }

    /**
     * 获取砍价帮总人数
     * @param bargainId 砍价产品ID
     * @param bargainUserUid 用户参与砍价表id
     * @return
     */
    @Override
    public int getBargainUserHelpPeopleCount(int bargainId, int bargainUserUid) {
        return yxStoreBargainUserHelpMapper.selectCount(new QueryWrapper<YxStoreBargainUserHelp>()
                .eq("bargain_id", bargainId).eq("bargain_user_id",bargainUserUid));
    }

    @Override
    public YxStoreBargainUserHelpQueryVo getYxStoreBargainUserHelpById(Serializable id) throws Exception{
        return yxStoreBargainUserHelpMapper.getYxStoreBargainUserHelpById(id);
    }

    @Override
    public Paging<YxStoreBargainUserHelpQueryVo> getYxStoreBargainUserHelpPageList(YxStoreBargainUserHelpQueryParam yxStoreBargainUserHelpQueryParam) throws Exception{
        Page page = setPageParam(yxStoreBargainUserHelpQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreBargainUserHelpQueryVo> iPage = yxStoreBargainUserHelpMapper.getYxStoreBargainUserHelpPageList(page,yxStoreBargainUserHelpQueryParam);
        return new Paging(iPage);
    }

}
