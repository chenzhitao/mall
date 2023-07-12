package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品分类表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-22
 */
@Data
@ApiModel(value="YxStoreCategoryQueryVo对象", description="商品分类表查询参数")
public class YxStoreCategoryQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "商品分类表ID")
private Integer id;

@ApiModelProperty(value = "父id")
private Integer pid;

@ApiModelProperty(value = "分类名称")
private String cateName;

@ApiModelProperty(value = "排序")
private Integer sort;

@ApiModelProperty(value = "图标")
private String pic;





}