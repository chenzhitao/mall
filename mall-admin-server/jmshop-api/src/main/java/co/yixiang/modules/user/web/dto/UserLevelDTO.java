package co.yixiang.modules.user.web.dto;

import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName UserLevelDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/12/6
 **/
@Data
public class UserLevelDTO implements Serializable {
    private List<YxSystemUserLevelQueryVo> list;
    private TaskDTO task;
}
