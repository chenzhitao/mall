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
 * 组合数据详情表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxSystemGroupData对象", description="组合数据详情表")
public class YxSystemGroupData extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "组合数据详情ID")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "对应的数据名称")
private String groupName;

@ApiModelProperty(value = "数据组对应的数据值（json数据）")
private String value;

@ApiModelProperty(value = "添加数据时间")
private Integer addTime;

@ApiModelProperty(value = "数据排序")
private Integer sort;

@ApiModelProperty(value = "状态（1：开启；2：关闭；）")
private Boolean status;

}
