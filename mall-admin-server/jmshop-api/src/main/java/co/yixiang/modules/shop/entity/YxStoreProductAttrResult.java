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
 * 商品属性详情表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxStoreProductAttrResult对象", description="商品属性详情表")
public class YxStoreProductAttrResult extends BaseEntity {

    private static final long serialVersionUID = 1L;

@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "商品ID")
private Integer productId;

@ApiModelProperty(value = "商品属性参数")
private String result;

@ApiModelProperty(value = "上次修改时间")
private Integer changeTime;

}
