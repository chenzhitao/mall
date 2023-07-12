package co.yixiang.modules.activity.web.vo;

import co.yixiang.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户参与砍价表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-12-21
 */
@Data
@ApiModel(value = "YxStoreBargainUserQueryVo对象", description = "用户参与砍价表查询参数")
public class YxStoreBargainUserQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户参与砍价表ID")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "砍价产品id")
    private Integer bargainId;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "砍价的最低价")
    private BigDecimal bargainPriceMin;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "砍价金额")
    private BigDecimal bargainPrice;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "砍掉的价格")
    private BigDecimal price;

    @ApiModelProperty(value = "状态 1参与中 2 活动结束参与失败 3活动结束参与成功")
    private Integer status;

    @ApiModelProperty(value = "参与时间")
    private Integer addTime;

    @ApiModelProperty(value = "是否取消")
    private Integer isDel;

    private Double residuePrice;

    private String title;

    private String image;

    private Integer datatime;

}
