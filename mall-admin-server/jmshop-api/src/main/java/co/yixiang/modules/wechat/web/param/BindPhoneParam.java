package co.yixiang.modules.wechat.web.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName BindPhoneParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/2/7
 **/
@Getter
@Setter
public class BindPhoneParam {
    @NotBlank(message = "验证码必填")
    private String captcha;

    @NotBlank(message = "手机号必填")
    private String phone;
}
