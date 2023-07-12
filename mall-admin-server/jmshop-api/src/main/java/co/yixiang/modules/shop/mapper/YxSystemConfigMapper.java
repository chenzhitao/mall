package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxSystemConfig;
import co.yixiang.modules.shop.web.param.YxSystemConfigQueryParam;
import co.yixiang.modules.shop.web.vo.YxSystemConfigQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 配置表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
@Repository
public interface YxSystemConfigMapper extends BaseMapper<YxSystemConfig> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemConfigQueryVo getYxSystemConfigById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxSystemConfigQueryParam
     * @return
     */
    IPage<YxSystemConfigQueryVo> getYxSystemConfigPageList(@Param("page") Page page, @Param("param") YxSystemConfigQueryParam yxSystemConfigQueryParam);

}
