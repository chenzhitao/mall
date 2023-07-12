package co.yixiang.modules.shop.mapping;

import co.yixiang.mapper.EntityMapper;
import co.yixiang.modules.shop.entity.YxStoreCouponUser;
import co.yixiang.modules.shop.web.vo.YxStoreCouponUserQueryVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* @author hupeng
* @date 2019-10-26
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CouponMap extends EntityMapper<YxStoreCouponUserQueryVo, YxStoreCouponUser> {

}