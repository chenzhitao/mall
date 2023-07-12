package co.yixiang.modules.activity.web.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TopCountDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/12/21
 **/
@Data
@Builder
public class TopCountDTO implements Serializable {
    private Integer lookCount;
    private Integer shareCount;
    private Integer userCount;


}
