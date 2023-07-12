/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.activity.entity.YxStoreBargain;
import co.yixiang.modules.activity.entity.YxStoreBargainUser;
import co.yixiang.modules.activity.entity.YxStoreBargainUserHelp;
import co.yixiang.modules.activity.mapper.YxStoreBargainMapper;
import co.yixiang.modules.activity.mapping.StoreBargainMap;
import co.yixiang.modules.activity.service.YxStoreBargainService;
import co.yixiang.modules.activity.service.YxStoreBargainUserHelpService;
import co.yixiang.modules.activity.service.YxStoreBargainUserService;
import co.yixiang.modules.activity.web.dto.BargainCountDTO;
import co.yixiang.modules.activity.web.dto.BargainDTO;
import co.yixiang.modules.activity.web.dto.TopCountDTO;
import co.yixiang.modules.activity.web.param.YxStoreBargainQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreBargainQueryVo;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 砍价表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-21
 */
@Slf4j
@Service
public class YxStoreBargainServiceImpl extends BaseServiceImpl<YxStoreBargainMapper, YxStoreBargain> implements YxStoreBargainService {

    @Autowired
    private YxStoreBargainMapper yxStoreBargainMapper;
    @Autowired
    private StoreBargainMap storeBargainMap;

    @Autowired
    private YxStoreBargainUserService storeBargainUserService;
    @Autowired
    private YxUserService userService;
    @Autowired
    private YxStoreOrderService storeOrderService;
    @Autowired
    private YxStoreBargainUserHelpService storeBargainUserHelpService;

    /**
     * 退回库存销量
     * @param num
     * @param bargainId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incStockDecSales(int num, int bargainId) {
        yxStoreBargainMapper.incStockDecSales(num,bargainId);
    }

    /**
     * 增加销量 减少库存
     * @param num
     * @param bargainId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decStockIncSales(int num, int bargainId) {
        yxStoreBargainMapper.decStockIncSales(num,bargainId);
    }

    @Override
    public YxStoreBargain getBargain(int bargainId) {
        QueryWrapper<YxStoreBargain> wrapper = new QueryWrapper<>();
        int nowTime = OrderUtil.getSecondTimestampTwo();
        wrapper.eq("id",bargainId).eq("is_del",0).eq("status",1)
                .le("start_time",nowTime).ge("stop_time",nowTime);
        return yxStoreBargainMapper.selectOne(wrapper);
    }



    /**
     * 开始帮助好友砍价
     * @param bargainId 砍价产品id
     * @param bargainUserUid 开启砍价用户id
     * @param uid 当前用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doHelp(int bargainId, int bargainUserUid, int uid) {
        //开始真正的砍价
        YxStoreBargainUser storeBargainUser = storeBargainUserService
                .getBargainUserInfo(bargainId,bargainUserUid);

        //判断是否已经帮助过
        int count = storeBargainUserHelpService.count(new QueryWrapper<YxStoreBargainUserHelp>()
                .eq("bargain_id",bargainId)
                .eq("bargain_user_id",storeBargainUser.getId())
                .eq("uid",uid));
        if(count > 0) {
            return;
        }

        YxStoreBargainQueryVo storeBargainQueryVo = getYxStoreBargainById(bargainId);
        //用户可以砍掉的金额 好友砍价之前获取可以砍价金额
        double coverPrice = NumberUtil.sub(storeBargainUser.getBargainPrice()
                ,storeBargainUser.getBargainPriceMin()).doubleValue();

        double random = 0d;
        if(coverPrice > 0 ){
            //用户剩余要砍掉的价格
            double surplusPrice = NumberUtil.sub(coverPrice,
                    storeBargainUser.getPrice()).doubleValue();
            if(surplusPrice == 0) {
                return;
            }


            //生成一个区间随机数
            random = OrderUtil.randomNumber(
                    storeBargainQueryVo.getBargainMinPrice().doubleValue(),
                    storeBargainQueryVo.getBargainMaxPrice().doubleValue());
            if(random > surplusPrice) {
                random = surplusPrice;
            }
        }


        //添加砍价帮助表
        YxStoreBargainUserHelp storeBargainUserHelp = YxStoreBargainUserHelp
                .builder()
                .uid(uid)
                .bargainId(bargainId)
                .bargainUserId(storeBargainUser.getId())
                .price(BigDecimal.valueOf(random))
                .addTime(OrderUtil.getSecondTimestampTwo())
                .build();
        storeBargainUserHelpService.save(storeBargainUserHelp);

        //更新砍价参与表
        YxStoreBargainUser bargainUser = YxStoreBargainUser
                .builder()
                .id(storeBargainUser.getId())
                .price(BigDecimal.valueOf(NumberUtil.add(storeBargainUser.getPrice().doubleValue(),random)))
                .build();

        storeBargainUserService.updateById(bargainUser);
    }

    /**
     * 顶部统计
     * @param bargainId
     * @return
     */
    @Override
    public TopCountDTO topCount(int bargainId) {
        TopCountDTO topCountDTO =  TopCountDTO.builder()
                .lookCount(yxStoreBargainMapper.lookCount())
                .shareCount(yxStoreBargainMapper.shareCount())
                .userCount(storeBargainUserService.count())
                .build();
        if(bargainId > 0) {
            addBargainShare(bargainId);
        }
        return topCountDTO;
    }

