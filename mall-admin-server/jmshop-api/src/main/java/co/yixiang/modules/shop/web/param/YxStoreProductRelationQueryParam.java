package co.yixiang.modules.shop.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 商品点赞和收藏表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxStoreProductRelationQueryParam对象", description="商品点赞和收藏表查询参数")
public class YxStoreProductRelationQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String category;

    private Integer id;
}
