package co.yixiang.modules.shop.repository;

import co.yixiang.modules.shop.domain.YxUser;
import co.yixiang.modules.shop.domain.YxUserLevel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
* @author hupeng
* @date 2019-10-06
*/
public interface YxUserLevelRepository extends JpaRepository<YxUserLevel, Integer>, JpaSpecificationExecutor {

    @Modifying
    @Query(value = "update yx_user_level set level_id = ?1, grade = ?2, discount = ?3, mark = ?4 where uid = ?5",nativeQuery = true)
    void updateUserLevel(Integer levelId, Integer grade, Integer discount, String mark, Integer uid);


    @Modifying
    @Query(value = "update yx_user_level set discount = ?2 where level_id = ?1",nativeQuery = true)
    void updateUserLevelDiscount(Integer levelId, BigDecimal discount);

    @Query(value = "select id,uid,level_id,grade,valid_time,is_forever,mer_id,status,mark,remind,is_del,add_time,discount from  yx_user_level where is_del = 0 and status = 1 and uid = ?1",nativeQuery = true)
    YxUserLevel selectOne(Integer uid);

    @Query(value = "select id,uid,level_id,grade,valid_time,is_forever,mer_id,status,mark,remind,is_del,add_time,discount from  yx_user_level where is_del = 0 and status = 1 and level_id = ?1",nativeQuery = true)
    List<YxUserLevel> selectOneByLevelid(Integer id);
}
