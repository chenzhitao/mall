package co.yixiang.modules.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.shop.entity.YxStoreCart;
import co.yixiang.modules.shop.web.param.YxStoreCartQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-25
 */
@Repository
public interface YxStoreCartMapper extends BaseMapper<YxStoreCart> {

    @Select("select IFNULL(sum(cart_num),0) from yx_store_cart " +
            "where is_pay=0 and is_del=0 and is_new=0 and uid=#{uid} and type=#{type}")
    int cartSum(@Param("uid") int uid,@Param("type") String type);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreCartQueryVo getYxStoreCartById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreCartQueryParam
     * @return
     */
    IPage<YxStoreCartQueryVo> getYxStoreCartPageList(@Param("page") Page page, @Param("param") YxStoreCartQueryParam yxStoreCartQueryParam);

}
