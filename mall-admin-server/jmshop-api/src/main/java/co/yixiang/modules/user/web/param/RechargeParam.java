package co.yixiang.modules.user.web.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName RechargeParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/12/8
 **/
@Data
public class RechargeParam implements Serializable {
    /**
     * 访问类型： weixinh5 / xx
     */
    private String from;

    /**
     * 付款金额
     */
    @NotNull(message = "金额必填")
    @Min(value = 1, message = "充值金额不能低于1")
    private Double price;

    /**
     * 赠送金额
     */
    private Double paidPrice = 0d;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 类型 1充值 2购买会员
     */
    private Integer type;

    /**
     * 订单等级
     */
    private Integer systemUserLevelId;
}
