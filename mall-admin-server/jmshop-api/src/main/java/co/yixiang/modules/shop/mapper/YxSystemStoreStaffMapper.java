package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxSystemStoreStaff;
import co.yixiang.modules.shop.web.param.YxSystemStoreStaffQueryParam;
import co.yixiang.modules.shop.web.vo.YxSystemStoreStaffQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 门店店员表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2020-03-23
 */
@Repository
public interface YxSystemStoreStaffMapper extends BaseMapper<YxSystemStoreStaff> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxSystemStoreStaffQueryVo getYxSystemStoreStaffById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxSystemStoreStaffQueryParam
     * @return
     */
    IPage<YxSystemStoreStaffQueryVo> getYxSystemStoreStaffPageList(@Param("page") Page page, @Param("param") YxSystemStoreStaffQueryParam yxSystemStoreStaffQueryParam);

}
