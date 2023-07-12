package co.yixiang.modules.order.mapper;

import co.yixiang.modules.manage.web.dto.ChartDataDTO;
import co.yixiang.modules.manage.web.dto.OrderDataDTO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.web.param.YxStoreOrderQueryParam;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Repository
public interface YxStoreOrderMapper extends BaseMapper<YxStoreOrder> {

    @Select("SELECT sum(pay_price) as num," +
            "FROM_UNIXTIME(add_time, '%m-%d') as time " +
            " FROM yx_store_order ${ew.customSqlSegment} " +
            " GROUP BY FROM_UNIXTIME(add_time,'%Y-%m-%d') " +
            " ORDER BY add_time ASC")
    List<ChartDataDTO> chartList(@Param(Constants.WRAPPER) Wrapper<YxStoreOrder> wrapper);

    @Select("SELECT count(id) as num," +
            "FROM_UNIXTIME(add_time, '%m-%d') as time " +
            " FROM yx_store_order ${ew.customSqlSegment} " +
            " GROUP BY FROM_UNIXTIME(add_time,'%Y-%m-%d') " +
            " ORDER BY add_time ASC")
    List<ChartDataDTO> chartListT(@Param(Constants.WRAPPER) Wrapper<YxStoreOrder> wrapper);

    @Select("SELECT sum(pay_price) as price,count(id) as count," +
            "FROM_UNIXTIME(add_time, '%m-%d') as time FROM yx_store_order" +
            " WHERE is_del = 0 AND paid = 1 AND refund_status = 0 " +
            "GROUP BY FROM_UNIXTIME(add_time,'%Y-%m-%d') ORDER BY add_time DESC")
    List<OrderDataDTO> getOrderDataPriceList(Page page);


    @Select("SELECT IFNULL(sum(pay_price),0) " +
            " FROM yx_store_order ${ew.customSqlSegment}")
    Double todayPrice(@Param(Constants.WRAPPER) Wrapper<YxStoreOrder> wrapper);



    @Select("select IFNULL(sum(pay_price),0) from yx_store_order " +
            "where paid=1 and is_del=0 and refund_status=0 and uid=#{uid}")
    double sumPrice(@Param("uid") int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreOrderQueryVo getYxStoreOrderById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreOrderQueryParam
     * @return
     */
    IPage<YxStoreOrderQueryVo> getYxStoreOrderPageList(@Param("page") Page page, @Param("param") YxStoreOrderQueryParam yxStoreOrderQueryParam);

}
