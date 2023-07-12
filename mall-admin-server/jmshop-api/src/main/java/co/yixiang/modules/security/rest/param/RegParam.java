package co.yixiang.modules.security.rest.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName RegParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/25
 **/
@Data
public class RegParam {
    @NotBlank(message = "手机号必填")
    private String account;

    @NotBlank
    private String captcha;

    @NotBlank
    private String password;

    //todo
    private Integer spread;

    private String inviteCode;

    // 会员等级
    private Integer userLevel;
    // 会员等级申请证件
    private String certInfo;
}
