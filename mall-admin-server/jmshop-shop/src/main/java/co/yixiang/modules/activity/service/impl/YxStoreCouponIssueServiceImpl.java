package co.yixiang.modules.activity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.activity.domain.YxStoreCoupon;
import co.yixiang.modules.activity.domain.YxStoreCouponIssue;
import co.yixiang.modules.activity.domain.YxStoreCouponIssueUser;
import co.yixiang.modules.activity.domain.YxStoreCouponUser;
import co.yixiang.modules.activity.repository.YxStoreCouponIssueRepository;
import co.yixiang.modules.activity.repository.YxStoreCouponIssueUserRepository;
import co.yixiang.modules.activity.repository.YxStoreCouponRepository;
import co.yixiang.modules.activity.repository.YxStoreCouponUserRepository;
import co.yixiang.modules.activity.service.YxStoreCouponIssueService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueDTO;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueQueryCriteria;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueUserQueryCriteria;
import co.yixiang.modules.activity.service.mapper.YxStoreCouponIssueMapper;
import co.yixiang.modules.activity.service.mapper.YxStoreCouponIssueUserMapper;
import co.yixiang.modules.shop.domain.YxStoreCart;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.PageUtil;
import co.yixiang.utils.QueryHelp;
import co.yixiang.utils.ValidationUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.uid;

