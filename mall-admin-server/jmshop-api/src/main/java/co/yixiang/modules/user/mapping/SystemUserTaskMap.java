package co.yixiang.modules.user.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.user.entity.YxSystemUserTask;
import co.yixiang.modules.user.web.vo.YxSystemUserTaskQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-12-7
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemUserTaskMap extends EntityMapper<YxSystemUserTaskQueryVo, YxSystemUserTask> {

}