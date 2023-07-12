package co.yixiang.modules.wechat.web.param;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName WxPhoneParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/02/07
 **/
@Getter
@Setter
public class WxPhoneParam {
    @NotBlank(message = "code参数缺失")
    private String code;

    private String encryptedData;

    private String iv;
}
