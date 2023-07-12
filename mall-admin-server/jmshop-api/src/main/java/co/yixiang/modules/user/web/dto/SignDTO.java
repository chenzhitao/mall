package co.yixiang.modules.user.web.dto;


import lombok.Data;

import java.io.Serializable;


/**
 * @ClassName SignDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/12/5
 **/
@Data
public class SignDTO implements Serializable {
    private String addTime;
    private String title;
    private Integer number;
}
