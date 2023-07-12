package co.yixiang.modules.order.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName OrderVerifyParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/03/05
 **/
@Data
public class OrderVerifyParam implements Serializable {

    private Integer isConfirm;
    @NotBlank(message = "缺少核销码")
    private String verifyCode;

}
