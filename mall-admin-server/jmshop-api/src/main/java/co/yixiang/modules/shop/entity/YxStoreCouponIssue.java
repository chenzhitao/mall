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
 * 优惠券前台领取表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreCouponIssue对象", description = "优惠券前台领取表")
public class YxStoreCouponIssue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "优惠券ID")
    private Integer cid;

    @ApiModelProperty(value = "优惠券领取开启时间")
    private Integer startTime;

    @ApiModelProperty(value = "优惠券领取结束时间")
    private Integer endTime;

    @ApiModelProperty(value = "优惠券领取数量")
    private Integer totalCount;

    @ApiModelProperty(value = "优惠券剩余领取数量")
    private Integer remainCount;

    @ApiModelProperty(value = "是否无限张数")
    private Boolean isPermanent;

    @ApiModelProperty(value = "1 正常 0 未开启 -1 已无效")
    private Boolean status;

    private Boolean isDel;

    @ApiModelProperty(value = "优惠券添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "优惠券时间类型")
    private Integer timeType;

    @ApiModelProperty(value = "优惠券期限")
    private Integer timeNum;

}
