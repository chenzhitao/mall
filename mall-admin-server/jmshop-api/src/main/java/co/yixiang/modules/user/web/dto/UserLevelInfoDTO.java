package co.yixiang.modules.user.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserLevelInfoDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/12/7
 **/
@Data
public class UserLevelInfoDTO implements Serializable {
    private Integer id;
    private Integer addTime;
    private Double discount;
    private Integer levelId;
    private String name;
    private String icon;
    private Integer grade;
}
