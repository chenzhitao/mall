package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxVideoProduct;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.repository.YxVideoProductRepository;
import co.yixiang.modules.shop.service.YxVideoProductService;
import co.yixiang.modules.shop.service.dto.YxVideoProductDto;
import co.yixiang.modules.shop.service.dto.YxVideoProductQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxVideoProductMapper;
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
//@CacheConfig(cacheNames = "yxVideoProduct")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxVideoProductServiceImpl implements YxVideoProductService {

    private final YxVideoProductRepository yxVideoProductRepository;

    private final YxVideoProductMapper yxVideoProductMapper;

    public YxVideoProductServiceImpl(YxVideoProductRepository yxVideoProductRepository, YxVideoProductMapper yxVideoProductMapper) {
        this.yxVideoProductRepository = yxVideoProductRepository;
        this.yxVideoProductMapper = yxVideoProductMapper;
    }

    @Override
    //@Cacheable
    public Map<String,Object> queryAll(YxVideoProductQueryCriteria criteria, Pageable pageable){
        Page<YxVideoProduct> page = yxVideoProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(yxVideoProductMapper::toDto));
    }

    @Override
    //@Cacheable
    public List<YxVideoProductDto> queryAll(YxVideoProductQueryCriteria criteria){
        return yxVideoProductMapper.toDto(yxVideoProductRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public YxVideoProductDto findById(Integer id) {
        YxVideoProduct yxVideoProduct = yxVideoProductRepository.findById(id).orElseGet(YxVideoProduct::new);
        ValidationUtil.isNull(yxVideoProduct.getId(),"YxVideoProduct","id",id);
        return yxVideoProductMapper.toDto(yxVideoProduct);
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public YxVideoProductDto create(YxVideoProduct resources) {
        return yxVideoProductMapper.toDto(yxVideoProductRepository.save(resources));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(YxVideoProduct resources) {
        YxVideoProduct yxVideoProduct = yxVideoProductRepository.findById(resources.getId()).orElseGet(YxVideoProduct::new);
        ValidationUtil.isNull( yxVideoProduct.getId(),"YxVideoProduct","id",resources.getId());
        yxVideoProduct.copy(resources);
        yxVideoProductRepository.save(yxVideoProduct);
    }

    @Override
    //@CacheEvict(allEntries = true)
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            yxVideoProductRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<YxVideoProductDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxVideoProductDto yxVideoProduct : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("视频ID", yxVideoProduct.getVideoId());
            map.put("商品ID", yxVideoProduct.getProductId());
            map.put("商品名称", yxVideoProduct.getProductName());
            map.put("排序编号", yxVideoProduct.getSortNo());
            map.put("添加时间", yxVideoProduct.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}