package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品属性详情表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-23
 */
@Data
@ApiModel(value="YxStoreProductAttrResultQueryVo对象", description="商品属性详情表查询参数")
public class YxStoreProductAttrResultQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

private Integer id;

@ApiModelProperty(value = "商品ID")
private Integer productId;

@ApiModelProperty(value = "商品属性参数")
private String result;

@ApiModelProperty(value = "上次修改时间")
private Integer changeTime;

}