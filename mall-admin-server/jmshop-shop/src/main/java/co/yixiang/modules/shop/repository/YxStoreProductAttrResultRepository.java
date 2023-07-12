package co.yixiang.modules.shop.repository;

import co.yixiang.modules.shop.domain.YxStoreProductAttrResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
* @author hupeng
* @date 2019-10-13
*/
public interface YxStoreProductAttrResultRepository extends JpaRepository<YxStoreProductAttrResult, Integer>, JpaSpecificationExecutor {

    /**
     * findByProductId
     * @param product_id
     * @return
     */
    YxStoreProductAttrResult findByProductId(Integer product_id);

    @Modifying
    @Transactional
    @Query(value = "delete from yx_store_product_attr_result where product_id =?1",nativeQuery = true)
    void deleteByProductId(Integer product_id);
}
