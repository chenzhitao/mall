package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
@Repository
public interface YxStoreProductMapper extends BaseMapper<YxStoreProduct> {

    @Update("update yx_store_product set stock=stock-#{num}, sales=sales+#{num}" +
            " where id=#{productId}")
    int decStockIncSales(@Param("num") int num,@Param("productId") int productId);

    @Update("update yx_store_product set stock=stock+#{num}, sales=sales-#{num}" +
            " where id=#{productId}")
    int incStockDecSales(@Param("num") int num,@Param("productId") int productId);

    @Update("update yx_store_product set sales=sales+#{num}" +
            " where id=#{productId}")
    int incSales(@Param("num") int num,@Param("productId") int productId);

    @Update("update yx_store_product set sales=sales-#{num}" +
            " where id=#{productId}")
    int decSales(@Param("num") int num,@Param("productId") int productId);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductQueryVo getYxStoreProductById(Serializable id);


    List<YxStoreProductQueryVo> getYxStoreProductByCateId(Serializable cateId);

    List<YxStoreProductQueryVo> getYxStoreProductByCateIds(@Param("cateId") String cateId);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreProductQueryParam
     * @return
     */
    IPage<YxStoreProductQueryVo> getYxStoreProductPageList(@Param("page") Page page, @Param("param") YxStoreProductQueryParam yxStoreProductQueryParam);

    List<YxStoreProductQueryVo> getYxStoreProductByTemplateId(Serializable cateId);

    List<YxStoreProductQueryVo> getYxStoreProductByVideoId(Serializable cateId);
}
