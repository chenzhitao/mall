/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.wechat.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.wechat.entity.YxWechatTemplate;
import co.yixiang.modules.wechat.web.param.YxWechatTemplateQueryParam;
import co.yixiang.modules.wechat.web.vo.YxWechatTemplateQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 微信模板 服务类
 * </p>
 *
 * @author xuwenbo
 * @since 2019-12-10
 */
public interface YxWechatTemplateService extends BaseService<YxWechatTemplate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxWechatTemplateQueryVo getYxWechatTemplateById(Serializable id) throws Exception;

    /**
     * 获取分页对象
     * @param yxWechatTemplateQueryParam
     * @return
     */
    Paging<YxWechatTemplateQueryVo> getYxWechatTemplatePageList(YxWechatTemplateQueryParam yxWechatTemplateQueryParam) throws Exception;

}
