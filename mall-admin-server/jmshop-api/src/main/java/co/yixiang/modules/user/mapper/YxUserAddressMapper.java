package co.yixiang.modules.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.user.entity.YxUserAddress;
import co.yixiang.modules.user.web.param.YxUserAddressQueryParam;
import co.yixiang.modules.user.web.vo.YxUserAddressQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 用户地址表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-28
 */
@Repository
public interface YxUserAddressMapper extends BaseMapper<YxUserAddress> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserAddressQueryVo getYxUserAddressById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxUserAddressQueryParam
     * @return
     */
    IPage<YxUserAddressQueryVo> getYxUserAddressPageList(@Param("page") Page page, @Param("param") YxUserAddressQueryParam yxUserAddressQueryParam);

}
