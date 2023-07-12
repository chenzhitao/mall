package co.yixiang.modules.activity.web.dto;

import co.yixiang.serializer.DoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BargainCountDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/12/21
 **/
@Data
@Builder
public class BargainCountDTO implements Serializable {
    @JsonSerialize(using = DoubleSerializer.class)
    private Double  alreadyPrice;
    private Integer count;
    private Integer pricePercent;
    private Integer status;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double price; //剩余的砍价金额
    private Boolean userBargainStatus; // 是否帮别人砍,没砍是true，砍了false


}
