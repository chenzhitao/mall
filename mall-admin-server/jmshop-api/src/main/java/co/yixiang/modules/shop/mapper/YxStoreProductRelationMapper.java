package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreProductRelation;
import co.yixiang.modules.shop.web.param.YxStoreProductRelationQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductRelationQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品点赞和收藏表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Repository
public interface YxStoreProductRelationMapper extends BaseMapper<YxStoreProductRelation> {

    @Select("select B.id pid,A.category,B.store_name as storeName,B.price," +
            "B.ot_price as otPrice,B.sales,B.image,B.is_del as isDel,B.is_show as isShow" +
            " from yx_store_product_relation A left join yx_store_product B " +
            "on A.product_id = B.id where A.uid=#{uid}")
    List<YxStoreProductRelationQueryVo> selectList(Page page,@Param("uid") int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductRelationQueryVo getYxStoreProductRelationById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreProductRelationQueryParam
     * @return
     */
    IPage<YxStoreProductRelationQueryVo> getYxStoreProductRelationPageList(@Param("page") Page page,
                                                                           @Param("param") YxStoreProductRelationQueryParam yxStoreProductRelationQueryParam);

}
