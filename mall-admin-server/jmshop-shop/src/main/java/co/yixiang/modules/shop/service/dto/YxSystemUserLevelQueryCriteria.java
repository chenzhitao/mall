package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import lombok.Data;

/**
* @author hupeng
* @date 2019-12-04
*/
@Data
public class YxSystemUserLevelQueryCriteria{
    @Query
    private Integer isDel;
}
