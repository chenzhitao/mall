package co.yixiang.modules.activity.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.activity.entity.YxStoreBargainUserHelp;
import co.yixiang.modules.activity.web.vo.YxStoreBargainUserHelpQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-12-22
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreBargainHelpMap extends EntityMapper<YxStoreBargainUserHelpQueryVo, YxStoreBargainUserHelp> {

}