package co.yixiang.modules.shop.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 优惠券发放记录对象StoreCouponUserVO
 *
 * @author taozi
 * @date 2020-05-06
 */
@Getter
@Setter
public class StoreCouponUserVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 优惠券发放记录id */
    @ApiModelProperty(value = "优惠券发放记录ID")
    private int id;

    /** 优惠券名称 */
    @ApiModelProperty(value = "优惠券名称")
    private String couponTitle;

    /** 优惠券的面值 */
    @ApiModelProperty(value = "优惠券的面值")
    private Double couponPrice;

    /** 最低消费多少金额可用优惠券 */
    @ApiModelProperty(value = "最低消费多少金额可用优惠券")
    private Double useMinPrice;

    /** 优惠券结束时间 */
    @ApiModelProperty(value = "优惠券结束时间")
    private Integer endTime;

    /** 优惠券类型 0通用券 1商品券 2内部券*/
    @ApiModelProperty(value = "优惠券类型 0通用券 1商品券 2内部券")
    private Integer type;

    /**优惠产品ID*/
    @ApiModelProperty(value = "优惠产品ID")
    private String productId;

    /**优惠产品ID*/
    @ApiModelProperty(value = "是否能使用")
    private Boolean isUse;

    private Integer categoryId;
    private String categoryName;

    private Integer timeType;
    private Integer timeNum;




}
