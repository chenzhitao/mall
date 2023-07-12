/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreProductReply;
import co.yixiang.modules.shop.mapper.YxStoreProductReplyMapper;
import co.yixiang.modules.shop.service.YxStoreProductReplyService;
import co.yixiang.modules.shop.web.dto.ReplyCountDTO;
import co.yixiang.modules.shop.web.param.YxStoreProductReplyQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductReplyQueryVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-23
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreProductReplyServiceImpl extends BaseServiceImpl<YxStoreProductReplyMapper, YxStoreProductReply> implements YxStoreProductReplyService {

    private final YxStoreProductReplyMapper yxStoreProductReplyMapper;

    /**
     * 评价数据
     * @param productId
     * @return
     */
    @Override
    public ReplyCountDTO getReplyCount(int productId) {
        ReplyCountDTO replyCountDTO = new ReplyCountDTO();

        QueryWrapper<YxStoreProductReply> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",productId).eq("is_del",0).eq("reply_type","product");
        int sumCount = yxStoreProductReplyMapper.selectCount(wrapper);
        replyCountDTO.setSumCount(sumCount);

        //好评
        QueryWrapper<YxStoreProductReply> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("product_id",productId).eq("is_del",0)
                .eq("reply_type","product").eq("product_score",5);
        int goodCount = yxStoreProductReplyMapper.selectCount(wrapperOne);
        replyCountDTO.setGoodCount(goodCount);

        //中评
        QueryWrapper<YxStoreProductReply> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.eq("product_id",productId).eq("is_del",0)
                .eq("reply_type","product")
                .lt("product_score",5).gt("product_score",2);
        replyCountDTO.setInCount(yxStoreProductReplyMapper.selectCount(wrapperTwo));

        //差评
        QueryWrapper<YxStoreProductReply> wrapperThree = new QueryWrapper<>();
        wrapperThree.eq("product_id",productId).eq("is_del",0)
                .eq("reply_type","product")
                .lt("product_score",2);
        replyCountDTO.setPoorCount(yxStoreProductReplyMapper.selectCount(wrapperThree));

        //好评率

        replyCountDTO.setReplySstar(""+NumberUtil.round(NumberUtil.mul(NumberUtil.div(goodCount,sumCount),5),2));
        replyCountDTO.setReplyChance(""+NumberUtil.round(NumberUtil.mul(NumberUtil.div(goodCount,sumCount),100),2));

        return replyCountDTO;
    }

    /**
     * 处理评价
     * @param replyQueryVo
     * @return
     */
    @Override
    public YxStoreProductReplyQueryVo handleReply(YxStoreProductReplyQueryVo replyQueryVo) {
        YxStoreCartQueryVo cartInfo = JSONObject.parseObject(replyQueryVo.getCartInfo()
                ,YxStoreCartQueryVo.class);
        if(ObjectUtil.isNotNull(cartInfo)){
            if(ObjectUtil.isNotNull(cartInfo.getProductInfo())){
                if(ObjectUtil.isNotNull(cartInfo.getProductInfo().getAttrInfo())){
                    replyQueryVo.setSuk(cartInfo.getProductInfo().getAttrInfo().getSuk());
                }
            }
        }

        BigDecimal star = NumberUtil.add(replyQueryVo.getProductScore(),
                replyQueryVo.getServiceScore());

        star = NumberUtil.div(star,2);

        replyQueryVo.setStar(String.valueOf(star.intValue()));

        if(StrUtil.isEmpty(replyQueryVo.getComment())){
            replyQueryVo.setComment("此用户没有填写评价");
        }

        return replyQueryVo;
    }

    /**
     * 获取单条评价
     * @param productId
     * @return
     */
    @Override
    public YxStoreProductReplyQueryVo getReply(int productId) {
        YxStoreProductReplyQueryVo vo = yxStoreProductReplyMapper.getReply(productId);
        if(ObjectUtil.isNotNull(vo)){
            return handleReply(yxStoreProductReplyMapper.getReply(productId));
        }
        return null;
    }


    /**
     * 获取评价列表
     * @param productId
     * @param type
     * @param page
     * @param limit
     * @return
     */
    @Override
    public List<YxStoreProductReplyQueryVo> getReplyList(int productId,int type,int page, int limit) {
        List<YxStoreProductReplyQueryVo> newList = new ArrayList<>();
        Page<YxStoreProductReply> pageModel = new Page<>(page, limit);
        List<YxStoreProductReplyQueryVo> list = yxStoreProductReplyMapper
                .selectReplyList(pageModel,productId,type);
        List<YxStoreProductReplyQueryVo> list1 = list.stream().map(i ->{
            YxStoreProductReplyQueryVo vo = new YxStoreProductReplyQueryVo();
            BeanUtils.copyProperties(i,vo);
            if(i.getPictures().contains(",")){
                vo.setPics(i.getPictures().split(","));
            }
            return vo;
        }).collect(Collectors.toList());
        for (YxStoreProductReplyQueryVo queryVo : list1) {
            newList.add(handleReply(queryVo));
        }
        return newList;
    }

    @Override
    public int getInfoCount(Integer oid, String unique) {
        QueryWrapper<YxStoreProductReply> wrapper = new QueryWrapper<>();
        wrapper.eq("`unique`",unique).eq("oid",oid);
        return yxStoreProductReplyMapper.selectCount(wrapper);
    }

    @Override
    public int productReplyCount(int productId) {
        QueryWrapper<YxStoreProductReply> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",productId).eq("is_del",0).eq("reply_type","product");
        return yxStoreProductReplyMapper.selectCount(wrapper);
    }

    @Override
    public int replyCount(String unique) {
        QueryWrapper<YxStoreProductReply> wrapper = new QueryWrapper<>();
        wrapper.eq("`unique`",unique);
        return yxStoreProductReplyMapper.selectCount(wrapper);
    }

    /**
     * 处理比例
     * @param productId
     * @param count
     * @return
     */
    @Override
    public String doReply(int productId, int count) {
        QueryWrapper<YxStoreProductReply> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",productId).eq("is_del",0)
                .eq("reply_type","product").eq("product_score",5);
        int productScoreCount = yxStoreProductReplyMapper.selectCount(wrapper);
        if(count > 0){
            return ""+NumberUtil.round(NumberUtil.mul(NumberUtil.div(productScoreCount,count),100),2);
        }

        return "0";
    }

    @Override
    public YxStoreProductReplyQueryVo getYxStoreProductReplyById(Serializable id) throws Exception{
        return yxStoreProductReplyMapper.getYxStoreProductReplyById(id);
    }

    @Override
    public Paging<YxStoreProductReplyQueryVo> getYxStoreProductReplyPageList(YxStoreProductReplyQueryParam yxStoreProductReplyQueryParam) throws Exception{
        Page page = setPageParam(yxStoreProductReplyQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreProductReplyQueryVo> iPage = yxStoreProductReplyMapper.getYxStoreProductReplyPageList(page,yxStoreProductReplyQueryParam);
        return new Paging(iPage);
    }

}
