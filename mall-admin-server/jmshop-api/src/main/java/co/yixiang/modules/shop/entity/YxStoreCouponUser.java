package co.yixiang.modules.shop.entity;

import co.yixiang.common.entity.BaseEntity;
import co.yixiang.serializer.BigDecimalSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 优惠券发放记录表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreCouponUser对象", description = "优惠券发放记录表")
public class YxStoreCouponUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "优惠券发放记录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "兑换的项目id")
    private Integer cid;

    @ApiModelProperty(value = "优惠券所属用户")
    private Integer uid;

    @ApiModelProperty(value = "优惠券名称")
    private String couponTitle;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "优惠券的面值")
    private BigDecimal couponPrice;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "最低消费多少金额可用优惠券")
    private BigDecimal useMinPrice;

    @ApiModelProperty(value = "优惠券创建时间")
    private Integer addTime;

    @ApiModelProperty(value = "优惠券结束时间")
    private Integer endTime;

    @ApiModelProperty(value = "使用时间")
    private Integer useTime;

    @ApiModelProperty(value = "获取方式")
    private String type;

    @ApiModelProperty(value = "状态（0：未使用，1：已使用, 2:已过期）")
    private Integer status;

    @ApiModelProperty(value = "是否有效")
    private Integer isFail;

    @ApiModelProperty(value = "商品分类ID")
    private Integer categoryId;

    @ApiModelProperty(value = "商品分类描述")
    private String categoryName;

    @ApiModelProperty(value = "优惠券有效期类型")
    private Integer timeType;

    @ApiModelProperty(value = "优惠券有效期期限")
    private Integer timeNum;



}
