package co.yixiang.modules.shop.repository;

import co.yixiang.modules.shop.domain.YxStoreCategory;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import co.yixiang.modules.shop.service.dto.YxStoreProductDTO;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
* @author hupeng
* @date 2019-10-04
*/
public interface YxStoreProductRepository extends JpaRepository<YxStoreProduct, Integer>, JpaSpecificationExecutor {
    @Modifying
    @Transactional
    @Query(value = "update yx_store_product set is_show = ?1 where id = ?2",nativeQuery = true)
    void updateOnsale(int status, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update yx_store_product set is_del = ?1 where id = ?2",nativeQuery = true)
    void updateDel(int status, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update yx_store_product set price = ?1 where id = ?2",nativeQuery = true)
    void updatePrice(BigDecimal price, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update yx_store_product set  stock= ?1 where id = ?2",nativeQuery = true)
    void updateStock(int stock, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update yx_store_product set " +
            "STORE_NAME=:#{#pro.storeName}, " +
            "KEYWORD=:#{#pro.keyword}, " +
            "PACKAGING=:#{#pro.packaging}, " +
            "UNIT_NAME=:#{#pro.unitName}, " +
            "BAR_CODE=:#{#pro.barCode}, " +
            "PRICE=:#{#pro.price}, " +
            "DESCRIPTION=:#{#pro.description}, " +
            "OT_PRICE=:#{#pro.otPrice}, " +
            "COST=:#{#pro.cost}, " +
            "POSTAGE=:#{#pro.postage}, " +
            "SORT=:#{#pro.sort}, " +
            "SALES=:#{#pro.sales}, " +
            "STOCK=:#{#pro.stock}, " +
            "IS_HOT=:#{#pro.isHot}, " +
            "IS_BENEFIT=:#{#pro.isBenefit}, " +
            "IS_BEST=:#{#pro.isBest}, " +
            "IS_NEW=:#{#pro.isNew}, " +
            "IS_RANK=:#{#pro.isRank}, " +
            "IS_POSTAGE=:#{#pro.isPostage}, " +
            "IS_GOOD=:#{#pro.isGood}, " +
            "GIVE_INTEGRAL=:#{#pro.giveIntegral}, " +
            "FICTI=:#{#pro.ficti}, " +
            "IS_SHOW=:#{#pro.isShow},  " +
            "CATE_ID=:#{#pro.storeCategory.id}  " +
            "where ID=:#{#pro.id}",nativeQuery = true)
    void updatePrice(@Param("pro") YxStoreProduct yxStoreProduct);

    List<YxStoreProduct> findByStoreCategoryAndIsDel(YxStoreCategory storeCategory,int isDel);

    @Query(value = "SELECT *  FROM yx_store_product where id in ?1",nativeQuery = true)
    List<YxStoreProduct> findByIds(List<String> idList);
}
