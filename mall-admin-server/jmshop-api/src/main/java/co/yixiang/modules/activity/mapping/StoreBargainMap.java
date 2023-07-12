package co.yixiang.modules.activity.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.activity.entity.YxStoreBargain;
import co.yixiang.modules.activity.web.vo.YxStoreBargainQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-12-21
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreBargainMap extends EntityMapper<YxStoreBargainQueryVo, YxStoreBargain> {

}