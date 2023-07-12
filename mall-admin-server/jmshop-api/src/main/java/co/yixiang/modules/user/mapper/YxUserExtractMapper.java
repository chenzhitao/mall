package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUserExtract;
import co.yixiang.modules.user.web.param.YxUserExtractQueryParam;
import co.yixiang.modules.user.web.vo.YxUserExtractQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 用户提现表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
@Repository
public interface YxUserExtractMapper extends BaseMapper<YxUserExtract> {

    @Select("select IFNULL(sum(extract_price),0) from yx_user_extract " +
            "where status=1 " +
            "and uid=#{uid}")
    double sumPrice(@Param("uid") int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserExtractQueryVo getYxUserExtractById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserExtractQueryParam
     * @return
     */
    IPage<YxUserExtractQueryVo> getYxUserExtractPageList(@Param("page") Page page, @Param("param") YxUserExtractQueryParam yxUserExtractQueryParam);

}
