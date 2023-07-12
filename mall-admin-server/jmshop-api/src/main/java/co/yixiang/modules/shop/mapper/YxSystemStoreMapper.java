package co.yixiang.modules.shop.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxSystemStore;
import co.yixiang.modules.shop.web.param.YxSystemStoreQueryParam;
import co.yixiang.modules.shop.web.vo.YxSystemStoreQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 门店自提 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-03-04
 */
@Repository
public interface YxSystemStoreMapper extends BaseMapper<YxSystemStore> {
    @Select("SELECT*,ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((#{lat} * PI() / 180 - latitude * PI() / 180" +
            "    ) / 2),2) + COS(40.0497810000 * PI() / 180) * COS(latitude * PI() / 180) * POW(" +
            "    SIN((#{lon} * PI() / 180 - longitude * PI() / 180) / 2),2))) * 1000) AS distance" +
            "    FROM yx_system_store WHERE is_del=0 AND is_show = 1 ORDER BY distance ASC"
            )
    List<YxSystemStoreQueryVo> getStoreList(Page page,@Param("lon") double lon,@Param("lat") double lat);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemStoreQueryVo getYxSystemStoreById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxSystemStoreQueryParam
     * @return
     */
    IPage<YxSystemStoreQueryVo> getYxSystemStorePageList(@Param("page") Page page, @Param("param") YxSystemStoreQueryParam yxSystemStoreQueryParam);

}
