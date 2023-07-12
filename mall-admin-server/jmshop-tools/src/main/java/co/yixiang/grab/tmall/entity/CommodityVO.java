package co.yixiang.grab.tmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author : godly.strong
 * mail : huangjunquan1109@163.com
 * @since : 2021/3/30 17:47
 * describe ：商品信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommodityVO
{

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 商品价格
     */
    private Double commodityPrice;

    /**
     * 商品主图
     */
    private String commodityImg;

    /**
     * 商品SKU 集合
     *
     */
    private List<CommoditySkuVO> commoditySkuVOList;

    /**
     * 商品图片 集合
     */
    private List<CommodityImgVO> commodityImgVOList;

    /**
     * 商品详情
     */
    private String commodityDetail;

}
