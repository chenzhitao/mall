package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.modules.user.web.param.YxSystemUserLevelQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserLevelQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 设置用户等级表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Repository
public interface YxSystemUserLevelMapper extends BaseMapper<YxSystemUserLevel> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemUserLevelQueryVo getYxSystemUserLevelById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxSystemUserLevelQueryParam
     * @return
     */
    IPage<YxSystemUserLevelQueryVo> getYxSystemUserLevelPageList(@Param("page") Page page, @Param("param") YxSystemUserLevelQueryParam yxSystemUserLevelQueryParam);

}
