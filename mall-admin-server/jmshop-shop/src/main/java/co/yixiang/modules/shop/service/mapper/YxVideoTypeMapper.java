package co.yixiang.modules.shop.service.mapper;

import co.yixiang.base.BaseMapper;
import co.yixiang.modules.shop.domain.YxVideoType;
import co.yixiang.modules.shop.service.dto.YxVideoTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yushen
* @date 2023-06-08
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxVideoTypeMapper extends BaseMapper<YxVideoTypeDto, YxVideoType> {

}