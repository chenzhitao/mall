package co.yixiang.modules.shop.repository;

import co.yixiang.modules.shop.domain.YxStoreCategory;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
* @author hupeng
* @date 2019-10-03
*/
public interface YxStoreCategoryRepository extends JpaRepository<YxStoreCategory, Integer>, JpaSpecificationExecutor {
    @Query(value = "select cate_name from yx_store_category where id = ?1",nativeQuery = true)
    String findNameById(Integer id);

    @Modifying
    @Query("update YxStoreCategory s set s.isDel = 1 where s.id =:id")
    void delCategory(Integer id);

    YxStoreCategory findByPid(Integer pid);

    @Query(value = "select id,pid,cate_name,sort,pic,is_show,add_time,is_del from yx_store_category where cate_name = :name and is_del = 0 ORDER BY add_time DESC LIMIT 1 ",nativeQuery = true)
    YxStoreCategory findByName(String name);

    @Query(value = "select id,pid,cate_name,sort,pic,is_show,add_time,is_del from yx_store_category where id = getGrpTopId(:id) and is_del = 0 ",nativeQuery = true)
    YxStoreCategory findParentById(Integer id);

    @Query(value = "select id,pid,cate_name,sort,pic,is_show,add_time,is_del from yx_store_category where FIND_IN_SET(id,getGrpParIds(:id)) and is_del = 0 ",nativeQuery = true)
    List<YxStoreCategory> findParentsById(Integer id);


    @Query(value = "select id,pid,cate_name,sort,pic,is_show,add_time,is_del from yx_store_category  where pid =:id ",nativeQuery = true)
    List<YxStoreCategory> findParentsByList(Integer id);

}
