package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.util.List;
import co.yixiang.annotation.Query;

/**
* @author yushen
* @date 2023-06-08
*/
@Data
public class YxVideoProductQueryCriteria{

    /** id */
    @Query
    private Integer id;

    /** 模板ID */
    @Query
    private Integer videoId;

    @Query
    private Integer productId;
}
