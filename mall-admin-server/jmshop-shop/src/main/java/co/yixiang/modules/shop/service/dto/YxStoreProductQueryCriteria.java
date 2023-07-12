package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import co.yixiang.modules.shop.domain.YxStoreCategory;
import lombok.Data;

/**
* @author hupeng
* @date 2019-10-04
*/
@Data
public class YxStoreProductQueryCriteria{

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String storeName;

    // 精确
    @Query
    private Integer isDel;

    @Query
    private Integer isShow;

    @Query
    private Integer cateId;

}
