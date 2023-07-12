package co.yixiang.modules.user.mapper;

import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.web.dto.BillDTO;
import co.yixiang.modules.user.web.dto.SignDTO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUserSign;
import co.yixiang.modules.user.web.param.YxUserSignQueryParam;
import co.yixiang.modules.user.web.vo.YxUserSignQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 签到记录表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-05
 */
@Repository
public interface YxUserSignMapper extends BaseMapper<YxUserSign> {



    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserSignQueryVo getYxUserSignById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserSignQueryParam
     * @return
     */
    IPage<YxUserSignQueryVo> getYxUserSignPageList(@Param("page") Page page, @Param("param") YxUserSignQueryParam yxUserSignQueryParam);

}
