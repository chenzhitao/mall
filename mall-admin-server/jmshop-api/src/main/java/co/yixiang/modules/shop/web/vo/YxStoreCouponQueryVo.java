package co.yixiang.modules.shop.web.vo;

import co.yixiang.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 优惠券表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-27
 */
@Data
@ApiModel(value = "YxStoreCouponQueryVo对象", description = "优惠券表查询参数")
public class YxStoreCouponQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "优惠券表ID")
    private Integer id;

    @ApiModelProperty(value = "优惠券名称")
    private String title;

    @ApiModelProperty(value = "兑换消耗积分值")
    private Integer integral;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "兑换的优惠券面值")
    private BigDecimal couponPrice;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "最低消费多少金额可用优惠券")
    private BigDecimal useMinPrice;

    @ApiModelProperty(value = "优惠券有效期限")
    private Integer couponTime;

    @ApiModelProperty(value = "优惠券到期有效期")
    private Integer endTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态（0：关闭，1：开启）")
    private Boolean status;

    @ApiModelProperty(value = "兑换项目添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDel;

    @ApiModelProperty(value = "优惠券描述")
    private String remark;

    private Integer timeType;
    private Integer timeNum;

}
