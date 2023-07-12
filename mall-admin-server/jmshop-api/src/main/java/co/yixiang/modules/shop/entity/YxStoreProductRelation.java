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
 * 商品点赞和收藏表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxStoreProductRelation对象", description="商品点赞和收藏表")
public class YxStoreProductRelation extends BaseEntity {

    private static final long serialVersionUID = 1L;

@TableId(value = "id", type = IdType.AUTO)
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

}
