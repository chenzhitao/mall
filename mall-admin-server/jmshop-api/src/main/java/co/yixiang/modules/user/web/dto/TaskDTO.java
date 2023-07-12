package co.yixiang.modules.user.web.dto;

import co.yixiang.modules.user.web.vo.YxSystemUserTaskQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName TaskDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/12/6
 **/
@Data
public class TaskDTO implements Serializable {
    private List<YxSystemUserTaskQueryVo> list;
    private Integer reachCount;
    private List<YxSystemUserTaskQueryVo> task;
}
