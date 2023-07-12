package co.yixiang.grab.tmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : godly.strong
 * mail : huangjunquan1109@163.com
 * @since : 2021/3/30 17:47
 * describe ：商品SKU
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommoditySkuVO
{

    /**
     * SKU ID
     */
    private String skuId;

    /**
     * SKU名称
     */
    private String skuName;

    /**
     * SKU价格
     */
    private Double skuPrice;

    /**
     * SKU主图
     */
    private String skuImg;

    /**
     * SKU库存
     */
    private Integer inventory;

}
