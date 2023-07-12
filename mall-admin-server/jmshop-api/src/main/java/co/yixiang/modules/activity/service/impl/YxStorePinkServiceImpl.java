/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.activity.entity.YxStorePink;
import co.yixiang.modules.activity.mapper.YxStoreCombinationMapper;
import co.yixiang.modules.activity.mapper.YxStorePinkMapper;
import co.yixiang.modules.activity.mapping.StorePinkMap;
import co.yixiang.modules.activity.service.YxStoreCombinationService;
import co.yixiang.modules.activity.service.YxStorePinkService;
import co.yixiang.modules.activity.web.dto.PinkDTO;
import co.yixiang.modules.activity.web.dto.PinkInfoDTO;
import co.yixiang.modules.activity.web.param.YxStorePinkQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreCombinationQueryVo;
import co.yixiang.modules.activity.web.vo.YxStorePinkQueryVo;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.order.web.param.RefundParam;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 拼团表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStorePinkServiceImpl extends BaseServiceImpl<YxStorePinkMapper, YxStorePink> implements YxStorePinkService {

    @Autowired
    private YxStorePinkMapper yxStorePinkMapper;
    @Autowired
    private YxStoreCombinationMapper yxStoreCombinationMapper;

    @Autowired
    private YxStoreCombinationService combinationService;
    @Autowired
    private YxStoreOrderService storeOrderService;
    @Autowired
    private YxUserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private StorePinkMap pinkMap;


    /**
     * 取消拼团
     * @param uid
     * @param cid
     * @param pinkId
     */
    @Override
    public void removePink(int uid, int cid, int pinkId) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.eq("id",pinkId).eq("uid",uid)
                .eq("cid",cid).eq("k_id",0).eq("is_refund",0)
                .eq("status",1).gt("stop_time",OrderUtil.getSecondTimestampTwo());
        YxStorePink pink = yxStorePinkMapper.selectOne(wrapper);

        if(pink == null) {
            throw new ErrorRequestException("拼团不存在或已经取消");
        }

        Map<String, Object> map = getPinkMemberAndPinK(pink);
        List<YxStorePink> pinkAll = (List<YxStorePink>)map.get("pinkAll");
        YxStorePink pinkT = (YxStorePink)map.get("pinkT");
        List<Integer> idAll = (List<Integer>)map.get("idAll");
        List<Integer> uidAll = (List<Integer>)map.get("uidAll");
        int count = (int)map.get("count");
        if(count < 1){
            pinkComplete(uidAll,idAll,uid,pinkT);

            throw new ErrorRequestException("拼团已完成，无法取消");
        }
        //如果团长取消拼团，团队还有人，就把后面的人作为下一任团长
        YxStorePink nextPinkT = null;
        if(pinkAll.size() > 0){
            nextPinkT = pinkAll.get(0);
        }

        //先退团长的money
        RefundParam param = new RefundParam();
        param.setUni(pinkT.getOrderId());
        param.setText("拼团取消开团");
        storeOrderService.orderApplyRefund(param,pinkT.getUid());
        orderPinkFailAfter(pinkT.getUid(),pinkT.getId());

        if(ObjectUtil.isNotNull(nextPinkT)){
            QueryWrapper<YxStorePink> wrapperO = new QueryWrapper<>();
            YxStorePink storePinkO = new YxStorePink();
            storePinkO.setKId(0);
            storePinkO.setStatus(1);
            storePinkO.setStopTime(OrderUtil.getSecondTimestampTwo()+"");
            storePinkO.setId(nextPinkT.getId());
            yxStorePinkMapper.updateById(storePinkO);

            //原有团长的数据变更成新团长下面
            wrapperO.eq("k_id",pinkT.getId());
            YxStorePink storePinkT = new YxStorePink();
            storePinkT.setKId(nextPinkT.getId());
            yxStorePinkMapper.update(storePinkT,wrapperO);

            //update order

            YxStoreOrder storeOrder = new YxStoreOrder();
            storeOrder.setPinkId(nextPinkT.getId());
            storeOrder.setId(nextPinkT.getId());

            storeOrderService.updateById(storeOrder);


        }
    }

    /**
     * 计算还差几人拼团
     * @param pink
     * @return
     */
    @Override
    public int surplusPeople(YxStorePink pink) {
        List<YxStorePink> listT = new ArrayList<>();
        if(pink.getKId() > 0){
            listT = getPinkMember(pink.getKId());
        }else{
            listT = getPinkMember(pink.getId());
        }

        return pink.getPeople() - (listT.size() + 1);
    }

    /**
     * 处理团员
     * @param pinkAll
     * @return
     */
    @Override
    public List<YxStorePinkQueryVo> handPinkAll(List<YxStorePink> pinkAll) {

        List<YxStorePinkQueryVo> list = pinkMap.toDto(pinkAll);
        for (YxStorePinkQueryVo queryVo : list) {
            YxUserQueryVo userQueryVo = userService.getYxUserById(queryVo.getUid());
            queryVo.setAvatar(userQueryVo.getAvatar());
            queryVo.setNickname(userQueryVo.getNickname());
        }
        return list;
    }

    /**
     * 处理团长
     * @param pinkT
     * @return
     */
    @Override
    public YxStorePinkQueryVo handPinkT(YxStorePink pinkT) {
        YxStorePinkQueryVo pinkQueryVo = pinkMap.toDto(pinkT);
        YxUserQueryVo userQueryVo = userService.getYxUserById(pinkQueryVo.getUid());
        pinkQueryVo.setAvatar(userQueryVo.getAvatar());
        pinkQueryVo.setNickname(userQueryVo.getNickname());

        return pinkQueryVo;
    }


    @Override
    public String getCurrentPinkOrderId(int id, int uid) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id).eq("uid",uid);
        YxStorePink pink = yxStorePinkMapper.selectOne(wrapper);
        if(ObjectUtil.isNull(pink)){
            QueryWrapper<YxStorePink> wrapperT = new QueryWrapper<>();
            wrapperT.eq("k_id",id).eq("uid",uid);
            pink = yxStorePinkMapper.selectOne(wrapperT);
            if(ObjectUtil.isNull(pink)) {
                return "";
            }
        }
        return pink.getOrderId();
    }

    /**
     * 获取当前拼团数据
     * @param id
     * @param uid
     * @return
     */
    @Override
    public YxStorePink getCurrentPink(int id, int uid) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id).eq("uid",uid);
        YxStorePink pink = yxStorePinkMapper.selectOne(wrapper);
        if(ObjectUtil.isNull(pink)){
            QueryWrapper<YxStorePink> wrapperT = new QueryWrapper<>();
            wrapperT.eq("k_id",id).eq("uid",uid);
            pink = yxStorePinkMapper.selectOne(wrapperT);
            if(ObjectUtil.isNull(pink)) {
                pink.setOrderId("");
            }
        }
        return pink;
    }

    /**
     * 拼团明细
     * @param id
     * @param uid
     */
    @Override
    public PinkInfoDTO pinkInfo(int id, int uid) {
        PinkDTO pinkDTO = getPinkUserOneT(id);
        if(ObjectUtil.isNull(pinkDTO)) {
            throw new ErrorRequestException("拼团不存在");
        }

        PinkInfoDTO infoDTO = new PinkInfoDTO();

        YxStorePink pink = getPinkUserOne(id);
        if(pink.getIsRefund() > 0){
            throw new ErrorRequestException("订单已退款");
        }

        int isOk = 0;//判断拼团是否完成
        int userBool = 0;//判断当前用户是否在团内  0未在 1在
        int pinkBool = 0;//判断拼团是否成功  0未 1是 -1结束

        Map<String, Object> map = getPinkMemberAndPinK(pink);
        List<YxStorePink> pinkAll = (List<YxStorePink>)map.get("pinkAll");
        YxStorePink pinkT = (YxStorePink)map.get("pinkT");
        List<Integer> idAll = (List<Integer>)map.get("idAll");
        List<Integer> uidAll = (List<Integer>)map.get("uidAll");
        int count = (int)map.get("count");
        if(count <= 0) {
            count = 0;
        }
        if(pinkT.getStatus() == 2){
            pinkBool = 1;
            isOk = 1;

        }else if(pinkT.getStatus() == 3){
            pinkBool = -1;
            isOk = 0;
        }else{
            if(count < 1){//组团完成
                isOk = 1;
                pinkBool = pinkComplete(uidAll,idAll,uid,pinkT);
            }else{
                pinkBool = pinkFail(pinkAll,pinkT,pinkBool);
            }
        }

        //团员
        if(ObjectUtil.isNotNull(pinkAll)){
            for (YxStorePink storePink : pinkAll) {
                if(storePink.getUid() == uid) {
                    userBool = 1;
                }
            }
        }

        //团长
        if(pinkT.getUid() == uid) {
            userBool = 1;
        }

        YxStoreCombinationQueryVo storeCombinationQueryVo = yxStoreCombinationMapper
                .getCombDetail(pink.getCid());
        if(ObjectUtil.isNull(storeCombinationQueryVo)) {
            throw new ErrorRequestException("拼团不存在或已下架");
        }

        YxUserQueryVo userInfo = userService.getYxUserById(uid);

        infoDTO.setCount(count);
        infoDTO.setCurrentPinkOrder(getCurrentPinkOrderId(id,uid));
        infoDTO.setIsOk(isOk);
        infoDTO.setPinkAll(handPinkAll(pinkAll));
        infoDTO.setPinkBool(pinkBool);
        infoDTO.setPinkT(handPinkT(pinkT));
        infoDTO.setStoreCombination(storeCombinationQueryVo);
        infoDTO.setUserBool(userBool);
        infoDTO.setUserInfo(userInfo);

        return infoDTO;

    }

    @Override
    public PinkDTO getPinkUserOneT(int id) {
        return yxStorePinkMapper.getPinkUserOne(id);
    }

    @Override
    public int pinkIngCount(int id) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id).eq("status",1);
        return yxStorePinkMapper.selectCount(wrapper);
    }

    /**
     * 获取拼团的团员
     * @param kid
     * @return
     */
    @Override
    public List<YxStorePink> getPinkMember(int kid) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.eq("k_id",kid).eq("is_refund",0).orderByAsc("id");
        return yxStorePinkMapper.selectList(wrapper);
    }

    /**
     * 获取一条拼团数据
     * @param id
     * @return
     */
    @Override
    public YxStorePink getPinkUserOne(int id) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        return yxStorePinkMapper.selectOne(wrapper);
    }

    /**
     * 拼团人数完成时，判断全部人都是未退款状态
     * @return
     */
    @Override
    public boolean getPinkStatus(List<Integer> idAll) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.in("id",idAll).eq("is_refund",0);
        int count = yxStorePinkMapper.selectCount(wrapper);
        if(count == 0) {
            return true;
        }
        return false;
    }

    /**
     * 设置结束时间
     * @param idAll
     */
    @Override
    public void setPinkStopTime(List<Integer> idAll) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.in("id",idAll);

        YxStorePink storePink = new YxStorePink();
        storePink.setStopTime(OrderUtil.getSecondTimestampTwo()+"");
        storePink.setStatus(2);

        yxStorePinkMapper.update(storePink,wrapper);
    }

    /**
     * 拼团完成更改数据写入内容
     * @param uidAll
     * @param idAll
     * @param uid
     * @param pinkT
     */
    @Override
    public int pinkComplete(List<Integer> uidAll,List<Integer> idAll,int uid,
                             YxStorePink pinkT) {
        boolean pinkStatus = getPinkStatus(idAll);
        int pinkBool = 6;
        if(!pinkStatus){
            setPinkStopTime(idAll);//更改状态
            if(uidAll.contains(uid)){
                pinkBool = 1;
            }else{
                pinkBool = 3;
            }
            //todo 模板消息
        }

        return pinkBool;

    }

    /**
     * 拼团失败退款之后
     * @param uid
     * @param pid
     */
    @Override
    public void orderPinkFailAfter(int uid, int pid) {
        YxStorePink yxStorePink = new YxStorePink();
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.eq("id",pid);
        yxStorePink.setStatus(3);
        yxStorePink.setStopTime(OrderUtil.getSecondTimestampTwo()+"");
        yxStorePinkMapper.update(yxStorePink,wrapper);

        QueryWrapper<YxStorePink> wrapperT = new QueryWrapper<>();
        wrapperT.eq("k_id",pid);
        yxStorePinkMapper.update(yxStorePink,wrapperT);
        //todo 模板消息
    }

    /**
     * 拼团失败 退款
     * @param pinkAll 拼团数据,不包括团长
     * @param pinkT 团长数据
     * @param pinkBool
     */
    @Override
    public int pinkFail(List<YxStorePink> pinkAll, YxStorePink pinkT,int pinkBool) {
        int now = OrderUtil.getSecondTimestampTwo();

        //拼团时间超时  退款
        if(Integer.valueOf(pinkT.getStopTime()) < now){
            pinkBool = -1;
            pinkAll.add(pinkT);
            for (YxStorePink storePink : pinkAll) {
                RefundParam param = new RefundParam();
                param.setUni(storePink.getOrderId());
                param.setText("拼团时间超时");
                storeOrderService.orderApplyRefund(param,storePink.getUid());
                orderPinkFailAfter(pinkT.getUid(),storePink.getId());
            }
        }

        return pinkBool;
    }

    /**
     * 获取参团人和团长和拼团总人数
     * @param pink
     * @return
     */
    @Override
    public Map<String, Object> getPinkMemberAndPinK(YxStorePink pink) {
        Map<String, Object> map = new LinkedHashMap<>();
        //查找拼团团员和团长
        List<YxStorePink> pinkAll = null;
        YxStorePink pinkT = null;
        List<Integer> idAll = new ArrayList<>();
        List<Integer> uidAll = new ArrayList<>();
        int count = 0;
        if(pink.getKId() > 0){
            pinkAll = getPinkMember(pink.getKId());
            pinkT =  getPinkUserOne(pink.getKId());
        }else{
            pinkAll = getPinkMember(pink.getId());
            pinkT =  pink;
        }
        //收集拼团用户id和拼团id
        for (YxStorePink storePink : pinkAll) {
            idAll.add(storePink.getId());
            uidAll.add(storePink.getUid());
        }
        idAll.add(pinkT.getId());
        uidAll.add(pinkT.getUid());
        //还差几人
        count =  pinkT.getPeople() - (pinkAll.size() + 1);

        map.put("pinkAll",pinkAll);
        map.put("pinkT",pinkT);
        map.put("count",count);
        map.put("idAll",idAll);
        map.put("uidAll",uidAll);

        return map;
    }

    /**
     * 创建拼团
     * @param order
     */
    @Override
    public void createPink(YxStoreOrderQueryVo order) {
        YxStoreCombinationQueryVo combinationQueryVo = combinationService
                .getYxStoreCombinationById(order.getCombinationId());
        order = storeOrderService.handleOrder(order);
        int pinkCount = yxStorePinkMapper.selectCount(Wrappers.<YxStorePink>lambdaQuery()
                .eq(YxStorePink::getOrderId,order.getOrderId()));
        if(pinkCount > 0) {
            return;
        }
        if(ObjectUtil.isNotNull(combinationQueryVo)){
            YxStorePink  storePink = new YxStorePink();
            storePink.setUid(order.getUid());
            storePink.setOrderId(order.getOrderId());
            storePink.setOrderIdKey(order.getId());
            storePink.setTotalNum(order.getTotalNum());
            storePink.setTotalPrice(order.getPayPrice());
            storePink.setKId(0);
            List<YxStoreCartQueryVo> cartInfo = order.getCartInfo();
            for (YxStoreCartQueryVo queryVo : cartInfo) {
                storePink.setCid(queryVo.getCombinationId());
                storePink.setPid(queryVo.getProductId());
                storePink.setPrice(queryVo.getProductInfo().getPrice());
            }
            int nowTime = OrderUtil.getSecondTimestampTwo();
            int stopTime = nowTime +(combinationQueryVo.getEffectiveTime()*3600);
            storePink.setPeople(combinationQueryVo.getPeople());
            storePink.setStopTime(stopTime+"");
            storePink.setAddTime(nowTime+"");
            if(order.getPinkId() > 0){
                if(getIsPinkUid(order.getPinkId(),order.getUid()) > 0) {
                    return;
                }
                storePink.setKId(order.getPinkId());
                storePink.setStopTime("0");
                save(storePink);

                //处理拼团完成
                Map<String, Object> map = getPinkMemberAndPinK(storePink);
                YxStorePink pinkT = (YxStorePink)map.get("pinkT");
                if(pinkT.getStatus() == 1){
                    int count = (int)map.get("count");
                    if(count == 0){//处理成功
                        pinkComplete((List<Integer>)map.get("uidAll"),
                                (List<Integer>)map.get("idAll"),order.getUid(),
                                pinkT);
                    }else{
                        pinkFail((List<YxStorePink>)map.get("pinkAll"),pinkT,0);
                    }
                }

            }else{
                save(storePink);
                //pink_id更新到order表
                YxStoreOrder yxStoreOrder =  new YxStoreOrder();
                yxStoreOrder.setPinkId(storePink.getId());
                yxStoreOrder.setId(order.getId());
                storeOrderService.updateById(yxStoreOrder);

                //开团加入队列
                String redisKey = String.valueOf(StrUtil.format("{}{}",
                        ShopConstants.REDIS_PINK_CANCEL_KEY, storePink.getId()));
                redisTemplate.opsForValue().set(redisKey, "1" , stopTime, TimeUnit.SECONDS);

            }

            //todo 模板消息
        }
    }

    /**
     * 判断用户是否在团内
     * @param id
     * @param uid
     * @return
     */
    @Override
    public int getIsPinkUid(int id, int uid) {
        QueryWrapper<YxStorePink> wrapper = new QueryWrapper<>();
        wrapper.eq("is_refund",0).eq("uid",uid).and(
                i->i.eq("k_id",id).or().eq("id",id));
        return yxStorePinkMapper.selectCount(wrapper);
    }

    /**
     * 获取拼团完成的商品总件数
     * @return
     */
    @Override
    public int getPinkOkSumTotalNum() {
        return yxStorePinkMapper.sumNum();
    }

    /**
     * 获取拼团完成的用户
     * @param uid
     * @return
     */
    @Override
    public List<String> getPinkOkList(int uid) {
        List<String> list = new ArrayList<>();
        List<PinkDTO> pinkDTOList = yxStorePinkMapper.getPinkOkList(uid);
        for (PinkDTO pinkDTO : pinkDTOList) {
            list.add(pinkDTO.getNickname()+"拼团成功");
        }
        //list = pinkDTOList.stream().map(PinkDTO::getNickname).collect(Collectors.toList());
        return list;
    }

    @Override
    public int getPinkPeople(int kid, int people) {
        QueryWrapper<YxStorePink> wrapper= new QueryWrapper<>();
        wrapper.eq("k_id",kid).eq("is_refund",0);
        int count = yxStorePinkMapper.selectCount(wrapper) + 1;
        return people - count;
    }

    /**
     * 获取团长拼团数据
     * @param cid
     * @param isAll
     * @return
     */
    @Override
    public Map<String,Object> getPinkAll(int cid, boolean isAll) {
        Map<String,Object> map = new LinkedHashMap<>();
        List<PinkDTO> list = yxStorePinkMapper.getPinks(cid);
        if(isAll){
            List<Integer> pindAll = new ArrayList<>();
            for (PinkDTO pinkDTO : list) {
                pinkDTO.setCount(String.valueOf(getPinkPeople(pinkDTO.getId()
                        ,pinkDTO.getPeople())));
                Date date = DateUtil.parse(OrderUtil.stampToDate(pinkDTO.getStopTime().toString()));
                System.out.println(date);
                pinkDTO.setH(String.valueOf(DateUtil.hour(date,true)));
                pinkDTO.setI(String.valueOf(DateUtil.minute(date)));
                pinkDTO.setS(String.valueOf(DateUtil.second(date)));
                pindAll.add(pinkDTO.getId());
            }

            map.put("pindAll",pindAll);

        }

        map.put("list",list);

        return map;

    }

    @Override
    public YxStorePinkQueryVo getYxStorePinkById(Serializable id){
        return yxStorePinkMapper.getYxStorePinkById(id);
    }

    @Override
    public Paging<YxStorePinkQueryVo> getYxStorePinkPageList(YxStorePinkQueryParam yxStorePinkQueryParam) throws Exception{
        Page page = setPageParam(yxStorePinkQueryParam,OrderItem.desc("create_time"));
        IPage<YxStorePinkQueryVo> iPage = yxStorePinkMapper.getYxStorePinkPageList(page,yxStorePinkQueryParam);
        return new Paging(iPage);
    }

}
