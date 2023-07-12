package co.yixiang.modules.activity.service.impl;

import cn.hutool.core.date.DateUtil;
import co.yixiang.modules.activity.domain.YxStoreCouponUser;
import co.yixiang.modules.activity.repository.YxStoreCouponUserRepository;
import co.yixiang.modules.activity.service.YxStoreCouponUserService;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueUserDTO;
import co.yixiang.modules.activity.service.dto.YxStoreCouponIssueUserQueryCriteria;
import co.yixiang.modules.activity.service.dto.YxStoreCouponUserDTO;
import co.yixiang.modules.activity.service.dto.YxStoreCouponUserQueryCriteria;
import co.yixiang.modules.activity.service.mapper.YxStoreCouponUserMapper;
import co.yixiang.modules.shop.service.YxUserService;
import co.yixiang.modules.shop.service.YxWechatUserService;
import co.yixiang.modules.shop.service.dto.YxUserDTO;
import co.yixiang.modules.shop.service.dto.YxWechatUserDTO;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.QueryHelp;
import co.yixiang.utils.TencentMsgUtil;
import co.yixiang.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
* @author hupeng
* @date 2019-11-10
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxStoreCouponUserServiceImpl implements YxStoreCouponUserService {

    private final YxStoreCouponUserRepository yxStoreCouponUserRepository;

    private final YxStoreCouponUserMapper yxStoreCouponUserMapper;

    private final YxUserService userService;

    private final YxWechatUserService wechatUserService;

    public YxStoreCouponUserServiceImpl(YxStoreCouponUserRepository yxStoreCouponUserRepository, YxStoreCouponUserMapper yxStoreCouponUserMapper, YxUserService userService,YxWechatUserService wechatUserService) {
        this.yxStoreCouponUserRepository = yxStoreCouponUserRepository;
        this.yxStoreCouponUserMapper = yxStoreCouponUserMapper;
        this.userService = userService;
        this.wechatUserService = wechatUserService;
    }

    @Override
    public Map<String,Object> queryAll(YxStoreCouponUserQueryCriteria criteria, Pageable pageable){
        Page<YxStoreCouponUser> page = yxStoreCouponUserRepository.
                findAll((root, criteriaQuery, criteriaBuilder)
                        -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        //List<YxStoreCouponUserDTO> storeOrderDTOS = new ArrayList<>();
        List<YxStoreCouponUserDTO> storeOrderDTOS = yxStoreCouponUserMapper
                .toDto(page.getContent());
        for (YxStoreCouponUserDTO couponUserDTO : storeOrderDTOS) {
            couponUserDTO.setNickname(userService.findById(couponUserDTO.getUid()).getNickname());
        }
        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",storeOrderDTOS);
        map.put("totalElements",page.getTotalElements());

        return map;
        //return PageUtil.toPage(page.map(yxStoreCouponUserMapper::toDto));
    }

    @Override
    public List<YxStoreCouponUserDTO> queryAll(YxStoreCouponUserQueryCriteria criteria){
        return yxStoreCouponUserMapper.toDto(yxStoreCouponUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public YxStoreCouponUserDTO findById(Integer id) {
        Optional<YxStoreCouponUser> yxStoreCouponUser = yxStoreCouponUserRepository.findById(id);
        ValidationUtil.isNull(yxStoreCouponUser,"YxStoreCouponUser","id",id);
        return yxStoreCouponUserMapper.toDto(yxStoreCouponUser.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public YxStoreCouponUserDTO create(YxStoreCouponUser resources) {
        return yxStoreCouponUserMapper.toDto(yxStoreCouponUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(YxStoreCouponUser resources) {
        Optional<YxStoreCouponUser> optionalYxStoreCouponUser = yxStoreCouponUserRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalYxStoreCouponUser,"YxStoreCouponUser","id",resources.getId());
        YxStoreCouponUser yxStoreCouponUser = optionalYxStoreCouponUser.get();
        yxStoreCouponUser.copy(resources);
        yxStoreCouponUserRepository.save(yxStoreCouponUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        yxStoreCouponUserRepository.deleteById(id);
    }

    @Override
    public void autoNoticeExpireUser() {
        YxStoreCouponUserQueryCriteria queryParam=new YxStoreCouponUserQueryCriteria();
        queryParam.setStatus(0);
        List<YxStoreCouponUserDTO> couponList= yxStoreCouponUserMapper.toDto(yxStoreCouponUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,queryParam,criteriaBuilder)));
        couponList.stream().forEach(userCoupon->{
            int currentTIme=OrderUtil.getSecondTimestampTwo();
            if(userCoupon.getEndTime()> currentTIme){
               int hour= (userCoupon.getEndTime()-currentTIme)/86400;
               if(hour>0 && hour<=1){
                   YxWechatUserDTO wechatUser = wechatUserService.findById(userCoupon.getUid());
                   //发送小程序消息
                   Map<String, Object> data = new HashMap<>();

                   Map<String, Object> value = new HashMap();
                   value.put("value",userCoupon.getCouponTitle() );
                   data.put("thing1", value);

                   Map<String, Object> value1 = new HashMap();
                   value1.put("value", "优惠券即将过期");
                   data.put("thing2", value1);

                   Map<String, Object> value2 = new HashMap();
                   value2.put("value", userCoupon.getCouponPrice());
                   data.put("amount6", value2);

                   Map<String, Object> value3 = new HashMap();
                   value3.put("value", OrderUtil.stampToDate(String.valueOf(userCoupon.getEndTime())));
                   data.put("time3", value3);
                   //下单成功发送消息
                   TencentMsgUtil.sendTemplateMsg(data, "7nm40h9AlLukcnn-8I7cCzXixmMnvoIDvqifdLGTaSU",wechatUser.getRoutineOpenid());
               }
            }
        });

    }

    public static void main(String[] args) {
        long endtime=OrderUtil.getSecondTimestampTwo()+86400+120;
        System.out.println(endtime);
        System.out.println(OrderUtil.getSecondTimestampTwo()+86400);
        int currentTIme=OrderUtil.getSecondTimestampTwo();

            long hour= (1688131317-currentTIme)/86400;
        System.out.println(hour);
    }
}
