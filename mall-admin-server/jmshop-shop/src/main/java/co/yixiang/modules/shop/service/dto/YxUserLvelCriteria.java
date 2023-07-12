package co.yixiang.modules.shop.service.dto;

import co.yixiang.annotation.Query;
import co.yixiang.modules.shop.domain.YxSystemUserLevel;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
* @author hupeng
* @date 2019-10-06
*/
@Data
public class YxUserLvelCriteria {


    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String uid;

    @Query
    private Integer status;

}
