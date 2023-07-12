package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.web.param.YxSystemAttachmentQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemAttachmentQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 附件管理表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
@Repository
public interface YxSystemAttachmentMapper extends BaseMapper<YxSystemAttachment> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemAttachmentQueryVo getYxSystemAttachmentById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxSystemAttachmentQueryParam
     * @return
     */
    IPage<YxSystemAttachmentQueryVo> getYxSystemAttachmentPageList(@Param("page") Page page, @Param("param") YxSystemAttachmentQueryParam yxSystemAttachmentQueryParam);

}
