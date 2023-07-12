package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品点赞和收藏表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-23
 */
@Data
@ApiModel(value = "YxStoreProductRelationQueryVo对象", description = "商品点赞和收藏表查询参数")
public class YxStoreProductRelationQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "类型(收藏(collect）、点赞(like))")
    private String type;

    @ApiModelProperty(value = "某种类型的商品(普通商品、秒杀商品)")
    private String category;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    private String image;

    private Integer isDel;

    private Integer isShow;

    private Double otPrice;

    private Integer pid;

    private Double price;

    private Integer sales;

    private String storeName;

}