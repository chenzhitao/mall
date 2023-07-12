package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.domain.YxProductTemplateItem;
import co.yixiang.modules.shop.service.dto.YxProductTemplateItemDto;
import co.yixiang.modules.shop.service.dto.YxProductTemplateItemQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author yushen
* @date 2023-05-11
*/
public interface YxProductTemplateItemService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxProductTemplateItemQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxProductTemplateItemDto>
    */
    List<YxProductTemplateItemDto> queryAll(YxProductTemplateItemQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return YxProductTemplateItemDto
     */
    YxProductTemplateItemDto findById(Integer id);

    /**
    * 创建
    * @param resources /
    * @return YxProductTemplateItemDto
    */
    YxProductTemplateItemDto create(YxProductTemplateItem resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(YxProductTemplateItem resources);

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
    void download(List<YxProductTemplateItemDto> all, HttpServletResponse response) throws IOException;
}