package co.yixiang.modules.manage.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderDataDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/25
 **/
@Data
public class OrderDataDTO implements Serializable {
    private Integer count;
    private Double price;
    private String time;
}
