package co.yixiang.modules.activity.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.activity.entity.YxStoreBargainUserHelp;
import co.yixiang.modules.activity.web.param.YxStoreBargainUserHelpQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainUserHelpQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 砍价用户帮助表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
@Repository
public interface YxStoreBargainUserHelpMapper extends BaseMapper<YxStoreBargainUserHelp> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreBargainUserHelpQueryVo getYxStoreBargainUserHelpById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreBargainUserHelpQueryParam
     * @return
     */
    IPage<YxStoreBargainUserHelpQueryVo> getYxStoreBargainUserHelpPageList(@Param("page") Page page, @Param("param") YxStoreBargainUserHelpQueryParam yxStoreBargainUserHelpQueryParam);

}
