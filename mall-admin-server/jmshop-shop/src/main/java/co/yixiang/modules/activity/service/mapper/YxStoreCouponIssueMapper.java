package co.yixiang.modules.activity.service.mapper;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.activity.domain.YxStoreCouponIssue;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author hupeng
* @date 2019-11-09
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface YxStoreCouponIssueMapper extends EntityMapper<YxStoreCouponIssueDTO, YxStoreCouponIssue> {
}
