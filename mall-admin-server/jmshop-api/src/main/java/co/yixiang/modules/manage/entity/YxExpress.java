package co.yixiang.modules.manage.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 快递公司表
 * </p>
 *
 * @author hupeng
 * @since 2019-12-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxExpress对象", description = "快递公司表")
public class YxExpress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "快递公司id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "快递公司简称")
    private String code;

    @ApiModelProperty(value = "快递公司全称")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否显示")
    private Boolean isShow;

}
