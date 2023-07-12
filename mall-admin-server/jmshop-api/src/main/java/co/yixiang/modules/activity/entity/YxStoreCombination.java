package co.yixiang.modules.activity.entity;

import java.math.BigDecimal;

import co.yixiang.serializer.BigDecimalSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 拼团产品表
 * </p>
 *
 * @author hupeng
 * @since 2019-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreCombination对象", description = "拼团产品表")
public class YxStoreCombination extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "推荐图")
    private String image;

    @ApiModelProperty(value = "轮播图")
    private String images;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动属性")
    private String attr;

    @ApiModelProperty(value = "参团人数")
    private Integer people;

    @ApiModelProperty(value = "简介")
    private String info;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "添加时间")
    private String addTime;

    @ApiModelProperty(value = "推荐")
    private Integer isHost;

    @ApiModelProperty(value = "产品状态")
    private Integer isShow;

    private Integer isDel;

    private Integer combination;

    @ApiModelProperty(value = "商户是否可用1可用0不可用")
    private Integer merUse;

    @ApiModelProperty(value = "是否包邮1是0否")
    private Integer isPostage;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "邮费")
    private BigDecimal postage;

    @ApiModelProperty(value = "拼团内容")
    private String description;

    @ApiModelProperty(value = "拼团开始时间")
    private Integer startTime;

    @ApiModelProperty(value = "拼团结束时间")
    private Integer stopTime;

    @ApiModelProperty(value = "拼团订单有效时间")
    private Integer effectiveTime;

    @ApiModelProperty(value = "拼图产品成本")
    private Integer cost;

    @ApiModelProperty(value = "浏览量")
    private Integer browse;

    @ApiModelProperty(value = "单位名")
    private String unitName;

    private Date endTimeDate;

    private Date startTimeDate;

}
