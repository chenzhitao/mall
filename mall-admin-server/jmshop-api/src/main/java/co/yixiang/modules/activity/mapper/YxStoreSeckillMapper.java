package co.yixiang.modules.activity.mapper;

import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.activity.entity.YxStoreSeckill;
import co.yixiang.modules.activity.web.param.YxStoreSeckillQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreSeckillQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品秒杀产品表 Mapper 接口
 * </p>
 *
 * @author xuwenbo
 * @since 2019-12-14
 */
@Repository
public interface YxStoreSeckillMapper extends BaseMapper<YxStoreSeckill> {

    @Update("update yx_store_seckill set stock=stock+#{num}, sales=sales-#{num}" +
            " where id=#{seckillId}")
    int incStockDecSales(@Param("num") int num,@Param("seckillId") int seckillId);

    @Update("update yx_store_seckill set stock=stock-#{num}, sales=sales+#{num}" +
            " where id=#{seckillId}")
    int decStockIncSales(@Param("num") int num,@Param("seckillId") int seckillId);

    @Select("SELECT c.id,c.image,c.price,c.title as storeName,c.is_show as isShow,c.cost," +
            "c.is_postage as isPostage,c.postage,c.sales,c.stock,c.is_del as isDel" +
            " FROM yx_store_seckill c " +
            " WHERE c.id = #{id} ")
    YxStoreProductQueryVo seckillInfo(int id);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreSeckillQueryVo getYxStoreSeckillById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreSeckillQueryParam
     * @return
     */
    IPage<YxStoreSeckillQueryVo> getYxStoreSeckillPageList(@Param("page") Page page, @Param("param") YxStoreSeckillQueryParam yxStoreSeckillQueryParam);

    @Select("select t.id, t.image, t.images, t.title, t.info, t.price, t.cost, t.sort, t.stock, t.sales, " +
            "t.postage, t.description,  t.status, t.num from yx_store_seckill t" +
            "INNER JOIN yx_store_product s ON s.id=t.product_id " +
            "WHERE t.is_show = 1 AND t.is_del = 0 AND t.start_time < unix_timestamp(now()) " +
            "AND t.stop_time > unix_timestamp(now()) ORDER BY t.sort desc,t.id desc")
    List<YxStoreSeckillQueryVo> getCombList(Page<YxStoreSeckill> pageModel, @Param("time") String time);
}
