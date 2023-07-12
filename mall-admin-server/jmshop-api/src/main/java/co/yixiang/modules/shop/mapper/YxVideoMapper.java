package co.yixiang.modules.shop.mapper;

import co.yixiang.modules.shop.entity.YxVideo;
import co.yixiang.modules.shop.entity.YxVideoType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 文章管理表 Mapper 接口
 * </p>
 *
 * @author hupeng
 * @since 2019-10-02
 */
@Repository
public interface YxVideoMapper extends BaseMapper<YxVideo> {


    @Select("select id,type_id,type_name,title,cover_image,video_url,score_num,watch_num,virtual_watch_num,create_time,remark,show_flag,sort_no from yx_video where type_id=#{typeId} order by sort_no desc")
    List<YxVideo> selectVideoList(@Param("typeId") int typeId);
}
