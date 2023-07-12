package co.yixiang.modules.shop.service;

import co.yixiang.modules.shop.domain.YxVideoType;
import co.yixiang.modules.shop.service.dto.YxVideoTypeDto;
import co.yixiang.modules.shop.service.dto.YxVideoTypeQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author yushen
* @date 2023-06-08
*/
public interface YxVideoTypeService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YxVideoTypeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YxVideoTypeDto>
    */
    List<YxVideoTypeDto> queryAll(YxVideoTypeQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param typeId ID
     * @return YxVideoTypeDto
     */
    YxVideoTypeDto findById(Integer typeId);

    /**
    * 创建
    * @param resources /
    * @return YxVideoTypeDto
    */
    YxVideoTypeDto create(YxVideoType resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(YxVideoType resources);

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
    void download(List<YxVideoTypeDto> all, HttpServletResponse response) throws IOException;
}