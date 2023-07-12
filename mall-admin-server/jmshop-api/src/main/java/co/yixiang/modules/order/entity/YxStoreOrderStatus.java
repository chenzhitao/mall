package co.yixiang.modules.order.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单操作记录表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreOrderStatus对象", description = "订单操作记录表")
public class YxStoreOrderStatus extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单id")
    private Integer oid;

    @ApiModelProperty(value = "操作类型")
    private String changeType;

    @ApiModelProperty(value = "操作备注")
    private String changeMessage;

    @ApiModelProperty(value = "操作时间")
    private Integer changeTime;

}
