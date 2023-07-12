package co.yixiang.modules.activity.repository;

import co.yixiang.modules.activity.domain.YxStoreCouponIssue;
import co.yixiang.modules.shop.domain.YxUserLevel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
* @author hupeng
* @date 2019-11-09
*/
public interface YxStoreCouponIssueRepository extends JpaRepository<YxStoreCouponIssue, Integer>, JpaSpecificationExecutor {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update yx_store_coupon_issue set remain_count=remain_count-1 where id= ?1",nativeQuery = true)
    int decCount(int id);

    @Query(value = "select * from  yx_store_coupon_issue where id = ?1",nativeQuery = true)
    YxStoreCouponIssue getYxStoreCouponIssueById(Integer id);
}
