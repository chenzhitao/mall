package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.util.List;
import co.yixiang.annotation.Query;

/**
* @author yushen
* @date 2023-05-11
*/
@Data
public class YxProductTemplateItemQueryCriteria{

    /** id */
    @Query
    private Integer id;

    /** 模板ID */
    @Query
    private Integer templateId;

    @Query
    private Integer productId;

}
