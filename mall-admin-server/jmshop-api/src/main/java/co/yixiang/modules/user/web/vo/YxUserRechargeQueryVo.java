package co.yixiang.modules.user.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户充值表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-12-08
 */
@Data
@ApiModel(value = "YxUserRechargeQueryVo对象", description = "用户充值表查询参数")
public class YxUserRechargeQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "充值用户UID")
    private Integer uid;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal price;

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

}