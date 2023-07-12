package co.yixiang.modules.order.web.dto;

import co.yixiang.serializer.DoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * @ClassName PriceGroup
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
public class PriceGroupDTO {

    @JsonSerialize(using = DoubleSerializer.class)
    private Double costPrice;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double storeFreePostage;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double storePostage;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double totalPrice;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double vipPrice;

}
