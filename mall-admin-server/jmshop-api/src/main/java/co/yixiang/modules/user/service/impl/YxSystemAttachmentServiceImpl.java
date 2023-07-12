/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.user.service.impl;

import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.mapper.YxSystemAttachmentMapper;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.web.param.YxSystemAttachmentQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemAttachmentQueryVo;
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
 * 附件管理表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxSystemAttachmentServiceImpl extends BaseServiceImpl<YxSystemAttachmentMapper, YxSystemAttachment> implements YxSystemAttachmentService {

    private final YxSystemAttachmentMapper yxSystemAttachmentMapper;

    @Override
    public YxSystemAttachment getInfo(String name) {
        QueryWrapper<YxSystemAttachment> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name).last("limit 1");
        return yxSystemAttachmentMapper.selectOne(wrapper);
    }

    @Override
    public YxSystemAttachment getByCode(String code) {
        QueryWrapper<YxSystemAttachment> wrapper = new QueryWrapper<>();
        wrapper.eq("invite_code",code).last("limit 1");
        return yxSystemAttachmentMapper.selectOne(wrapper);
    }

    @Override
    public void attachmentAdd(String name, String attSize, String attDir,String sattDir) {
        YxSystemAttachment attachment = new YxSystemAttachment();
        attachment.setName(name);
        attachment.setAttSize(attSize);
        attachment.setAttDir(attDir);
        attachment.setAttType("image/jpeg");
        attachment.setSattDir(sattDir);
        attachment.setTime(OrderUtil.getSecondTimestampTwo());
        attachment.setImageType(1);
        attachment.setModuleType(2);
        attachment.setPid(1);
        yxSystemAttachmentMapper.insert(attachment);
    }

    @Override
    public void newAttachmentAdd(String name, String attSize, String attDir, String sattDir, int uid, String code) {
        YxSystemAttachment attachment = new YxSystemAttachment();
        attachment.setName(name);
        attachment.setAttSize(attSize);
        attachment.setAttDir(attDir);
        attachment.setAttType("image/jpeg");
        attachment.setSattDir(sattDir);
        attachment.setTime(OrderUtil.getSecondTimestampTwo());
        attachment.setImageType(1);
        attachment.setModuleType(2);
        attachment.setPid(1);
        attachment.setUid(uid);
        attachment.setInviteCode(code);
        yxSystemAttachmentMapper.insert(attachment);
    }

    @Override
    public YxSystemAttachmentQueryVo getYxSystemAttachmentById(Serializable id) throws Exception{
        return yxSystemAttachmentMapper.getYxSystemAttachmentById(id);
    }

    @Override
    public Paging<YxSystemAttachmentQueryVo> getYxSystemAttachmentPageList(YxSystemAttachmentQueryParam yxSystemAttachmentQueryParam) throws Exception{
        Page page = setPageParam(yxSystemAttachmentQueryParam,OrderItem.desc("create_time"));
        IPage<YxSystemAttachmentQueryVo> iPage = yxSystemAttachmentMapper.getYxSystemAttachmentPageList(page,yxSystemAttachmentQueryParam);
        return new Paging(iPage);
    }

}
