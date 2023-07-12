package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 优惠券前台用户领取记录表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-27
 */
@Data
@ApiModel(value="YxStoreCouponIssueUserQueryVo对象", description="优惠券前台用户领取记录表查询参数")
public class YxStoreCouponIssueUserQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

private Integer id;

@ApiModelProperty(value = "领取优惠券用户ID")
private Integer uid;

@ApiModelProperty(value = "优惠券前台领取ID")
private Integer issueCouponId;

@ApiModelProperty(value = "领取时间")
private Integer addTime;

}