package co.yixiang.modules.order.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-10-26
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMap extends EntityMapper<YxStoreOrderQueryVo, YxStoreOrder> {

}