package co.yixiang.modules.shop.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 优惠券前台用户领取记录表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreCouponIssueUser对象", description = "优惠券前台用户领取记录表")
public class YxStoreCouponIssueUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "领取优惠券用户ID")
    private Integer uid;

    @ApiModelProperty(value = "优惠券前台领取ID")
    private Integer issueCouponId;

    @ApiModelProperty(value = "领取时间")
    private Integer addTime;

}
