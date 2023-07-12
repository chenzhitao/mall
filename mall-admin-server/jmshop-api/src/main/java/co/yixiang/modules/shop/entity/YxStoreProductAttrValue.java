package co.yixiang.modules.shop.entity;

import co.yixiang.common.entity.BaseEntity;
import co.yixiang.serializer.BigDecimalSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 商品属性值表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreProductAttrValue对象", description = "商品属性值表")
public class YxStoreProductAttrValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "商品属性索引值 (attr_value|attr_value[|....])")
    private String suk;

    @ApiModelProperty(value = "属性对应的库存")
    private Integer stock;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "属性金额")
    private BigDecimal price;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "唯一值")
    @TableField(value = "`unique`")
    private String unique;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "成本价")
    private BigDecimal cost;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "批发价")
    private BigDecimal wholesale;

    @ApiModelProperty(value = "商品编码")
    private String barCode;

    @ApiModelProperty(value = "包装规格")
    private String packaging;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "原价")
    private BigDecimal otPrice;

}
