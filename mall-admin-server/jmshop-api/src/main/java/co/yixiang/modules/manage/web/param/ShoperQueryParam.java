package co.yixiang.modules.manage.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hupeng
 * @date 2019-02-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="商户查询参数", description="商户查询参数")
public class ShoperQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    private Integer status = 0;

    private Integer cate = 1;

    private Integer type = 1;
}
