/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.modules.shop.entity.YxVideo;
import co.yixiang.modules.shop.entity.YxVideoType;
import co.yixiang.modules.shop.mapper.YxVideoMapper;
import co.yixiang.modules.shop.mapper.YxVideoTypeMapper;
import co.yixiang.modules.shop.service.YxVideoService;
import co.yixiang.modules.shop.service.YxVideoTypeService;
import co.yixiang.modules.shop.web.vo.YxVideoQueryVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * 文章管理表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-02
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxVideoImpl extends BaseServiceImpl<YxVideoMapper, YxVideo> implements YxVideoService {

    private final YxVideoMapper yxVideoMapper;


    @Override
    public List<YxVideo> selectVideoList(int typeId) {
        return yxVideoMapper.selectVideoList(typeId);
    }

    @Override
    public YxVideoQueryVo covertVideoQuery(YxVideo video) {
        YxVideoQueryVo query=new YxVideoQueryVo();
        query.setId(video.getId());
        query.setTypeId(video.getTypeId());
        query.setTypeName(video.getTypeName());
        query.setTitle(video.getTitle());
        query.setCoverImage(video.getCoverImage());
        query.setVideoUrl(video.getVideoUrl());
        query.setScoreNum(video.getScoreNum());
        query.setWatchNum(video.getWatchNum());
        query.setVirtualWatchNum(video.getVirtualWatchNum());
        query.setCreateTime(video.getCreateTime());
        query.setRemark(video.getRemark());
        query.setShowFlag(video.getShowFlag());
        query.setSortNo(video.getSortNo());
        return query;
    }
}
