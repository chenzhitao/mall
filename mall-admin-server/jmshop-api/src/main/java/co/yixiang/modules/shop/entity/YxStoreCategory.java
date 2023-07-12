package co.yixiang.modules.shop.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreCategory对象", description = "商品分类表")
public class YxStoreCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品分类表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父id")
    private Integer pid;

    @ApiModelProperty(value = "分类名称")
    private String cateName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String pic;

    @ApiModelProperty(value = "是否推荐")
    private Boolean isShow;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

}
