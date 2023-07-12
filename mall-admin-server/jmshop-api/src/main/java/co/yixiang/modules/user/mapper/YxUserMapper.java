package co.yixiang.modules.user.mapper;

import co.yixiang.modules.user.web.dto.PromUserDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.web.param.YxUserQueryParam;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-16
 */
@Repository
public interface YxUserMapper extends BaseMapper<YxUser> {



    @Select("<script>SELECT u.uid,u.nickname,u.avatar,from_unixtime(u.add_time,'%Y/%m/%d') as time," +
            "u.spread_count as childCount,COUNT(o.id) as orderCount," +
            "IFNULL(SUM(o.pay_price),0) as numberCount FROM yx_user u " +
            "LEFT JOIN yx_store_order o ON u.uid=o.uid " +
            "WHERE u.uid in <foreach item='id' index='index' collection='uids' " +
            " open='(' separator=',' close=')'>" +
            "   #{id}" +
            " </foreach> <if test='keyword != null'>" +
            " AND ( u.nickname LIKE CONCAT(CONCAT('%',#{keyword}),'%') OR u.phone LIKE CONCAT(CONCAT('%',#{keyword}),'%'))</if>" +
            " GROUP BY u.uid ORDER BY #{orderBy} " +
            "</script>")
    List<PromUserDTO> getUserSpreadCountList(Page page,
                                             @Param("uids") List uids,
                                             @Param("keyword") String keyword,
                                             @Param("orderBy") String orderBy);

    @Update("update yx_user set now_money=now_money-#{payPrice}" +
            " where uid=#{uid}")
    int decPrice(@Param("payPrice") double payPrice,@Param("uid") int uid);

    @Update("update yx_user set brokerage_price=brokerage_price+#{brokeragePrice}" +
            " where uid=#{uid}")
    int incBrokeragePrice(@Param("brokeragePrice") double brokeragePrice,@Param("uid") int uid);

    @Update("update yx_user set pay_count=pay_count+1" +
            " where uid=#{uid}")
    int incPayCount(@Param("uid") int uid);

    @Update("update yx_user set now_money=now_money+#{price}" +
            " where uid=#{uid}")
    int incMoney(@Param("uid") int uid,double price);

    @Update("update yx_user set integral=integral-#{integral}" +
            " where uid=#{uid}")
    int decIntegral(@Param("integral") double integral,@Param("uid") int uid);

    @Update("update yx_user set integral=integral+#{integral}" +
            " where uid=#{uid}")
    int incIntegral(@Param("integral") double integral,@Param("uid") int uid);


    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserQueryVo getYxUserById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserQueryParam
     * @return
     */
    IPage<YxUserQueryVo> getYxUserPageList(@Param("page") Page page, @Param("param") YxUserQueryParam yxUserQueryParam);

}
