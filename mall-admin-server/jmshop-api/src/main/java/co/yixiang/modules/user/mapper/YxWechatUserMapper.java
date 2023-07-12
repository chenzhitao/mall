package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxWechatUser;
import co.yixiang.modules.user.web.param.YxWechatUserQueryParam;
import co.yixiang.modules.user.web.vo.YxWechatUserQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 微信用户表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Repository
public interface YxWechatUserMapper extends BaseMapper<YxWechatUser> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxWechatUserQueryVo getYxWechatUserById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxWechatUserQueryParam
     * @return
     */
    IPage<YxWechatUserQueryVo> getYxWechatUserPageList(@Param("page") Page page, @Param("param") YxWechatUserQueryParam yxWechatUserQueryParam);

}
