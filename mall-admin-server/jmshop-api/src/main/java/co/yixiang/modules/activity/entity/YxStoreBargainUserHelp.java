package co.yixiang.modules.activity.entity;

import java.math.BigDecimal;

import co.yixiang.serializer.BigDecimalSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * <p>
 * 砍价用户帮助表
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "YxStoreBargainUserHelp对象", description = "砍价用户帮助表")
public class YxStoreBargainUserHelp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "砍价用户帮助表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "帮助的用户id")
    private Integer uid;

    @ApiModelProperty(value = "砍价产品ID")
    private Integer bargainId;

    @ApiModelProperty(value = "用户参与砍价表id")
    private Integer bargainUserId;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "帮助砍价多少金额")
    private BigDecimal price;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

}
