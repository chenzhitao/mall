package co.yixiang.modules.shop.mapper;

import co.yixiang.enums.CouponEnum;
import co.yixiang.modules.shop.web.vo.StoreCouponUserVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreCouponUser;
import co.yixiang.modules.shop.web.param.YxStoreCouponUserQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponUserQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 优惠券发放记录表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Repository
public interface YxStoreCouponUserMapper extends BaseMapper<YxStoreCouponUser> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    YxStoreCouponUserQueryVo getYxStoreCouponUserById(Serializable id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param yxStoreCouponUserQueryParam
     * @return
     */
    IPage<YxStoreCouponUserQueryVo> getYxStoreCouponUserPageList(@Param("page") Page page, @Param("param") YxStoreCouponUserQueryParam yxStoreCouponUserQueryParam);

    @Select("select A.id,A.coupon_title as couponTitle,A.coupon_price as couponPrice,A.category_id,A.category_name," +
            "A.end_time as endTime,B.use_min_price as useMinPrice,B.type,A.time_type,A.time_num" +
            " from yx_store_coupon_user A left join yx_store_coupon B " +
            "on A.cid = B.id LEFT JOIN yx_store_coupon_issue C ON C.cid = B.id " +
            "where A.status = 0" +
            " AND ((C.start_time < unix_timestamp( now()) AND A.end_time > unix_timestamp(now())) or (A.time_type=1 and A.end_time > unix_timestamp(now())))" +
            " AND A.uid = #{uid} " +
            " ORDER BY B.id DESC")
    List<StoreCouponUserVo> selectCouponList(@Param("now") int now, @Param("price") double price,
                                             @Param("uid") int uid);

    /**
     * 获取可用优惠券
     *
     * @return
     */
    @Select("SELECT " +
            "A.id, " +
            "A.coupon_title AS couponTitle, " +
            "A.coupon_price AS couponPrice, " +
            "A.end_time AS endTime, " +
            "A.category_id AS categoryId,A.time_type,A.time_num, " +
            "A.category_name AS categoryName, " +
            "B.use_min_price AS useMinPrice, " +
            "B.type " +
            "FROM " +
            "yx_store_coupon_user A " +
            "LEFT JOIN yx_store_coupon B ON A.cid = B.id " +
            "LEFT JOIN yx_store_coupon_issue C ON C.cid = B.id " +
            "WHERE " +
            "A. STATUS = 0 " +
            "and A.is_fail = 0 " +
            "AND ((C.start_time < unix_timestamp( now()) AND A.end_time > unix_timestamp(now())) or (A.time_type=1 and A.end_time > unix_timestamp(now())))" +
            "AND A.uid = #{uid} " +
            "ORDER BY " +
            "A.coupon_price DESC ")
    List<YxStoreCouponUser> beUsableCoupon(@Param("now") int now, @Param("uid") int uid);

}
