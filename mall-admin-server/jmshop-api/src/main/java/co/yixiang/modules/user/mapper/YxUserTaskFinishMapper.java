package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUserTaskFinish;
import co.yixiang.modules.user.web.param.YxUserTaskFinishQueryParam;
import co.yixiang.modules.user.web.vo.YxUserTaskFinishQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 用户任务完成记录表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-07
 */
@Repository
public interface YxUserTaskFinishMapper extends BaseMapper<YxUserTaskFinish> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserTaskFinishQueryVo getYxUserTaskFinishById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserTaskFinishQueryParam
     * @return
     */
    IPage<YxUserTaskFinishQueryVo> getYxUserTaskFinishPageList(@Param("page") Page page, @Param("param") YxUserTaskFinishQueryParam yxUserTaskFinishQueryParam);

}
