package co.yixiang.modules.user.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-12-7
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemUserLevelMap extends EntityMapper<YxSystemUserLevelQueryVo, YxSystemUserLevel> {

}