package co.yixiang.modules.manage.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ChartDataDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/25
 **/
@Data
public class ChartDataDTO implements Serializable {
    private Double num;
    private String time;
}
