package co.yixiang.modules.user.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName UserExtParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/13
 **/
@Data
public class UserExtParam implements Serializable {

    //支付宝用户名
    private String alipayCode;

    @NotBlank(message = "体现类型不能为空")
    private String extractType;

    @NotBlank(message = "金额不能为空")
    private String money;

    //微信号
    private String weixin;

    //支付宝账号
    private String name;
}
