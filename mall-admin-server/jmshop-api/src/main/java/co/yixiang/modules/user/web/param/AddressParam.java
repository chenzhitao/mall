package co.yixiang.modules.user.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName AddressParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class AddressParam  implements Serializable {
    private String id;
    @NotBlank
    private String real_name;
    private String post_code;
    private String is_default;
    @NotBlank
    private String detail;
    @NotBlank
    private String phone;
    private AddressDetailParam address;
}
