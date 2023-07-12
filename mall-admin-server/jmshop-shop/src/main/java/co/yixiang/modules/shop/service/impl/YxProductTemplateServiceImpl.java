package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxProductTemplate;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.repository.YxProductTemplateRepository;
import co.yixiang.modules.shop.service.YxProductTemplateService;
import co.yixiang.modules.shop.service.dto.YxProductTemplateDto;
import co.yixiang.modules.shop.service.dto.YxProductTemplateQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxProductTemplateMapper;
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
//@CacheConfig(cacheNames = "yxProductTemplate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxProductTemplateServiceImpl implements YxProductTemplateService {

    private final YxProductTemplateRepository yxProductTemplateRepository;

    private final YxProductTemplateMapper yxProductTemplateMapper;

    public YxProductTemplateServiceImpl(YxProductTemplateRepository yxProductTemplateRepository, YxProductTemplateMapper yxProductTemplateMapper) {
        this.yxProductTemplateRepository = yxProductTemplateRepository;
        this.yxProductTemplateMapper = yxProductTemplateMapper;
    }

    @Override
    //@Cacheable
    public Map<String,Object> queryAll(YxProductTemplateQueryCriteria criteria, Pageable pageable){
        Page<YxProductTemplate> page = yxProductTemplateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(yxProductTemplateMapper::toDto));
    }

    @Override
    //@Cacheable
    public List<YxProductTemplateDto> queryAll(YxProductTemplateQueryCriteria criteria){
        return yxProductTemplateMapper.toDto(yxProductTemplateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public YxProductTemplateDto findById(Integer id) {
        YxProductTemplate yxProductTemplate = yxProductTemplateRepository.findById(id).orElseGet(YxProductTemplate::new);
        ValidationUtil.isNull(yxProductTemplate.getId(),"YxProductTemplate","id",id);
        return yxProductTemplateMapper.toDto(yxProductTemplate);
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public YxProductTemplateDto create(YxProductTemplate resources) {
        return yxProductTemplateMapper.toDto(yxProductTemplateRepository.save(resources));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(YxProductTemplate resources) {
        YxProductTemplate yxProductTemplate = yxProductTemplateRepository.findById(resources.getId()).orElseGet(YxProductTemplate::new);
        ValidationUtil.isNull( yxProductTemplate.getId(),"YxProductTemplate","id",resources.getId());
        yxProductTemplate.copy(resources);
        yxProductTemplateRepository.save(yxProductTemplate);
    }

    @Override
    //@CacheEvict(allEntries = true)
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            yxProductTemplateRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<YxProductTemplateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxProductTemplateDto yxProductTemplate : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标题", yxProductTemplate.getTitle());
            map.put("描述", yxProductTemplate.getDescription());
            map.put("模板分类", yxProductTemplate.getType());
            map.put("图片", yxProductTemplate.getImageUrl());
            map.put("商品ID", yxProductTemplate.getProductId());
            map.put("商品名称", yxProductTemplate.getProductName());
            map.put("状态（0：未上架，1：上架）", yxProductTemplate.getIsShow());
            map.put("排序编号", yxProductTemplate.getSortNo());
            map.put("添加时间", yxProductTemplate.getAddTime());
            map.put("是否删除", yxProductTemplate.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}