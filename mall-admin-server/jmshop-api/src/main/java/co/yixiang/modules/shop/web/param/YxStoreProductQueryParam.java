package co.yixiang.modules.shop.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

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
@ApiModel(value="YxStoreProductQueryParam对象", description="商品表查询参数")
public class YxStoreProductQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    private String type;
    private String sid;
    private String news;
    private String priceOrder;
    private String salesOrder;
    private String keyword;

    private String cateId;
}
