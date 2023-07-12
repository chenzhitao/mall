package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.domain.YxVideoProduct;
import co.yixiang.modules.shop.service.dto.YxVideoProductDto;
import co.yixiang.modules.shop.service.dto.YxVideoProductQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author yushen
* @date 2023-06-08
*/
public interface YxVideoProductService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxVideoProductQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxVideoProductDto>
    */
    List<YxVideoProductDto> queryAll(YxVideoProductQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return YxVideoProductDto
     */
    YxVideoProductDto findById(Integer id);

    /**
    * 创建
    * @param resources /
    * @return YxVideoProductDto
    */
    YxVideoProductDto create(YxVideoProduct resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(YxVideoProduct resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Integer[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YxVideoProductDto> all, HttpServletResponse response) throws IOException;
}