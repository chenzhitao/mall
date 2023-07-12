package co.yixiang.modules.user.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 附件管理表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxSystemAttachmentQueryParam对象", description="附件管理表查询参数")
public class YxSystemAttachmentQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
