package co.yixiang.modules.user.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 等级任务设置
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxSystemUserTask对象", description = "等级任务设置")
public class YxSystemUserTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "配置原名")
    private String realName;

    @ApiModelProperty(value = "任务类型")
    private String taskType;

    @ApiModelProperty(value = "限定数")
    private Integer number;

    @ApiModelProperty(value = "等级id")
    private Integer levelId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否显示")
    private Integer isShow;

    @ApiModelProperty(value = "是否务必达成任务,1务必达成,0=满足其一")
    private Integer isMust;

    @ApiModelProperty(value = "任务说明")
    private String illustrate;

    @ApiModelProperty(value = "新增时间")
    private Integer addTime;

}
