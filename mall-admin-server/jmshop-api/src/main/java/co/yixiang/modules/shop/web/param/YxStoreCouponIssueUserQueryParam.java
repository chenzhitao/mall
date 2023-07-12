package co.yixiang.modules.shop.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 优惠券前台用户领取记录表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxStoreCouponIssueUserQueryParam对象", description="优惠券前台用户领取记录表查询参数")
public class YxStoreCouponIssueUserQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
