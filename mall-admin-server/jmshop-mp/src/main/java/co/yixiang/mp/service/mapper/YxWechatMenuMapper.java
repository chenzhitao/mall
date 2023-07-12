package co.yixiang.mp.service.mapper;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.mp.domain.YxWechatMenu;
import co.yixiang.mp.service.dto.YxWechatMenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author hupeng
* @date 2019-10-06
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxWechatMenuMapper extends EntityMapper<YxWechatMenuDTO, YxWechatMenu> {

}