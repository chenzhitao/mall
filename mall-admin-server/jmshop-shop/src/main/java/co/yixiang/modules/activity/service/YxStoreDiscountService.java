package co.yixiang.modules.activity.service;

import co.yixiang.modules.activity.domain.YxStoreDiscount;
import co.yixiang.modules.activity.service.dto.YxStoreDiscountDTO;
import co.yixiang.modules.activity.service.dto.YxStoreDiscountQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

//@CacheConfig(cacheNames = "YxStoreDiscount")
public interface YxStoreDiscountService {

    /**
    * 查询数据分页
    * @param criteria
    * @param pageable
    * @return
    */
    //@Cacheable
    Map<String,Object> queryAll(YxStoreDiscountQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria
    * @return
    */
    //@Cacheable
    List<YxStoreDiscountDTO> queryAll(YxStoreDiscountQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    //@Cacheable(key = "#p0")
    YxStoreDiscountDTO findById(Integer id);

    /**
     * 创建
     * @param resources
     * @return
     */
    //@CacheEvict(allEntries = true)
    YxStoreDiscountDTO create(YxStoreDiscount resources);

    /**
     * 编辑
     * @param resources
     */
    //@CacheEvict(allEntries = true)
    void update(YxStoreDiscount resources);

    /**
     * 删除
     * @param id
     */
    //@CacheEvict(allEntries = true)
    void delete(Integer id);

    YxStoreDiscountDTO findByProductId(Integer id);

}