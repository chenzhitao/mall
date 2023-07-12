package co.yixiang.modules.activity.entity;

import java.math.BigDecimal;

import co.yixiang.serializer.BigDecimalSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 拼团表
 * </p>
 *
 * @author hupeng
 * @since 2019-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStorePink对象", description = "拼团表")
public class YxStorePink extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "订单id 生成")
    private String orderId;

    @ApiModelProperty(value = "订单id  数据库")
    private Integer orderIdKey;

    @ApiModelProperty(value = "购买商品个数")
    private Integer totalNum;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "购买总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "拼团产品id")
    private Integer cid;

    @ApiModelProperty(value = "产品id")
    private Integer pid;

    @ApiModelProperty(value = "拼图总人数")
    private Integer people;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "拼团产品单价")
    private BigDecimal price;

    @ApiModelProperty(value = "开始时间")
    private String addTime;

    private String stopTime;

    @ApiModelProperty(value = "团长id 0为团长")
    private Integer kId;

    @ApiModelProperty(value = "是否发送模板消息0未发送1已发送")
    private Integer isTpl;

    @ApiModelProperty(value = "是否退款 0未退款 1已退款")
    private Integer isRefund;

    @ApiModelProperty(value = "状态1进行中2已完成3未完成")
    private Integer status;

}
