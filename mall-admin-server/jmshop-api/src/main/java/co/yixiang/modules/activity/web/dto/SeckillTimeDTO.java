package co.yixiang.modules.activity.web.dto;

import lombok.Data;

@Data
public class SeckillTimeDTO {

    private Integer id;
    /**
     * 00:00
     */
    private String time;
    /**
     *状态
     */
    private String state;

    private Integer status;

    private Integer stop;
}
