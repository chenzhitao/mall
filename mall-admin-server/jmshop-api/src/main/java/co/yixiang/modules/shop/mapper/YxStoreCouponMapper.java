package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreCoupon;
import co.yixiang.modules.shop.web.param.YxStoreCouponQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 优惠券表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Repository
public interface YxStoreCouponMapper extends BaseMapper<YxStoreCoupon> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponQueryVo getYxStoreCouponById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreCouponQueryParam
     * @return
     */
    IPage<YxStoreCouponQueryVo> getYxStoreCouponPageList(@Param("page") Page page, @Param("param") YxStoreCouponQueryParam yxStoreCouponQueryParam);

}
