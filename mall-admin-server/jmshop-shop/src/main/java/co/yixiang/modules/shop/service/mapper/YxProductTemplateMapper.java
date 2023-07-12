package co.yixiang.modules.shop.service.mapper;

import co.yixiang.base.BaseMapper;
import co.yixiang.modules.shop.domain.YxProductTemplate;
import co.yixiang.modules.shop.service.dto.YxProductTemplateDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author yushen
* @date 2023-05-11
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxProductTemplateMapper extends BaseMapper<YxProductTemplateDto, YxProductTemplate> {

}