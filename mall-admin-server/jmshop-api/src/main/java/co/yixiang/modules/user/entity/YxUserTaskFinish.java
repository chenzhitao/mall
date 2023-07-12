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
 * 用户任务完成记录表
 * </p>
 *
 * @author hupeng
 * @since 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxUserTaskFinish对象", description = "用户任务完成记录表")
public class YxUserTaskFinish extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务id")
    private Integer taskId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "是否有效")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

}
