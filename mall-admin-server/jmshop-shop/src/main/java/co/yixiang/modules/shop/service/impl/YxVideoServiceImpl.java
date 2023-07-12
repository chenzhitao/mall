package co.yixiang.modules.shop.service.impl;

import co.yixiang.modules.shop.domain.YxVideo;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.shop.repository.YxVideoRepository;
import co.yixiang.modules.shop.service.YxVideoService;
import co.yixiang.modules.shop.service.dto.YxVideoDto;
import co.yixiang.modules.shop.service.dto.YxVideoQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxVideoMapper;
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
//@CacheConfig(cacheNames = "yxVideo")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxVideoServiceImpl implements YxVideoService {

    private final YxVideoRepository yxVideoRepository;

    private final YxVideoMapper yxVideoMapper;

    public YxVideoServiceImpl(YxVideoRepository yxVideoRepository, YxVideoMapper yxVideoMapper) {
        this.yxVideoRepository = yxVideoRepository;
        this.yxVideoMapper = yxVideoMapper;
    }

    @Override
    //@Cacheable
    public Map<String,Object> queryAll(YxVideoQueryCriteria criteria, Pageable pageable){
        Page<YxVideo> page = yxVideoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(yxVideoMapper::toDto));
    }

    @Override
    //@Cacheable
    public List<YxVideoDto> queryAll(YxVideoQueryCriteria criteria){
        return yxVideoMapper.toDto(yxVideoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public YxVideoDto findById(Integer id) {
        YxVideo yxVideo = yxVideoRepository.findById(id).orElseGet(YxVideo::new);
        ValidationUtil.isNull(yxVideo.getId(),"YxVideo","id",id);
        return yxVideoMapper.toDto(yxVideo);
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public YxVideoDto create(YxVideo resources) {
        return yxVideoMapper.toDto(yxVideoRepository.save(resources));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(YxVideo resources) {
        YxVideo yxVideo = yxVideoRepository.findById(resources.getId()).orElseGet(YxVideo::new);
        ValidationUtil.isNull( yxVideo.getId(),"YxVideo","id",resources.getId());
        yxVideo.copy(resources);
        yxVideoRepository.save(yxVideo);
    }

    @Override
    //@CacheEvict(allEntries = true)
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            yxVideoRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<YxVideoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxVideoDto yxVideo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("类型ID", yxVideo.getTypeId());
            map.put("类型名称", yxVideo.getTypeName());
            map.put("标题", yxVideo.getTitle());
            map.put("视频封面", yxVideo.getCoverImage());
            map.put("视频地址", yxVideo.getVideoUrl());
            map.put("奖励积分数量", yxVideo.getScoreNum());
            map.put("真实浏览量", yxVideo.getWatchNum());
            map.put("虚拟浏览量", yxVideo.getVirtualWatchNum());
            map.put("创建时间", yxVideo.getCreateTime());
            map.put("备注", yxVideo.getRemark());
            map.put("是否展示", yxVideo.getShowFlag());
            map.put("排序", yxVideo.getSortNo());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}