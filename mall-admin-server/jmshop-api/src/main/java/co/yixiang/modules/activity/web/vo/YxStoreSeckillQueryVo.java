package co.yixiang.modules.activity.web.vo;

import cn.hutool.core.util.StrUtil;
import co.yixiang.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 商品秒杀产品表 查询结果对象
 * </p>
 *
 * @author xuwenbo
 * @date 2019-12-14
 */
@Data
@ApiModel(value="YxStoreSeckillQueryVo对象", description="商品秒杀产品表查询参数")
public class YxStoreSeckillQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品秒杀产品表id")
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "推荐图")
    private String image;

    @ApiModelProperty(value = "轮播图")
    private String images;

    private List<String> sliderImageArr;

    public List<String> getSliderImageArr() {
        if(StrUtil.isNotEmpty(images)){
            return Arrays.asList(images.split(","));
        }else {
            return new ArrayList<>();
        }

    }

    @ApiModelProperty(value = "轮播图")
    private String[] pics;
    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "简介")
    private String info;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "成本")
    private BigDecimal cost;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "原价")
    private BigDecimal otPrice;

    @ApiModelProperty(value = "返多少积分")
    private BigDecimal giveIntegral;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "单位名")
    private String unitName;

    @ApiModelProperty(value = "邮费")
    private BigDecimal postage;

    @ApiModelProperty(value = "内容")
    private String description;

    @ApiModelProperty(value = "开始时间")
    private Integer startTime;

    @ApiModelProperty(value = "结束时间")
    private Integer stopTime;

    @ApiModelProperty(value = "添加时间")
    private String addTime;

    @ApiModelProperty(value = "产品状态")
    private Integer status;

    @ApiModelProperty(value = "是否包邮")
    private Integer isPostage;

    @ApiModelProperty(value = "热门推荐")
    private Integer isHot;

    @ApiModelProperty(value = "删除 0未删除1已删除")
    private Integer isDel;

    @ApiModelProperty(value = "最多秒杀几个")
    private Integer num;

    @ApiModelProperty(value = "显示")
    private Integer isShow;

    private Integer percent; //百分比



}
