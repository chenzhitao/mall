package co.yixiang.modules.shop.repository;

import co.yixiang.modules.shop.domain.YxStoreProductAttrValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
* @author hupeng
* @date 2019-10-13
*/
public interface YxStoreProductAttrValueRepository extends JpaRepository<YxStoreProductAttrValue, Integer>, JpaSpecificationExecutor {

    @Modifying
    @Transactional
    @Query(value = "delete from yx_store_product_attr_value where product_id =?1",nativeQuery = true)
    void deleteByProductId(Integer id);

    @Query(value = "select sum(stock)  from yx_store_product_attr_value " +
            "where product_id = ?1",nativeQuery = true)
    Integer sumStock(Integer productId);

    @Query(value = "select min(price)  from yx_store_product_attr_value " +
            "where product_id = ?1",nativeQuery = true)
    BigDecimal lowestPrice(Integer productId);

    @Query(value = "select *  from yx_store_product_attr_value t " +
            " where t.unique = ?1",nativeQuery = true)
    YxStoreProductAttrValue findbyUnique(String unique);


    @Query(value = "select *  from yx_store_product_attr_value t  where t.product_id = ?1",nativeQuery = true)
    List<YxStoreProductAttrValue> findByProductId(Integer productId);

    /**
     * 根据商品ID和sku查询商品属性值
     * @param productId
     * @param suk
     * @return
     */
    YxStoreProductAttrValue findByProductIdAndSuk(Integer productId,String suk);

}
