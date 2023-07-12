package co.yixiang.modules.manage.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName OrderPriceParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/26
 **/
@Data
public class OrderDeliveryParam implements Serializable {
    @NotBlank(message = "订单编号错误")
    private String orderId;
    @NotBlank(message = "快递单号必填")
    private String deliveryId;
    @NotBlank(message = "快递公司必填")
    private String deliveryName;
    @NotBlank(message = "快递方式必填")
    private String deliveryType;
}
