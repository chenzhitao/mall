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
import co.yixiang.modules.activity.entity.YxStoreSeckill;
import co.yixiang.modules.activity.mapper.YxStoreSeckillMapper;
import co.yixiang.modules.activity.mapping.StoreSeckillMap;
import co.yixiang.modules.activity.service.YxStoreSeckillService;
import co.yixiang.modules.activity.web.dto.StoreSeckillDTO;
import co.yixiang.modules.activity.web.param.YxStoreSeckillQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreSeckillQueryVo;
import co.yixiang.modules.shop.service.YxStoreProductReplyService;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 商品秒杀产品表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-12-14
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreSeckillServiceImpl extends BaseServiceImpl<YxStoreSeckillMapper, YxStoreSeckill> implements YxStoreSeckillService {

    private final YxStoreSeckillMapper yxStoreSeckillMapper;
    private final StoreSeckillMap storeSeckillMap;

    private final YxStoreProductReplyService replyService;


    /**
     * 退回库存减少销量
     * @param num
     * @param seckillId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incStockDecSales(int num, int seckillId) {
        yxStoreSeckillMapper.incStockDecSales(num,seckillId);
    }

    /**
     * 减库存增加销量
     * @param num
     * @param seckillId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decStockIncSales(int num, int seckillId) {
        yxStoreSeckillMapper.decStockIncSales(num,seckillId);
    }

    @Override
    public YxStoreSeckill getSeckill(int id) {
        QueryWrapper<YxStoreSeckill> wrapper = new QueryWrapper<>();
        int nowTime = OrderUtil.getSecondTimestampTwo();
        wrapper.eq("id",id).eq("is_del",0).eq("status",1)
                .le("start_time",nowTime).ge("stop_time",nowTime);
        return yxStoreSeckillMapper.selectOne(wrapper);
    }

    /**
     * 产品详情
     * @param id
     * @return
     */
    @Override
    public StoreSeckillDTO getDetail(int id) throws Exception{
        YxStoreSeckillQueryVo yxStoreSeckillQueryVo =  getYxStoreSeckillById(id);

        if(ObjectUtil.isNull(yxStoreSeckillQueryVo)){
            throw new ErrorRequestException("秒杀产品不存在或已下架");
        }

        StoreSeckillDTO storeSeckillDTO = StoreSeckillDTO.builder()
                .storeInfo(yxStoreSeckillQueryVo)
                .reply(replyService.getReply(yxStoreSeckillQueryVo.getProductId()))
                .replyCount(replyService.productReplyCount(yxStoreSeckillQueryVo.getProductId()))
                .build();


        return storeSeckillDTO;
    }

    /**
     * 秒杀产品列表
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<YxStoreSeckillQueryVo> getList(int page, int limit, int time) {
        int nowTime = OrderUtil.getSecondTimestampTwo();
        Page<YxStoreSeckill> pageModel = new Page<>(page, limit);
        QueryWrapper<YxStoreSeckill> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del",0).eq("status",1)
                .eq("time_id",time)
                .le("start_time",nowTime).ge("stop_time",nowTime).orderByDesc("sort");
        List<YxStoreSeckillQueryVo> yxStoreSeckillQueryVos = storeSeckillMap
                .toDto(yxStoreSeckillMapper.selectPage(pageModel,wrapper).getRecords());
        yxStoreSeckillQueryVos.forEach(item->{
            Integer sum = item.getSales() + item.getStock();
            item.setPercent(NumberUtil.round(NumberUtil.mul(NumberUtil.div(item.getSales(),sum),
                    100),0).intValue());
        });
        return yxStoreSeckillQueryVos;
    }

    @Override
    public YxStoreSeckillQueryVo getYxStoreSeckillById(Serializable id) throws Exception{

        return yxStoreSeckillMapper.getYxStoreSeckillById(id);
    }

    @Override
    public Paging<YxStoreSeckillQueryVo> getYxStoreSeckillPageList(YxStoreSeckillQueryParam yxStoreSeckillQueryParam) throws Exception{
        Page page = setPageParam(yxStoreSeckillQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreSeckillQueryVo> iPage = yxStoreSeckillMapper.getYxStoreSeckillPageList(page,yxStoreSeckillQueryParam);
        return new Paging(iPage);
    }

}
