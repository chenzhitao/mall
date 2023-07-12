package co.yixiang.modules.order.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName RefundParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/6
 **/
@Data
public class RefundParam implements Serializable {
    private String refund_reason_wap_explain;
    private String refund_reason_wap_img;
    @NotBlank(message = "请填写退款原因")
    private String text;
    @NotBlank(message = "参数错误")
    private String uni;
}