    /**
     * 砍价 砍价帮总人数、剩余金额、进度条、已经砍掉的价格
     * @param bargainId
     * @param uid
     */
    @Override
    public BargainCountDTO helpCount(int bargainId, int uid,int myUid) {
        YxStoreBargainUser storeBargainUser = storeBargainUserService
                .getBargainUserInfo(bargainId,uid);

        boolean userBargainStatus = true;
        if(ObjectUtil.isNull(storeBargainUser)) {
            BargainCountDTO bargainCountDTO = BargainCountDTO
                    .builder()
                    .count(0)
                    .alreadyPrice(0d)
                    .status(0)
                    .pricePercent(0)
                    .price(0d)
                    .userBargainStatus(userBargainStatus)
                    .build();

            return bargainCountDTO;
        }

        int helpCount = storeBargainUserHelpService.count(new QueryWrapper<YxStoreBargainUserHelp>()
                .eq("bargain_user_id", storeBargainUser.getId())
                .eq("bargain_id",bargainId).eq("uid",myUid));

        if(helpCount > 0) {
            userBargainStatus = false;
        }


        int count = storeBargainUserHelpService
                .getBargainUserHelpPeopleCount(bargainId,storeBargainUser.getId());
        //用户可以砍掉的价格
        double diffPrice = NumberUtil.sub(storeBargainUser.getBargainPrice()
                ,storeBargainUser.getBargainPriceMin()).doubleValue();
        //砍价进度条百分比
        int pricePercent = 0;
        if(diffPrice <= 0) {
            pricePercent = 100;
        }else{
            pricePercent = NumberUtil.round(NumberUtil.mul(NumberUtil.div(
                    storeBargainUser.getPrice(),diffPrice),100)
                    ,0).intValue();
        }


        BargainCountDTO bargainCountDTO = BargainCountDTO
                .builder()
                .count(count)
                .alreadyPrice(storeBargainUser.getPrice().doubleValue())
                .status(storeBargainUser.getStatus())
                .pricePercent(pricePercent)
                .price(NumberUtil.sub(diffPrice,storeBargainUser.getPrice()).doubleValue())
                .userBargainStatus(userBargainStatus)
                .build();

        return bargainCountDTO;
    }

    /**
     * 砍价支付成功订单数量
     * @param bargainId 砍价id
     * @return int
     */
    @Override
    public int getBargainPayCount(int bargainId) {
        return storeOrderService.count(new QueryWrapper<YxStoreOrder>()
                .eq("bargain_id", bargainId).eq("paid",1).eq("refund_status",0));
    }

    /**
     * 增加分享次数
     * @param id
     */
    @Override
    public void addBargainShare(int id) {
        yxStoreBargainMapper.addBargainShare(id);
    }

    /**
     * 增加浏览次数
     * @param id
     */
    @Override
    public void addBargainLook(int id) {
        yxStoreBargainMapper.addBargainLook(id);
    }

    /**
     * 砍价详情
     * @param id 砍价id
     * @param uid 用户id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BargainDTO getDetail(int id, int uid) {
        QueryWrapper<YxStoreBargain> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del",0).eq("status",1).eq("id",id);

        YxStoreBargain storeBargain = yxStoreBargainMapper.selectOne(wrapper);
        if(ObjectUtil.isNull(storeBargain)) {
            throw new ErrorRequestException("砍价已结束");
        }

        addBargainLook(id);

        YxStoreBargainQueryVo storeBargainQueryVo = storeBargainMap.toDto(storeBargain);

        BargainDTO bargainDTO = BargainDTO
                .builder()
                .bargain(storeBargainQueryVo)
                .userInfo(userService.getYxUserById(uid))
                .bargainSumCount(getBargainPayCount(id))
                .build();

        return bargainDTO;
    }

    /**
     * 获取砍价商品列表
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<YxStoreBargainQueryVo> getList(int page, int limit) {
        Page<YxStoreBargain> pageModel = new Page<>(page, limit);
        QueryWrapper<YxStoreBargain> wrapper = new QueryWrapper<>();
        int nowTime = OrderUtil.getSecondTimestampTwo();
        wrapper.eq("is_del",0).eq("status",1)
                .lt("start_time",nowTime).gt("stop_time",nowTime);

        List<YxStoreBargainQueryVo> yxStoreBargainQueryVos = storeBargainMap
                .toDto(yxStoreBargainMapper.selectPage(pageModel,wrapper).getRecords());

        yxStoreBargainQueryVos.forEach(item->{
            item.setPeople(storeBargainUserService.getBargainUserList(item.getId(),1).size());
        });

        return yxStoreBargainQueryVos;
    }

    @Override
    public YxStoreBargainQueryVo getYxStoreBargainById(Serializable id){
        return yxStoreBargainMapper.getYxStoreBargainById(id);
    }

    @Override
    public Paging<YxStoreBargainQueryVo> getYxStoreBargainPageList(YxStoreBargainQueryParam yxStoreBargainQueryParam) throws Exception{
        Page page = setPageParam(yxStoreBargainQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreBargainQueryVo> iPage = yxStoreBargainMapper.getYxStoreBargainPageList(page,yxStoreBargainQueryParam);
        return new Paging(iPage);
    }

}
