package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 优惠券前台领取表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-27
 */
@Data
@ApiModel(value = "YxStoreCouponIssueQueryVo对象", description = "优惠券前台领取表查询参数")
public class YxStoreCouponIssueQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "优惠券ID")
    private Integer cid;

    @ApiModelProperty(value = "优惠券名称")
    private String cname;

    @ApiModelProperty(value = "优惠券领取开启时间")
    private Integer startTime;

    @ApiModelProperty(value = "优惠券领取结束时间")
    private Integer endTime;

    @ApiModelProperty(value = "优惠券领取数量")
    private Integer totalCount;

    @ApiModelProperty(value = "优惠券剩余领取数量")
    private Integer remainCount;

    @ApiModelProperty(value = "是否无限张数")
    private Integer isPermanent;

    @ApiModelProperty(value = "1 正常 0 未开启 -1 已无效")
    private Integer status;

    private Double couponPrice;

    private Double useMinPrice;

    private Boolean isUse;

    private Integer categoryId;
    private String categoryName;

    private Integer timeType;
    private Integer timeNum;

}
