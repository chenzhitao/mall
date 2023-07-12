package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.modules.shop.domain.YxSystemUserLevel;
import co.yixiang.modules.shop.domain.YxUserLevel;
import co.yixiang.modules.shop.repository.YxSystemUserLevelRepository;
import co.yixiang.modules.shop.repository.YxUserLevelRepository;
import co.yixiang.modules.shop.service.YxSystemUserLevelService;
import co.yixiang.modules.shop.service.dto.YxSystemUserLevelDTO;
import co.yixiang.modules.shop.service.dto.YxSystemUserLevelQueryCriteria;
import co.yixiang.modules.shop.service.mapper.YxSystemUserLevelMapper;
import co.yixiang.utils.PageUtil;
import co.yixiang.utils.QueryHelp;
import co.yixiang.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @author hupeng
* @date 2019-12-04
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxSystemUserLevelServiceImpl implements YxSystemUserLevelService {

    private final YxSystemUserLevelRepository yxSystemUserLevelRepository;

    private final YxSystemUserLevelMapper yxSystemUserLevelMapper;

    private final YxUserLevelRepository yxUserLevelRepository;

    public YxSystemUserLevelServiceImpl(YxSystemUserLevelRepository yxSystemUserLevelRepository, YxSystemUserLevelMapper yxSystemUserLevelMapper, YxUserLevelRepository yxUserLevelRepository ) {
        this.yxSystemUserLevelRepository = yxSystemUserLevelRepository;
        this.yxSystemUserLevelMapper = yxSystemUserLevelMapper;
        this.yxUserLevelRepository = yxUserLevelRepository;
    }

    @Override
    public Map<String,Object> queryAll(YxSystemUserLevelQueryCriteria criteria, Pageable pageable){
        criteria.setIsDel(0);
        Page<YxSystemUserLevel> page = yxSystemUserLevelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(yxSystemUserLevelMapper::toDto));
    }

    @Override
    public List<YxSystemUserLevelDTO> queryAll(YxSystemUserLevelQueryCriteria criteria){
        return yxSystemUserLevelMapper.toDto(yxSystemUserLevelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public YxSystemUserLevelDTO findById(Integer id) {
        Optional<YxSystemUserLevel> yxSystemUserLevel = yxSystemUserLevelRepository.findById(id);
        //ValidationUtil.isNull(yxSystemUserLevel,"YxSystemUserLevel","id",id);
        if(yxSystemUserLevel.isPresent()){
            return yxSystemUserLevelMapper.toDto(yxSystemUserLevel.get());
        }
        return new YxSystemUserLevelDTO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public YxSystemUserLevelDTO create(YxSystemUserLevel resources) {
        return yxSystemUserLevelMapper.toDto(yxSystemUserLevelRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(YxSystemUserLevel resources) {
        Optional<YxSystemUserLevel> optionalYxSystemUserLevel = yxSystemUserLevelRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalYxSystemUserLevel,"YxSystemUserLevel","id",resources.getId());
        YxSystemUserLevel yxSystemUserLevel = optionalYxSystemUserLevel.get();
        yxSystemUserLevel.copy(resources);
        yxSystemUserLevelRepository.save(yxSystemUserLevel);

        List<YxUserLevel> yxUserLevel = yxUserLevelRepository.selectOneByLevelid(resources.getId());
        if(ObjectUtil.isNotNull(yxUserLevel)){
            yxUserLevelRepository.updateUserLevelDiscount(resources.getId(), resources.getDiscount());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        yxSystemUserLevelRepository.deleteById(id);
    }
}
