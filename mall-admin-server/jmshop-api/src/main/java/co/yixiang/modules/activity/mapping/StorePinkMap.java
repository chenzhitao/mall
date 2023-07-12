package co.yixiang.modules.activity.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.activity.entity.YxStorePink;
import co.yixiang.modules.activity.web.vo.YxStorePinkQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-10-19
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StorePinkMap extends EntityMapper<YxStorePinkQueryVo, YxStorePink> {

}