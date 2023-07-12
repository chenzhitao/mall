package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxVideoType;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.repository.YxVideoTypeRepository;
import co.yixiang.modules.shop.service.YxVideoTypeService;
import co.yixiang.modules.shop.service.dto.YxVideoTypeDto;
import co.yixiang.modules.shop.service.dto.YxVideoTypeQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxVideoTypeMapper;
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
* @date 2023-06-08
*/
@Service
//@CacheConfig(cacheNames = "yxVideoType")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxVideoTypeServiceImpl implements YxVideoTypeService {

    private final YxVideoTypeRepository yxVideoTypeRepository;

    private final YxVideoTypeMapper yxVideoTypeMapper;

    public YxVideoTypeServiceImpl(YxVideoTypeRepository yxVideoTypeRepository, YxVideoTypeMapper yxVideoTypeMapper) {
        this.yxVideoTypeRepository = yxVideoTypeRepository;
        this.yxVideoTypeMapper = yxVideoTypeMapper;
    }

    @Override
    //@Cacheable
    public Map<String,Object> queryAll(YxVideoTypeQueryCriteria criteria, Pageable pageable){
        Page<YxVideoType> page = yxVideoTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);

        return PageUtil.toPage(page.map(yxVideoTypeMapper::toDto));
    }

    @Override
    //@Cacheable
    public List<YxVideoTypeDto> queryAll(YxVideoTypeQueryCriteria criteria){
        return yxVideoTypeMapper.toDto(yxVideoTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public YxVideoTypeDto findById(Integer id) {
        YxVideoType yxVideoType = yxVideoTypeRepository.findById(id).orElseGet(YxVideoType::new);
        ValidationUtil.isNull(yxVideoType.getId(),"YxVideoType","id",id);
        return yxVideoTypeMapper.toDto(yxVideoType);
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public YxVideoTypeDto create(YxVideoType resources) {
        return yxVideoTypeMapper.toDto(yxVideoTypeRepository.save(resources));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(YxVideoType resources) {
        YxVideoType yxVideoType = yxVideoTypeRepository.findById(resources.getId()).orElseGet(YxVideoType::new);
        ValidationUtil.isNull( yxVideoType.getId(),"YxVideoType","id",resources.getId());
        yxVideoType.copy(resources);
        yxVideoTypeRepository.save(yxVideoType);
    }

    @Override
    //@CacheEvict(allEntries = true)
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            yxVideoTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<YxVideoTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxVideoTypeDto yxVideoType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("类型名称", yxVideoType.getTypeName());
            map.put("备注", yxVideoType.getRemark());
            map.put("是否展示", yxVideoType.getShowFlag());
            map.put("排序", yxVideoType.getSortNo());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
