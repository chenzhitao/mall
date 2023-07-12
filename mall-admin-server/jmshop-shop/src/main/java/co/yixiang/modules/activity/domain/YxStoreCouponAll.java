package co.yixiang.modules.activity.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述：
 * 作者：godly.strong
 * 时间：2021/3/31
 */
@Data
public class YxStoreCouponAll {

    // 优惠券名称
    @ApiModelProperty(value = "优惠券名称")
    private String title;

    // 兑换消耗积分值
    @ApiModelProperty(value = "兑换消耗积分值")
    private Integer integral;

    // 兑换的优惠券面值
    @ApiModelProperty(value = "兑换的优惠券面值")
    private BigDecimal couponPrice;

    // 最低消费多少金额可用优惠券
    @ApiModelProperty(value = "最低消费多少金额可用优惠券")
    private BigDecimal useMinPrice;

    // 优惠券有效期限（单位：天）
    @ApiModelProperty(value = "惠券有效期限（单位：天）")
    private Integer couponTime;

    // 排序
    @ApiModelProperty(value = "排序")
    private Integer sort;

    // 状态（0：关闭，1：开启）
    @ApiModelProperty(value = "状态（0：关闭，1：开启）")
    private Integer status;


    @ApiModelProperty(value = "领取时间不能为空")
    private Date startTimeDate;

    @ApiModelProperty(value = "结束时间不能为空")
    private Date endTimeDate;

    // 优惠券领取数量
    @ApiModelProperty(value = "优惠券领取数量")
    private Integer totalCount;

    // 是否无限张数
    @ApiModelProperty(value = "是否无限张数")
    private Integer isPermanent;

    @ApiModelProperty(value = "发布状态")
    private Integer issueStatus;

    @ApiModelProperty(value = "优惠券类型 0- 领取券（需要用户自动领取） 1-推送券（定向推送用户）")
    private Integer type;

    @ApiModelProperty(value = "商品分类")
    private Integer categoryId;

    @ApiModelProperty(value = "优惠券描述")
    private String remark;

    @NotNull(message = "优惠券期限类型不能为空")
    @ApiModelProperty(value = "优惠券有效期类型")
    private Integer timeType;

    @ApiModelProperty(value = "优惠券有效期限")
    private Integer timeNum;

}
