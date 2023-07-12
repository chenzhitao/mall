package co.yixiang.modules.wechat.mapper;

import co.yixiang.modules.wechat.entity.YxWechatTemplate;
import co.yixiang.modules.wechat.web.param.YxWechatTemplateQueryParam;
import co.yixiang.modules.wechat.web.vo.YxWechatTemplateQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 微信模板 Mapper 接口
 * </p>
 *
 * @author xuwenbo
 * @since 2019-12-10
 */
@Repository
public interface YxWechatTemplateMapper extends BaseMapper<YxWechatTemplate> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxWechatTemplateQueryVo getYxWechatTemplateById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxWechatTemplateQueryParam
     * @return
     */
    IPage<YxWechatTemplateQueryVo> getYxWechatTemplatePageList(@Param("page") Page page, @Param("param") YxWechatTemplateQueryParam yxWechatTemplateQueryParam);

}
