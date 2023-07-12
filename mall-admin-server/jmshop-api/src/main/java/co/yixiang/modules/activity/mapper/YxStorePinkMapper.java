package co.yixiang.modules.activity.mapper;

import co.yixiang.modules.activity.web.dto.PinkDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.activity.entity.YxStorePink;
import co.yixiang.modules.activity.web.param.YxStorePinkQueryParam;
import co.yixiang.modules.activity.web.vo.YxStorePinkQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 拼团表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-11-19
 */
@Repository
public interface YxStorePinkMapper extends BaseMapper<YxStorePink> {

    @Select("SELECT p.id,p.uid,p.people,p.price,p.stop_time as stopTime,u.nickname,u.avatar" +
            " FROM yx_store_pink p INNER JOIN yx_user u ON u.uid=p.uid" +
            " WHERE stop_time > unix_timestamp(now()) AND p.cid = #{cid} AND p.k_id = 0 " +
            "AND p.is_refund = 0 ORDER BY p.add_time DESC")
    List<PinkDTO> getPinks(int cid);

    //<![CDATA[ >= ]]>
    @Select("SELECT p.id,u.nickname,u.avatar" +
            " FROM yx_store_pink p RIGHT  JOIN yx_user u ON u.uid=p.uid" +
            " where p.status= 2 AND p.uid <> ${uid} " +
            "AND p.is_refund = 0")
    List<PinkDTO> getPinkOkList(int uid);

    @Select("SELECT p.id,p.uid,p.people,p.price,p.stop_time as stopTime,u.nickname,u.avatar" +
            " FROM yx_store_pink p LEFT JOIN yx_user u ON u.uid=p.uid" +
            " where p.k_id= ${kid} " +
            "AND p.is_refund = 0")
    List<PinkDTO> getPinkMember(int kid);

    @Select("SELECT p.id,p.uid,p.people,p.price,p.stop_time as stopTime,u.nickname,u.avatar" +
            " FROM yx_store_pink p LEFT JOIN yx_user u ON u.uid=p.uid" +
            " where p.id= ${id} ")
    PinkDTO getPinkUserOne(int id);

    @Select("select IFNULL(sum(total_num),0) from yx_store_pink " +
            "where status=2 and is_refund=0")
    int sumNum();

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxStorePinkQueryVo getYxStorePinkById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param yxStorePinkQueryParam
     * @return
     */
    IPage<YxStorePinkQueryVo> getYxStorePinkPageList(@Param("page") Page page, @Param("param") YxStorePinkQueryParam yxStorePinkQueryParam);

}
