package co.yixiang.modules.activity.web.dto;


import lombok.Data;

import java.util.List;

@Data
public class SeckillConfigDTO  {


    private List<SeckillTimeDTO> seckillTime;

    private String lovely;

    private Integer seckillTimeIndex;
}
