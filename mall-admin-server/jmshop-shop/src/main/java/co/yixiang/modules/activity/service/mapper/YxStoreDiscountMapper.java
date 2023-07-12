package co.yixiang.modules.activity.service.mapper;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.activity.domain.YxStoreDiscount;
import co.yixiang.modules.activity.service.dto.YxStoreDiscountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author xuwenbo
* @date 2019-12-22
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxStoreDiscountMapper extends EntityMapper<YxStoreDiscountDTO, YxStoreDiscount> {

}