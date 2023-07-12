package co.yixiang.modules.user.mapper;

import co.yixiang.modules.user.web.dto.UserLevelInfoDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUserLevel;
import co.yixiang.modules.user.web.param.YxUserLevelQueryParam;
import co.yixiang.modules.user.web.vo.YxUserLevelQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 用户等级记录表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Repository
public interface YxUserLevelMapper extends BaseMapper<YxUserLevel> {

    @Select("SELECT l.id,a.add_time as addTime,l.discount,a.level_id as levelId,l.name," +
            "l.icon,l.grade FROM yx_user_level a INNER JOIN yx_system_user_level l " +
            "ON l.id=a.level_id WHERE a.status = 1 AND a.is_del = 0 AND a.id = #{id} LIMIT 1")
    UserLevelInfoDTO getUserLevelInfo(int id);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserLevelQueryVo getYxUserLevelById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserLevelQueryParam
     * @return
     */
    IPage<YxUserLevelQueryVo> getYxUserLevelPageList(@Param("page") Page page, @Param("param") YxUserLevelQueryParam yxUserLevelQueryParam);

}
