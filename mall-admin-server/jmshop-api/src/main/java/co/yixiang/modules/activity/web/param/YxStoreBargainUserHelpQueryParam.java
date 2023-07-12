package co.yixiang.modules.activity.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 砍价用户帮助表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="砍价用户帮助表查询参数", description="砍价用户帮助表查询参数")
public class YxStoreBargainUserHelpQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
