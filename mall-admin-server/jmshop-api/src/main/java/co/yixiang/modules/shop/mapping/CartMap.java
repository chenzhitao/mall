package co.yixiang.modules.shop.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.shop.entity.YxStoreCart;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-10-26
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMap extends EntityMapper<YxStoreCartQueryVo, YxStoreCart> {

}