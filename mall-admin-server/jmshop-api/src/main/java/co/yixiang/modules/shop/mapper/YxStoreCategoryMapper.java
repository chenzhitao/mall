package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreCategory;
import co.yixiang.modules.shop.web.param.YxStoreCategoryQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCategoryQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 商品分类表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-22
 */
@Repository
public interface YxStoreCategoryMapper extends BaseMapper<YxStoreCategory> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCategoryQueryVo getYxStoreCategoryById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreCategoryQueryParam
     * @return
     */
    IPage<YxStoreCategoryQueryVo> getYxStoreCategoryPageList(@Param("page") Page page, @Param("param") YxStoreCategoryQueryParam yxStoreCategoryQueryParam);

}
