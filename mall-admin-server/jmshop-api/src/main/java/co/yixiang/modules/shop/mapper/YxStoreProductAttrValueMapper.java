package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrValueQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrValueQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商品属性值表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Repository
public interface YxStoreProductAttrValueMapper extends BaseMapper<YxStoreProductAttrValue> {

    @Select("select sum(stock) from yx_store_product_attr_value " +
            "where product_id = #{productId}")
    Integer sumStock(Integer productId);

    @Select("select min(price) from yx_store_product_attr_value " +
            "where product_id = #{productId}")
    BigDecimal lowestPrice(Integer productId);

    @Select("select max(ot_price) from yx_store_product_attr_value " +
            "where product_id = #{productId}")
    BigDecimal maxOtPrice(Integer productId);

    @Select("select max(price) from yx_store_product_attr_value " +
            "where product_id = #{productId}")
    BigDecimal maxPrice(Integer productId);

    @Update("update yx_store_product_attr_value set stock=stock-#{num}, sales=sales+#{num}" +
            " where product_id=#{productId} and `unique`=#{unique}")
    int decStockIncSales(@Param("num") int num,@Param("productId") int productId,
                 @Param("unique")  String unique);

    @Update("update yx_store_product_attr_value set stock=stock+#{num}, sales=sales-#{num}" +
            " where product_id=#{productId} and `unique`=#{unique}")
    int incStockDecSales(@Param("num") int num,@Param("productId") int productId,
                         @Param("unique")  String unique);



    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductAttrValueQueryVo getYxStoreProductAttrValueById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreProductAttrValueQueryParam
     * @return
     */
    IPage<YxStoreProductAttrValueQueryVo> getYxStoreProductAttrValuePageList(@Param("page") Page page, @Param("param") YxStoreProductAttrValueQueryParam yxStoreProductAttrValueQueryParam);

}
