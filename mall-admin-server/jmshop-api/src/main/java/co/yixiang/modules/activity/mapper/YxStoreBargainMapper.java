package co.yixiang.modules.activity.mapper;

import co.yixiang.modules.activity.entity.YxStoreBargain;
import co.yixiang.modules.activity.web.param.YxStoreBargainQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 砍价表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
@Repository
public interface YxStoreBargainMapper extends BaseMapper<YxStoreBargain> {

    @Update("update yx_store_bargain set stock=stock+#{num}, sales=sales-#{num}" +
            " where id=#{bargainId}")
    int incStockDecSales(@Param("num") int num,@Param("bargainId") int bargainId);

    @Update("update yx_store_bargain set stock=stock-#{num}, sales=sales+#{num}" +
            " where id=#{bargainId}")
    int decStockIncSales(@Param("num") int num,@Param("bargainId") int bargainId);

    @Select("SELECT c.id,c.image,c.min_price as price,c.price as otPrice," +
            "c.title as storeName,c.status as isShow,c.cost," +
            "c.is_postage as isPostage,c.postage,c.sales,c.stock,c.is_del as isDel" +
            " FROM yx_store_bargain c " +
            " WHERE c.id = #{id} ")
    YxStoreProductQueryVo bargainInfo(int id);

    @Select("select IFNULL(sum(look),0)" +
            "from yx_store_bargain")
    int lookCount();

    @Select("select IFNULL(sum(share),0) as shareCount " +
            "from yx_store_bargain")
    int shareCount();

    @Update("update yx_store_bargain set share=share+1" +
            " where id=#{id}")
    int addBargainShare(@Param("id") int id);

    @Update("update yx_store_bargain set look=look+1" +
            " where id=#{id}")
    int addBargainLook(@Param("id") int id);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreBargainQueryVo getYxStoreBargainById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreBargainQueryParam
     * @return
     */
    IPage<YxStoreBargainQueryVo> getYxStoreBargainPageList(@Param("page") Page page, @Param("param") YxStoreBargainQueryParam yxStoreBargainQueryParam);

}
