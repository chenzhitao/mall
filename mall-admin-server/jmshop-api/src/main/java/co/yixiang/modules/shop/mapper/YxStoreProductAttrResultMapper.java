package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreProductAttrResult;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrResultQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrResultQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 商品属性详情表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Repository
public interface YxStoreProductAttrResultMapper extends BaseMapper<YxStoreProductAttrResult> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductAttrResultQueryVo getYxStoreProductAttrResultById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreProductAttrResultQueryParam
     * @return
     */
    IPage<YxStoreProductAttrResultQueryVo> getYxStoreProductAttrResultPageList(@Param("page") Page page, @Param("param") YxStoreProductAttrResultQueryParam yxStoreProductAttrResultQueryParam);

}
