package co.yixiang.modules.activity.repository;

import co.yixiang.modules.activity.domain.YxStoreDiscount;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
* @author xuwenbo
* @date 2019-12-22
*/
public interface YxStoreDiscountRepository extends JpaRepository<YxStoreDiscount, Integer>, JpaSpecificationExecutor {

    @Query(value = "SELECT id, product_id, start_time, stop_time, grade, discount, title, info, json_str, start_time_date, end_time_date, add_time, sort, status, is_del  FROM yx_store_discount where product_id = ?1 and grade = ?2 and start_time <= ?3 and stop_time >= ?3 and is_del = 0 and status = 1 order by add_time desc limit 1 ",nativeQuery = true)
    List<YxStoreDiscount> findByProductIdAndGrade(Integer id, Integer grade, Integer time);

    @Query(value = "SELECT id, product_id, start_time, stop_time, grade, discount, title, info, json_str, start_time_date, end_time_date, add_time, sort, status, is_del  FROM yx_store_discount where product_id = ?1 and start_time_date <= now() and end_time_date >= now() and is_del = 0 and status = 1 order by add_time desc limit 1 ",nativeQuery = true)
    Optional<YxStoreDiscount> findByProductId(Integer id);
}