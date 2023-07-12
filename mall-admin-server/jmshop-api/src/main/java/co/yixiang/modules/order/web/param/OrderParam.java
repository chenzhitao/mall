package co.yixiang.modules.order.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName OrderParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class OrderParam implements Serializable {
    private String addressId;
    private Integer bargainId;
    private Integer combinationId;
    private Integer couponId;
    private String from;
    private String mark;
    @NotBlank(message="请选择支付方式")
    private String payType;
    private String phone;
    private Integer pinkId = 0;
    private String realName;
    private Integer seckillId;
    private Integer shippingType;
    private Double useIntegral;
    private Integer isChannel = 1;
    private Integer storeId;

}
