package co.yixiang.modules.user.entity;

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
 * 用户充值表
 * </p>
 *
 * @author hupeng
 * @since 2019-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxUserRecharge对象", description = "用户充值表")
public class YxUserRecharge extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "充值用户UID")
    private Integer uid;

    @ApiModelProperty(value = "充值用户")
    private String nickname;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal price;

    @ApiModelProperty(value = "赠送金额")
    private BigDecimal givePrice;

    @ApiModelProperty(value = "充值类型")
    private String rechargeType;

    @ApiModelProperty(value = "是否充值")
    private Integer paid;

    @ApiModelProperty(value = "充值支付时间")
    private Integer payTime;

    @ApiModelProperty(value = "充值时间")
    private Integer addTime;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;

    @ApiModelProperty(value = "充值类型：1充值 2购买会员")
    private Integer type;

    @ApiModelProperty(value = "等级ID")
    private Integer systemUserLevelId;
}
