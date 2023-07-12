package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxSystemUserTask;
import co.yixiang.modules.user.web.param.YxSystemUserTaskQueryParam;
import co.yixiang.modules.user.web.vo.YxSystemUserTaskQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 等级任务设置 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Repository
public interface YxSystemUserTaskMapper extends BaseMapper<YxSystemUserTask> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemUserTaskQueryVo getYxSystemUserTaskById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxSystemUserTaskQueryParam
     * @return
     */
    IPage<YxSystemUserTaskQueryVo> getYxSystemUserTaskPageList(@Param("page") Page page, @Param("param") YxSystemUserTaskQueryParam yxSystemUserTaskQueryParam);

}
