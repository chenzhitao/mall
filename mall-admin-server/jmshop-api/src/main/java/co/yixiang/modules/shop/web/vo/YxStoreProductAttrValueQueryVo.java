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
 * 商品属性值表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-23
 */
@Data
@ApiModel(value = "YxStoreProductAttrValueQueryVo对象", description = "商品属性值表查询参数")
public class YxStoreProductAttrValueQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "原价")
    private String otPrice;

}
