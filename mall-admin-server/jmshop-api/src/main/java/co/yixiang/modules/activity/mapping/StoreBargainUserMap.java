package co.yixiang.modules.activity.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.activity.entity.YxStoreBargainUser;
import co.yixiang.modules.activity.web.vo.YxStoreBargainUserQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-12-21
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreBargainUserMap extends EntityMapper<YxStoreBargainUserQueryVo, YxStoreBargainUser> {

}