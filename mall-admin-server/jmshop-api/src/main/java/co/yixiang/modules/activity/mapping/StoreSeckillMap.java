package co.yixiang.modules.activity.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.activity.entity.YxStoreSeckill;
import co.yixiang.modules.activity.web.vo.YxStoreSeckillQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-12-17
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreSeckillMap extends EntityMapper<YxStoreSeckillQueryVo, YxStoreSeckill> {

}