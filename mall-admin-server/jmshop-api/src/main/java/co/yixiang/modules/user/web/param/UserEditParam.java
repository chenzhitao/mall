package co.yixiang.modules.user.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName UserEditParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/02/07
 **/
@Data
public class UserEditParam implements Serializable {
    @NotBlank(message = "请上传头像")
    private String avatar;
    @NotBlank(message = "请填写昵称")
    private String nickname;

    private String cardId;

    private String weixin;

    private String qq;

    private String mail;

    private String addres;

    private String telephone;

    private String dingding;


}
