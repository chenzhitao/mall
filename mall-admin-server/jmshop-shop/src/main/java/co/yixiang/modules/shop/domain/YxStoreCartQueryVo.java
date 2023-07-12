package co.yixiang.modules.shop.domain;

import co.yixiang.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 购物车表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-25
 */
@Data
@ApiModel(value = "YxStoreCartQueryVo对象", description = "购物车表查询参数")
public class YxStoreCartQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "购物车表ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "商品属性")
    private String productAttrUnique;

    @ApiModelProperty(value = "商品数量")
    private Integer cartNum;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "拼团id")
    private Integer combinationId;

    @ApiModelProperty(value = "秒杀产品ID")
    private Integer seckillId;

    @ApiModelProperty(value = "砍价id")
    private Integer bargainId;

    @ApiModelProperty(value = "订单成本价")
    private Double costPrice;

    @ApiModelProperty(value = "订单总金额")
    private Double truePrice;

    @ApiModelProperty(value = "商品库存")
    private Integer trueStock;

    @ApiModelProperty(value = "设置会员价")
    private Double vipTruePrice;

    private String unique;

    @ApiModelProperty(value = "新增是否评价字段")
    private Integer isReply;

    @ApiModelProperty(value = "商品包装规格")
    private String packaging;

    @ApiModelProperty(value = "商品包装规格数量")
    private Integer packNum;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "修改价格")
    private BigDecimal alterPrice;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "原价")
    private BigDecimal otPrice;


}
