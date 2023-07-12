package co.yixiang.modules.user.mapper;

import co.yixiang.modules.user.web.dto.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.web.param.YxUserBillQueryParam;
import co.yixiang.modules.user.web.vo.YxUserBillQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户账单表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Repository
public interface YxUserBillMapper extends BaseMapper<YxUserBill> {

    @Select("select IFNULL(sum(number),0) from yx_user_bill " +
            "where status=1 and type='sign' and pm=1 and category='integral' " +
            "and uid=#{uid}")
    double sumIntegral(@Param("uid") int uid);

    @Select("SELECT FROM_UNIXTIME(a.add_time,'%Y-%m-%d') as addTime,a.title,a.number " +
            "FROM yx_user_bill a INNER JOIN yx_user u ON u.uid=a.uid WHERE a.category = 'integral'" +
            " AND a.type = 'sign' AND a.status = 1 AND a.uid = #{uid} " +
            "ORDER BY a.add_time DESC")
    List<SignDTO> getSignList(@Param("uid") int uid, Page page);

    @Select("SELECT o.order_id as orderId,FROM_UNIXTIME(b.add_time, '%Y-%m-%d %H:%i') as time," +
            "b.number,u.avatar,u.nickname FROM yx_user_bill b " +
            "INNER JOIN yx_store_order o ON o.id=b.link_id " +
            "RIGHT JOIN yx_user u ON u.uid=o.uid" +
            " WHERE b.uid = #{uid} AND ( FROM_UNIXTIME(b.add_time, '%Y-%m')= #{time} ) AND " +
            "b.category = 'now_money' AND b.type = 'brokerage' ORDER BY time DESC")
    List<BillOrderRecordDTO> getBillOrderRList(@Param("time") String time, @Param("uid") int uid);

    @Select("SELECT FROM_UNIXTIME(add_time,'%Y-%m') as time " +
            " FROM yx_user_bill ${ew.customSqlSegment}")
    List<String> getBillOrderList(@Param(Constants.WRAPPER) Wrapper<YxUserBill> userWrapper,Page page);

    @Select("SELECT FROM_UNIXTIME(add_time,'%Y-%m') as time,group_concat(id SEPARATOR ',') ids " +
            " FROM yx_user_bill ${ew.customSqlSegment}")
    List<BillDTO> getBillList(@Param(Constants.WRAPPER) Wrapper<YxUserBill> userWrapper,Page page);

    @Select("SELECT FROM_UNIXTIME(add_time,'%Y-%m-%d %H:%i') as add_time,title,number,pm " +
            " FROM yx_user_bill ${ew.customSqlSegment}")
    List<UserBillDTO> getUserBillList(@Param(Constants.WRAPPER) Wrapper<YxUserBill> userWrapper);

    @Select("select IFNULL(sum(number),0) from yx_user_bill " +
            "where status=1 and type='brokerage' and pm=1 and category='now_money' " +
            "and uid=#{uid}")
    double sumPrice(@Param("uid") int uid);


    @Select("select IFNULL(sum(number),0) from yx_user_bill " +
            "where status=1 and type='brokerage' and pm=1 and category='now_money' " +
            "and uid=#{uid} and TO_DAYS(NOW()) - TO_DAYS(add_time) <= 1")
    double sumYesterdayPrice(@Param("uid") int uid);


    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserBillQueryVo getYxUserBillById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserBillQueryParam
     * @return
     */
    IPage<YxUserBillQueryVo> getYxUserBillPageList(@Param("page") Page page, @Param("param") YxUserBillQueryParam yxUserBillQueryParam);

}
