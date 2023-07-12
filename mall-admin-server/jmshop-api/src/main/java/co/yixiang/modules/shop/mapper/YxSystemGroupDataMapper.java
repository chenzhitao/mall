package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxSystemGroupData;
import co.yixiang.modules.shop.web.param.YxSystemGroupDataQueryParam;
import co.yixiang.modules.shop.web.vo.YxSystemGroupDataQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 组合数据详情表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
@Repository
public interface YxSystemGroupDataMapper extends BaseMapper<YxSystemGroupData> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemGroupDataQueryVo getYxSystemGroupDataById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxSystemGroupDataQueryParam
     * @return
     */
    IPage<YxSystemGroupDataQueryVo> getYxSystemGroupDataPageList(@Param("page") Page page, @Param("param") YxSystemGroupDataQueryParam yxSystemGroupDataQueryParam);

}
