package co.yixiang.modules.order.web.dto;

import co.yixiang.serializer.DoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ComputeDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
public class ComputeDTO implements Serializable {
    @JsonSerialize(using = DoubleSerializer.class)
    private Double couponPrice;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double deductionPrice;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double payPostage;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double payPrice;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double totalPrice;
}
