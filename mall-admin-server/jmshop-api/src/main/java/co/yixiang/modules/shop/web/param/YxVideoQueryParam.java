package co.yixiang.modules.shop.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxVideoParam对象", description="视频查询参数")
public class YxVideoQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    private Integer typeId;
    private String title;
}