/**
 * @author hupeng
 * @date 2019-11-09
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreCouponIssueServiceImpl implements YxStoreCouponIssueService {

    private final YxStoreCouponIssueRepository yxStoreCouponIssueRepository;

    private final YxStoreCouponIssueMapper yxStoreCouponIssueMapper;

    private final YxStoreCouponRepository yxStoreCouponRepository;

    private YxStoreCouponIssueUserRepository yxStoreCouponIssueUserRepository;

    private final YxStoreCouponUserRepository yxStoreCouponUserRepository;


    public YxStoreCouponIssueServiceImpl(YxStoreCouponIssueRepository yxStoreCouponIssueRepository, YxStoreCouponIssueMapper yxStoreCouponIssueMapper, YxStoreCouponRepository yxStoreCouponRepository,YxStoreCouponIssueUserRepository yxStoreCouponIssueUserRepository,YxStoreCouponUserRepository yxStoreCouponUserRepository) {
        this.yxStoreCouponIssueRepository = yxStoreCouponIssueRepository;
        this.yxStoreCouponIssueMapper = yxStoreCouponIssueMapper;
        this.yxStoreCouponRepository = yxStoreCouponRepository;
        this.yxStoreCouponIssueUserRepository=yxStoreCouponIssueUserRepository;
        this.yxStoreCouponUserRepository=yxStoreCouponUserRepository;
    }

    @Override
    public Map<String, Object> queryAll(YxStoreCouponIssueQueryCriteria criteria, Pageable pageable) {
        Page<YxStoreCouponIssue> page = yxStoreCouponIssueRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        List<YxStoreCouponIssueDTO> pages = yxStoreCouponIssueMapper.toDto(page.getContent());
        for (YxStoreCouponIssueDTO yx : pages) {
            Optional<YxStoreCoupon> yxStoreCoupon = yxStoreCouponRepository.findById(yx.getCid());
            if (yxStoreCoupon.isPresent()) {
                yx.setUseMinPrice(yxStoreCoupon.get().getUseMinPrice());
                yx.setCouponPrice(yxStoreCoupon.get().getCouponPrice());
            }
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", pages);
        map.put("totalElements", page.getTotalElements());

        return map;
    }

    @Override
    public List<YxStoreCouponIssueDTO> queryAll(YxStoreCouponIssueQueryCriteria criteria) {
        return yxStoreCouponIssueMapper.toDto(yxStoreCouponIssueRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public List<YxStoreCouponIssue> list(YxStoreCouponIssue yxStoreCouponIssue) {
        return yxStoreCouponIssueMapper.toDto(yxStoreCouponIssueRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, yxStoreCouponIssue, criteriaBuilder)));
    }

    @Override
    public YxStoreCouponIssueDTO findById(Integer id) {
        Optional<YxStoreCouponIssue> yxStoreCouponIssue = yxStoreCouponIssueRepository.findById(id);
        ValidationUtil.isNull(yxStoreCouponIssue, "YxStoreCouponIssue", "id", id);
        return yxStoreCouponIssueMapper.toDto(yxStoreCouponIssue.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public YxStoreCouponIssueDTO create(YxStoreCouponIssue resources) {
        return yxStoreCouponIssueMapper.toDto(yxStoreCouponIssueRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(YxStoreCouponIssue resources) {
        Optional<YxStoreCouponIssue> optionalYxStoreCouponIssue = yxStoreCouponIssueRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalYxStoreCouponIssue, "YxStoreCouponIssue", "id", resources.getId());
        YxStoreCouponIssue yxStoreCouponIssue = optionalYxStoreCouponIssue.get();
        yxStoreCouponIssue.copy(resources);
        yxStoreCouponIssueRepository.save(yxStoreCouponIssue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        yxStoreCouponIssueRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issueUserCoupon(Integer issueCouponId, String uids) {
        YxStoreCouponIssue couponIssueQueryVo=yxStoreCouponIssueRepository.getYxStoreCouponIssueById(issueCouponId);
        if (ObjectUtil.isNull(couponIssueQueryVo)) {
            throw new ErrorRequestException("领取的优惠劵已领完或已过期");
        }
        if (couponIssueQueryVo.getRemainCount() == 0) {
            throw new ErrorRequestException("领取的优惠劵已领完或已过期");
        }
        String []userIDList=uids.split(",");
        Optional<YxStoreCoupon> coupon= yxStoreCouponRepository.findById(couponIssueQueryVo.getCid());
        YxStoreCoupon storeCoupon=coupon.get();
        if (ObjectUtil.isNull(storeCoupon)) {
            throw new ErrorRequestException("优惠劵不存在");
        }
        for(String id:userIDList){
            couponIssueQueryVo =yxStoreCouponIssueRepository.getYxStoreCouponIssueById(issueCouponId);
            if (couponIssueQueryVo.getRemainCount() == 0) {
                break;
            }
            YxStoreCouponIssueUserQueryCriteria issuser=new YxStoreCouponIssueUserQueryCriteria();
            issuser.setIssueCouponId(couponIssueQueryVo.getId());
            issuser.setUid(Integer.parseInt(id));
            List issUserList=yxStoreCouponIssueUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, issuser, criteriaBuilder));
            if (issUserList.size() ==0) {
                if (couponIssueQueryVo.getRemainCount() > 0) {
                    YxStoreCouponUser couponUser = new YxStoreCouponUser();
                    couponUser.setCid(couponIssueQueryVo.getCid());
                    couponUser.setUid(Integer.parseInt(id));
                    couponUser.setCouponTitle(storeCoupon.getTitle());
                    couponUser.setCouponPrice(storeCoupon.getCouponPrice());
                    couponUser.setUseMinPrice(storeCoupon.getUseMinPrice());
                    //int addTime = OrderUtil.getSecondTimestampTwo();
                    couponUser.setAddTime(OrderUtil.getSecondTimestampTwo());
                    //int endTime = addTime + storeCouponQueryVo.getCouponTime() * 86400;
                    couponUser.setCategoryId(couponIssueQueryVo.getCategoryId());
                    couponUser.setCategoryName(couponIssueQueryVo.getCategoryName());
                    couponUser.setType("get");
                    couponUser.setStatus(0);
                    couponUser.setIsFail(0);

                    couponUser.setTimeType(couponIssueQueryVo.getTimeType());
                    couponUser.setTimeNum(couponIssueQueryVo.getTimeNum());
                    if(couponIssueQueryVo.getTimeType()!=null && couponIssueQueryVo.getTimeType().intValue()==0){
                        couponUser.setEndTime(storeCoupon.getCouponTime());
                    }else{
                       int endTime= OrderUtil.getSecondTimestampTwo()+couponIssueQueryVo.getTimeNum().intValue()*86400;
                        couponUser.setEndTime(endTime);
                    }
                    yxStoreCouponUserRepository.save(couponUser);


                    YxStoreCouponIssueUser couponIssueUser = new YxStoreCouponIssueUser();
                    couponIssueUser.setAddTime(OrderUtil.getSecondTimestampTwo());
                    couponIssueUser.setIssueCouponId(issueCouponId);
                    couponIssueUser.setUid(Integer.parseInt(id));
                    yxStoreCouponIssueUserRepository.save(couponIssueUser);

                    if (couponIssueQueryVo.getRemainCount() > 0) {
                        this.yxStoreCouponIssueRepository.decCount(issueCouponId);
                    }
                }


            }
        }



    }
}
