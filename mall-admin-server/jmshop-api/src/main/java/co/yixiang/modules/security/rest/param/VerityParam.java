package co.yixiang.modules.security.rest.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName VerityParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/25
 **/
@Data
public class VerityParam {
    @NotBlank(message = "手机号必填")
    private String phone;

    private String type;
}
