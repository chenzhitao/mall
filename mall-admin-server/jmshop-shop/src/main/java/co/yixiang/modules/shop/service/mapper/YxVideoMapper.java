package co.yixiang.modules.shop.service.mapper;

import co.yixiang.base.BaseMapper;
import co.yixiang.modules.shop.domain.YxVideo;
import co.yixiang.modules.shop.service.dto.YxVideoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yushen
* @date 2023-06-08
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxVideoMapper extends BaseMapper<YxVideoDto, YxVideo> {

}