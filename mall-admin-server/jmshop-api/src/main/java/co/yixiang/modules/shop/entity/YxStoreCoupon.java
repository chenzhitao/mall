package co.yixiang.modules.shop.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreCoupon对象", description = "优惠券表")
public class YxStoreCoupon extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "优惠券表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "优惠券名称")
    private String title;

    @ApiModelProperty(value = "兑换消耗积分值")
    private Integer integral;

    @ApiModelProperty(value = "兑换的优惠券面值")
    private BigDecimal couponPrice;

    @ApiModelProperty(value = "最低消费多少金额可用优惠券")
    private BigDecimal useMinPrice;

    @ApiModelProperty(value = "优惠券有效期限（单位：天）")
    private Integer couponTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态（0：关闭，1：开启）")
    private Boolean status;

    @ApiModelProperty(value = "兑换项目添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDel;

}
