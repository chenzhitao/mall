package co.yixiang.modules.user.web.dto;

import lombok.Data;

/**
 * @ClassName PromUserDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class PromUserDTO {
    private String avatar;
    private String  nickname;
    private Integer childCount;
    private Integer numberCount;
    private Integer  orderCount;
    private Integer uid;
    private String time;
}
