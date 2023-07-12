package co.yixiang.modules.activity.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.activity.entity.YxStoreBargainUser;
import co.yixiang.modules.activity.web.param.YxStoreBargainUserQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainUserQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户参与砍价表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
@Repository
public interface YxStoreBargainUserMapper extends BaseMapper<YxStoreBargainUser> {


    @Select("SELECT u.uid,u.is_del as isDel,u.bargain_price - u.price as residuePrice,u.id," +
            "u.bargain_id as bargainId,u.bargain_price as bargainPrice," +
            "u.bargain_price_min as bargainPriceMin,u.price,u.status,b.title," +
            "b.image,b.stop_time as datatime FROM yx_store_bargain_user u INNER JOIN " +
            "yx_store_bargain b ON b.id=u.bargain_id WHERE u.uid = #{uid} AND u.is_del = 0 " +
            "ORDER BY u.id DESC ")
    List<YxStoreBargainUserQueryVo> getBargainUserList(@Param("uid") int uid, Page page);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStoreBargainUserQueryVo getYxStoreBargainUserById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStoreBargainUserQueryParam
     * @return
     */
    IPage<YxStoreBargainUserQueryVo> getYxStoreBargainUserPageList(@Param("page") Page page, @Param("param") YxStoreBargainUserQueryParam yxStoreBargainUserQueryParam);

}
