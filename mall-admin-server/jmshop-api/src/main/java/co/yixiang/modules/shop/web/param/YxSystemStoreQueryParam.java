package co.yixiang.modules.shop.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 门店自提 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="门店自提", description="门店自提查询参数")
public class YxSystemStoreQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    private String latitude;

    private String longitude;

}
