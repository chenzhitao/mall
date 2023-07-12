package co.yixiang.modules.shop.mapper;

import co.yixiang.modules.shop.entity.YxProductTemplate;
import co.yixiang.modules.shop.web.param.YxArticleQueryParam;
import co.yixiang.modules.shop.web.vo.StoreCouponUserVo;
import co.yixiang.modules.shop.web.vo.YxArticleQueryVo;
import co.yixiang.modules.shop.web.vo.YxProductTemplateVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
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
public interface YxProductTemplateMapper extends BaseMapper<YxProductTemplate> {

    @Select("select id,title,description,type,image_url as imageUrl,product_id as productId,product_name as productName,is_show as isShow,sort_no as sortNo from yx_product_template where is_show=1 order by sort_no asc")
    List<YxProductTemplateVo> selectProductTemplateList();
}
