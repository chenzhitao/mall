package co.yixiang.modules.shop.mapper;

import co.yixiang.modules.shop.entity.YxProductTemplate;
import co.yixiang.modules.shop.entity.YxVideoType;
import co.yixiang.modules.shop.web.vo.YxProductTemplateVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface YxVideoTypeMapper extends BaseMapper<YxVideoType> {


    @Select("select id,type_name,remark,show_flag,sort_no from yx_video_type where show_flag='Y' order by sort_no asc")
    List<YxVideoType> selectVideoTypeList();
}
