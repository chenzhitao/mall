package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxProductTemplateItem;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.repository.YxProductTemplateItemRepository;
import co.yixiang.modules.shop.service.YxProductTemplateItemService;
import co.yixiang.modules.shop.service.dto.YxProductTemplateItemDto;
import co.yixiang.modules.shop.service.dto.YxProductTemplateItemQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxProductTemplateItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import co.yixiang.utils.PageUtil;
import co.yixiang.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author yushen
* @date 2023-05-11
*/
@Service
//@CacheConfig(cacheNames = "yxProductTemplateItem")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxProductTemplateItemServiceImpl implements YxProductTemplateItemService {

    private final YxProductTemplateItemRepository yxProductTemplateItemRepository;

    private final YxProductTemplateItemMapper yxProductTemplateItemMapper;

    public YxProductTemplateItemServiceImpl(YxProductTemplateItemRepository yxProductTemplateItemRepository, YxProductTemplateItemMapper yxProductTemplateItemMapper) {
        this.yxProductTemplateItemRepository = yxProductTemplateItemRepository;
        this.yxProductTemplateItemMapper = yxProductTemplateItemMapper;
    }

    @Override
    //@Cacheable
    public Map<String,Object> queryAll(YxProductTemplateItemQueryCriteria criteria, Pageable pageable){
        Page<YxProductTemplateItem> page = yxProductTemplateItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(yxProductTemplateItemMapper::toDto));
    }

    @Override
    //@Cacheable
    public List<YxProductTemplateItemDto> queryAll(YxProductTemplateItemQueryCriteria criteria){
        return yxProductTemplateItemMapper.toDto(yxProductTemplateItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public YxProductTemplateItemDto findById(Integer id) {
        YxProductTemplateItem yxProductTemplateItem = yxProductTemplateItemRepository.findById(id).orElseGet(YxProductTemplateItem::new);
        ValidationUtil.isNull(yxProductTemplateItem.getId(),"YxProductTemplateItem","id",id);
        return yxProductTemplateItemMapper.toDto(yxProductTemplateItem);
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public YxProductTemplateItemDto create(YxProductTemplateItem resources) {
        return yxProductTemplateItemMapper.toDto(yxProductTemplateItemRepository.save(resources));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(YxProductTemplateItem resources) {
        YxProductTemplateItem yxProductTemplateItem = yxProductTemplateItemRepository.findById(resources.getId()).orElseGet(YxProductTemplateItem::new);
        ValidationUtil.isNull( yxProductTemplateItem.getId(),"YxProductTemplateItem","id",resources.getId());
        yxProductTemplateItem.copy(resources);
        yxProductTemplateItemRepository.save(yxProductTemplateItem);
    }

    @Override
    //@CacheEvict(allEntries = true)
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            yxProductTemplateItemRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<YxProductTemplateItemDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxProductTemplateItemDto yxProductTemplateItem : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("模板ID", yxProductTemplateItem.getTemplateId());
            map.put("商品ID", yxProductTemplateItem.getProductId());
            map.put("商品名称", yxProductTemplateItem.getProductName());
            map.put("备注", yxProductTemplateItem.getDescription());
            map.put("排序编号", yxProductTemplateItem.getSortNo());
            map.put("添加时间", yxProductTemplateItem.getAddTime());
            map.put("是否参与排名  0否  1是", yxProductTemplateItem.getRankFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}