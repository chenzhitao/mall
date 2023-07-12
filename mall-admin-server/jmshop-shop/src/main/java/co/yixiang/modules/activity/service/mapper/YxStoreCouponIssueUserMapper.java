package co.yixiang.modules.activity.service.mapper;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.activity.domain.YxStoreCouponIssueUser;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueUserDTO;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
* @author hupeng
* @date 2019-11-09
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxStoreCouponIssueUserMapper extends EntityMapper<YxStoreCouponIssueUserDTO, YxStoreCouponIssueUser> {


}
