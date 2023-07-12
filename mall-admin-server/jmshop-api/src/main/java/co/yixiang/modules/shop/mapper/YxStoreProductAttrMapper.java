package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreProductAttr;
import co.yixiang.modules.shop.web.param.YxStoreProductAttrQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductAttrQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 商品属性表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Repository
public interface YxStoreProductAttrMapper extends BaseMapper<YxStoreProductAttr> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreProductAttrQueryVo getYxStoreProductAttrById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreProductAttrQueryParam
     * @return
     */
    IPage<YxStoreProductAttrQueryVo> getYxStoreProductAttrPageList(@Param("page") Page page, @Param("param") YxStoreProductAttrQueryParam yxStoreProductAttrQueryParam);

}
