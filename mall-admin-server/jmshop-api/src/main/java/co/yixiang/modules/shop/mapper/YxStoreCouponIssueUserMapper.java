package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreCouponIssueUser;
import co.yixiang.modules.shop.web.param.YxStoreCouponIssueUserQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueUserQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 优惠券前台用户领取记录表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Repository
public interface YxStoreCouponIssueUserMapper extends BaseMapper<YxStoreCouponIssueUser> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCouponIssueUserQueryVo getYxStoreCouponIssueUserById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreCouponIssueUserQueryParam
     * @return
     */
    IPage<YxStoreCouponIssueUserQueryVo> getYxStoreCouponIssueUserPageList(@Param("page") Page page, @Param("param") YxStoreCouponIssueUserQueryParam yxStoreCouponIssueUserQueryParam);

}
