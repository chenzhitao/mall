package co.yixiang.modules.manage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.manage.entity.YxExpress;
import co.yixiang.modules.manage.web.param.YxExpressQueryParam;
import co.yixiang.modules.manage.web.vo.YxExpressQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 快递公司表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-13
 */
@Repository
public interface YxExpressMapper extends BaseMapper<YxExpress> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxExpressQueryVo getYxExpressById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxExpressQueryParam
     * @return
     */
    IPage<YxExpressQueryVo> getYxExpressPageList(@Param("page") Page page, @Param("param") YxExpressQueryParam yxExpressQueryParam);

}
