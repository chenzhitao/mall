package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUserRecharge;
import co.yixiang.modules.user.web.param.YxUserRechargeQueryParam;
import co.yixiang.modules.user.web.vo.YxUserRechargeQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 用户充值表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-08
 */
@Repository
public interface YxUserRechargeMapper extends BaseMapper<YxUserRecharge> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserRechargeQueryVo getYxUserRechargeById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserRechargeQueryParam
     * @return
     */
    IPage<YxUserRechargeQueryVo> getYxUserRechargePageList(@Param("page") Page page, @Param("param") YxUserRechargeQueryParam yxUserRechargeQueryParam);

}
