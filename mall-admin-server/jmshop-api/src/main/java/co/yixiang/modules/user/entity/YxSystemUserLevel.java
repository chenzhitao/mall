package co.yixiang.modules.user.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 设置用户等级表
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxSystemUserLevel对象", description = "设置用户等级表")
public class YxSystemUserLevel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "会员名称")
    private String name;

    @ApiModelProperty(value = "购买金额")
    private BigDecimal money;

    @ApiModelProperty(value = "有效时间")
    private Integer validDate;

    @ApiModelProperty(value = "是否为永久会员")
    private Integer isForever;

    @ApiModelProperty(value = "是否购买,1=购买,0=不购买")
    private Integer isPay;

    @ApiModelProperty(value = "是否显示 1=显示,0=隐藏")
    private Integer isShow;

    @ApiModelProperty(value = "会员等级")
    private Integer grade;

    @ApiModelProperty(value = "享受折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "会员卡背景")
    private String image;

    @ApiModelProperty(value = "会员图标")
    private String icon;

    @ApiModelProperty(value = "说明")
    @TableField(value = "`explain`")
    private String explain;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "是否删除.1=删除,0=未删除")
    private Integer isDel;

}
