/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.activity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.activity.entity.YxStoreCombination;
import co.yixiang.modules.activity.mapper.YxStoreCombinationMapper;
import co.yixiang.modules.activity.service.YxStoreCombinationService;
import co.yixiang.modules.activity.service.YxStorePinkService;
import co.yixiang.modules.activity.web.dto.PinkDTO;
import co.yixiang.modules.activity.web.dto.StoreCombinationDTO;
import co.yixiang.modules.activity.web.param.YxStoreCombinationQueryParam;
import co.yixiang.modules.activity.web.vo.YxStoreCombinationQueryVo;
import co.yixiang.modules.shop.service.YxStoreProductReplyService;
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
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 拼团产品表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreCombinationServiceImpl extends BaseServiceImpl<YxStoreCombinationMapper, YxStoreCombination> implements YxStoreCombinationService {

    @Autowired
    private YxStoreCombinationMapper yxStoreCombinationMapper;

    @Autowired
    private YxStoreProductReplyService replyService;

    @Autowired
    private YxStorePinkService storePinkService;

    /**
     * 减库存增加销量
     * @param num
     * @param combinationId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decStockIncSales(int num, int combinationId) {
        yxStoreCombinationMapper.decStockIncSales(num,combinationId);
    }

    /**
     * 增加库存 减少销量
     * @param num
     * @param combinationId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incStockDecSales(int num, int combinationId) {
        yxStoreCombinationMapper.incStockDecSales(num,combinationId);
    }

    @Override
    public YxStoreCombination getCombination(int id) {
        QueryWrapper<YxStoreCombination> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id).eq("is_del",0).eq("is_show",1);
        return yxStoreCombinationMapper.selectOne(wrapper);
    }

    /**
     * 判断库存是否足够
     * @param combinationId
     * @param cartNum
     * @return
     */
    @Override
    public boolean judgeCombinationStock(int combinationId, int cartNum) {
        YxStoreCombinationQueryVo queryVo = getYxStoreCombinationById(combinationId);
        if(queryVo.getStock() >= cartNum){
            return true;
        }
        return false;
    }

    @Override
    public YxStoreCombinationQueryVo getCombinationT(int id) {
        return yxStoreCombinationMapper.getCombDetail(id);
    }

    @Override
    public StoreCombinationDTO getDetail(int id,int uid) {
        YxStoreCombinationQueryVo storeCombinationQueryVo = yxStoreCombinationMapper
                .getCombDetail(id);
        if(ObjectUtil.isNull(storeCombinationQueryVo)){
            throw new ErrorRequestException("拼团不存在或已下架");
        }


        StoreCombinationDTO storeCombinationDTO = new StoreCombinationDTO();

        storeCombinationDTO.setStoreInfo(storeCombinationQueryVo);

        storeCombinationDTO.setReply(replyService
                .getReply(storeCombinationQueryVo.getProductId()));
        int replyCount = replyService.productReplyCount(storeCombinationQueryVo.getProductId());
        storeCombinationDTO.setReplyCount(replyCount);
        storeCombinationDTO.setReplyChance(replyService.doReply(storeCombinationQueryVo.getProductId()
                ,replyCount));
        Map<String,Object> map = storePinkService.getPinkAll(id,true);
        storeCombinationDTO.setPindAll((List<Integer>)map.get("pindAll"));
        storeCombinationDTO.setPink((List<PinkDTO> )map.get("list"));
        storeCombinationDTO.setPinkOkList(storePinkService.getPinkOkList(uid));
        storeCombinationDTO.setPinkOkSum(storePinkService.getPinkOkSumTotalNum());

        return storeCombinationDTO;
    }

    /**
     * 拼团列表
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<YxStoreCombinationQueryVo> getList(int page, int limit) {
        Page<YxStoreCombination> pageModel = new Page<>(page, limit);
        return yxStoreCombinationMapper.getCombList(pageModel);
    }

    @Override
    public YxStoreCombinationQueryVo getYxStoreCombinationById(Serializable id){
        return yxStoreCombinationMapper.getYxStoreCombinationById(id);
    }

    @Override
    public Paging<YxStoreCombinationQueryVo> getYxStoreCombinationPageList(YxStoreCombinationQueryParam yxStoreCombinationQueryParam) throws Exception{
        Page page = setPageParam(yxStoreCombinationQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreCombinationQueryVo> iPage = yxStoreCombinationMapper.getYxStoreCombinationPageList(page,yxStoreCombinationQueryParam);
        return new Paging(iPage);
    }

}
