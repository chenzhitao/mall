/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.activity.entity.YxStoreBargain;
import co.yixiang.modules.activity.entity.YxStoreCombination;
import co.yixiang.modules.activity.entity.YxStoreSeckill;
import co.yixiang.modules.activity.mapper.YxStoreBargainMapper;
import co.yixiang.modules.activity.mapper.YxStoreCombinationMapper;
import co.yixiang.modules.activity.mapper.YxStoreSeckillMapper;
import co.yixiang.modules.activity.service.YxStoreBargainService;
import co.yixiang.modules.activity.service.YxStoreCombinationService;
import co.yixiang.modules.activity.service.YxStoreSeckillService;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.shop.entity.YxStoreCart;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.modules.shop.mapper.YxStoreCartMapper;
import co.yixiang.modules.shop.mapper.YxStoreProductAttrValueMapper;
import co.yixiang.modules.shop.mapping.CartMap;
import co.yixiang.modules.shop.service.YxStoreCartService;
import co.yixiang.modules.shop.service.YxStoreProductAttrService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;


/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-25
 */
@Slf4j
@Service
@Builder
@Transactional(rollbackFor = Exception.class)
public class YxStoreCartServiceImpl extends BaseServiceImpl<YxStoreCartMapper, YxStoreCart> implements YxStoreCartService {

    @Autowired
    private YxStoreCartMapper yxStoreCartMapper;
    @Autowired
    private YxStoreSeckillMapper storeSeckillMapper;
    @Autowired
    private YxStoreBargainMapper yxStoreBargainMapper;
    @Autowired
    private YxStoreCombinationMapper storeCombinationMapper;
    @Autowired
    private YxStoreProductService productService;
    @Autowired
    private YxStoreProductAttrService productAttrService;
    @Autowired
    private YxStoreCombinationService storeCombinationService;
    @Autowired
    private YxStoreSeckillService storeSeckillService;
    @Autowired
    private YxStoreBargainService storeBargainService;
    @Autowired
    private YxStoreOrderService storeOrderService;
    @Autowired
    private YxUserService userService;
    @Autowired
    private CartMap cartMap;

    @Autowired
    private YxStoreProductAttrValueMapper storeProductAttrValueMapper;

    /**
     * 删除购物车
     *
     * @param uid
     * @param ids
     */
    @Override
    public void removeUserCart(int uid, List<String> ids) {
        QueryWrapper<YxStoreCart> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).in("id", ids);

        YxStoreCart storeCart = new YxStoreCart();
        storeCart.setIsDel(1);

        yxStoreCartMapper.update(storeCart, wrapper);
    }

    /**
     * 改购物车数量
     *
     * @param cartId
     * @param cartNum
     * @param uid
     */
    @Override
    public void changeUserCartNum(int cartId, int cartNum, int packNum, int uid) {
        QueryWrapper<YxStoreCart> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("id", cartId);

        YxStoreCart cart = getOne(wrapper);
        if (ObjectUtil.isNull(cart)) {
            throw new ErrorRequestException("购物车不存在");
        }

        if (cartNum <= 0) {
            throw new ErrorRequestException("库存错误");
        }

        //todo 普通商品库存
        int stock = productService.getProductStock(cart.getProductId(), cart.getProductAttrUnique());
        if (stock < cartNum) {
            throw new ErrorRequestException("该产品库存不足" + cartNum);
        }

        if (cartNum == cart.getCartNum()) {
            return;
        }

        YxStoreCart storeCart = new YxStoreCart();
        storeCart.setCartNum(cartNum);
        storeCart.setPackNum(packNum);
        storeCart.setId(Long.valueOf(cartId));

        yxStoreCartMapper.updateById(storeCart);


    }

    /**
     * 购物车列表
     *
     * @param uid     用户id
     * @param cartIds 购物车id，多个逗号隔开
     * @param status  0-购购物车列表
     * @return
     */
    @Override
    public Map<String, Object> getUserProductCartList(int uid, String cartIds, int status) {
        QueryWrapper<YxStoreCart> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("type", "product").eq("is_pay", 0).eq("is_del", 0).orderByDesc("add_time");
        if (status == 0) {
            wrapper.eq("is_new", 0);
        }
        if (StrUtil.isNotEmpty(cartIds)) {
            wrapper.in("id", Arrays.asList(cartIds.split(",")));
        }
        List<YxStoreCart> carts = yxStoreCartMapper.selectList(wrapper);

        List<YxStoreCartQueryVo> valid = new ArrayList<>();
        List<YxStoreCartQueryVo> invalid = new ArrayList<>();

        for (YxStoreCart storeCart : carts) {
            YxStoreProductQueryVo storeProduct = null;
            if (storeCart.getCombinationId() > 0) {
                storeProduct = ObjectUtil.clone(storeCombinationMapper.combinatiionInfo(storeCart.getCombinationId()));
            } else if (storeCart.getSeckillId() > 0) {
                storeProduct = ObjectUtil.clone(storeSeckillMapper.seckillInfo(storeCart.getSeckillId()));
            } else if (storeCart.getBargainId() > 0) {
                storeProduct = ObjectUtil.clone(yxStoreBargainMapper.bargainInfo(storeCart.getBargainId()));
            } else {
                //必须得重新克隆创建一个新对象
                storeProduct = ObjectUtil.clone(productService.getNewStoreProductById(storeCart.getProductId()));
            }

            YxStoreCartQueryVo storeCartQueryVo = cartMap.toDto(storeCart);

            if (ObjectUtil.isNull(storeProduct)) {
                YxStoreCart yxStoreCart = new YxStoreCart();
                yxStoreCart.setIsDel(1);
                yxStoreCartMapper.update(yxStoreCart, new QueryWrapper<YxStoreCart>().lambda().eq(YxStoreCart::getId, storeCart.getId()));
            } else if (storeProduct.getIsShow() == 0 || storeProduct.getIsDel() == 1 || storeProduct.getStock() == 0) {
                storeCartQueryVo.setProductInfo(storeProduct);
                if(storeProduct!=null){
                    BigDecimal maxOtPrice = storeProductAttrValueMapper.maxOtPrice(storeCartQueryVo.getProductId());
                    if (maxOtPrice != null) {
                        storeCartQueryVo.setOtPrice(maxOtPrice);
                    }
                }
                invalid.add(storeCartQueryVo);
            } else {
                if (StrUtil.isNotEmpty(storeCart.getProductAttrUnique())) {
                    YxStoreProductAttrValue productAttrValue = productAttrService.uniqueByAttrInfo(storeCart.getProductAttrUnique());
                    if (ObjectUtil.isNull(productAttrValue) || productAttrValue.getStock() == 0) {
                        storeCartQueryVo.setProductInfo(storeProduct);
                        invalid.add(storeCartQueryVo);
                    } else {
                        storeProduct.setAttrInfo(productAttrValue);
                        storeCartQueryVo.setProductInfo(storeProduct);

                        //设置真实价格
                        //设置VIP价格
                        double vipPrice = 0d;
                        if (storeCart.getCombinationId() > 0 || storeCart.getSeckillId() > 0 || storeCart.getBargainId() > 0) {
                            vipPrice = storeCart.getAlterPrice().compareTo(BigDecimal.ZERO) == 0 ? productAttrValue.getPrice().doubleValue() : storeCart.getAlterPrice().doubleValue();
                        } else {
                            vipPrice = userService.setLevelPrice(storeCart.getAlterPrice().compareTo(BigDecimal.ZERO) == 0 ? productAttrValue.getPrice().doubleValue() : storeCart.getAlterPrice().doubleValue(), uid, productAttrValue.getProductId());
                        }
                        storeCartQueryVo.setTruePrice(vipPrice);
                        //设置会员价
                        storeCartQueryVo.setVipTruePrice(storeCart.getAlterPrice().compareTo(BigDecimal.ZERO) == 0 ? productAttrValue.getPrice().doubleValue() : storeCart.getAlterPrice().doubleValue());
                        storeCartQueryVo.setCostPrice(productAttrValue.getCost().doubleValue());
                        storeCartQueryVo.setTrueStock(productAttrValue.getStock());
                        storeCartQueryVo.setAlterPrice(storeCart.getAlterPrice());
                        if(storeProduct!=null){
                            BigDecimal maxOtPrice = storeProductAttrValueMapper.maxOtPrice(storeCartQueryVo.getProductId());
                            if (maxOtPrice != null) {
                                storeCartQueryVo.setOtPrice(maxOtPrice);
                            }
                        }
                        valid.add(storeCartQueryVo);

                    }
                } else {
                    //设置VIP价格
                    double vipPrice = 0d;
                    if (storeCart.getCombinationId() > 0 || storeCart.getSeckillId() > 0 || storeCart.getBargainId() > 0) {
                        vipPrice = storeProduct.getPrice().doubleValue();
                    } else {
                        vipPrice = userService.setLevelPrice(storeProduct.getPrice().doubleValue(), uid, storeProduct.getId());
                    }
                    storeCartQueryVo.setTruePrice(vipPrice);
                    //todo 设置会员价
                    storeCartQueryVo.setVipTruePrice(0d);
                    storeCartQueryVo.setCostPrice(storeProduct.getCost().doubleValue());
                    storeCartQueryVo.setTrueStock(storeProduct.getStock());
                    storeCartQueryVo.setProductInfo(storeProduct);
                    storeCartQueryVo.setAlterPrice(storeCart.getAlterPrice());
                    if(storeProduct!=null){
                        BigDecimal maxOtPrice = storeProductAttrValueMapper.maxOtPrice(storeCartQueryVo.getProductId());
                        if (maxOtPrice != null) {
                            storeCartQueryVo.setOtPrice(maxOtPrice);
                        }
                    }
                    valid.add(storeCartQueryVo);
                }
            }


        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("valid", valid);
        map.put("invalid", invalid);
        return map;
    }

    /**
     * 添加购物车
     *
     * @param uid               用户id
     * @param productId         普通产品编号
     * @param cartNum           购物车数量
     * @param productAttrUnique 属性唯一值
     * @param type              product
     * @param isNew             1 加入购物车直接购买  0 加入购物车
     * @param combinationId     拼团id
     * @param seckillId         秒杀id
     * @param bargainId         砍价id
     * @param alterPrice
     * @return
     */
    @Override
    public int addCart(int uid, int productId, int cartNum, String productAttrUnique, String type, int isNew, int combinationId, int seckillId, int bargainId, String packaging, int packNum, BigDecimal alterPrice) {

        checkProductStock(uid, productId, cartNum, productAttrUnique, combinationId, seckillId, bargainId);

        QueryWrapper<YxStoreCart> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("type", type).eq("is_pay", 0).eq("is_del", 0).eq("product_id", productId).eq("is_new", isNew).eq("product_attr_unique", productAttrUnique).eq("combination_id", combinationId).eq("bargain_id", bargainId).eq("seckill_id", seckillId).eq("packaging", packaging).orderByDesc("id").last("limit 1");

        YxStoreCart cart = yxStoreCartMapper.selectOne(wrapper);
        YxStoreCart storeCart = new YxStoreCart();

        storeCart.setBargainId(bargainId);
        storeCart.setCartNum(cartNum);
        storeCart.setCombinationId(combinationId);
        storeCart.setProductAttrUnique(productAttrUnique);
        storeCart.setProductId(productId);
        storeCart.setSeckillId(seckillId);
        storeCart.setType(type);
        storeCart.setUid(uid);
        storeCart.setIsNew(isNew);
        storeCart.setPackaging(packaging);
        storeCart.setPackNum(packNum);
        storeCart.setAlterPrice(alterPrice);
        if (ObjectUtil.isNotNull(cart)) {
            if (isNew == 0) {
                storeCart.setCartNum(cartNum + cart.getCartNum());
                storeCart.setPackNum(packNum + cart.getPackNum());
            }
            storeCart.setId(cart.getId());
            yxStoreCartMapper.updateById(storeCart);
        } else {
            //判断是否已经添加过
            storeCart.setAddTime(OrderUtil.getSecondTimestampTwo());
            yxStoreCartMapper.insert(storeCart);
        }

        return storeCart.getId().intValue();
    }

    @Override
    public int getUserCartNum(int uid, String type, int numType) {
        int num = 0;
        QueryWrapper<YxStoreCart> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("type", type).eq("is_pay", 0).eq("is_del", 0).eq("is_new", 0);
        if (numType > 0) {
            num = yxStoreCartMapper.selectCount(wrapper);
        } else {
            num = yxStoreCartMapper.cartSum(uid, type);
        }
        return num;
    }

    @Override
    public YxStoreCartQueryVo getYxStoreCartById(Serializable id) {
        return yxStoreCartMapper.getYxStoreCartById(id);
    }

    /**
     * 验证产品库存是否足够
     *
     * @param uid               用户ID
     * @param productId         产品ID
     * @param cartNum           购买数量
     * @param productAttrUnique 商品属性Unique
     * @param combinationId     拼团产品ID
     * @param seckillId         秒杀产品ID
     * @param bargainId         砍价产品ID
     */
    @Override
    public void checkProductStock(int uid, int productId, int cartNum, String productAttrUnique, int combinationId, int seckillId, int bargainId) {
        //todo 拼团
        if (combinationId > 0) {
            boolean isStock = storeCombinationService.judgeCombinationStock(combinationId, cartNum);
            if (!isStock) {
                throw new ErrorRequestException("该产品库存不足");
            }

            YxStoreCombination storeCombination = storeCombinationService.getCombination(combinationId);
            if (ObjectUtil.isNull(storeCombination)) {
                throw new ErrorRequestException("该产品已下架或删除");
            }
        } else if (seckillId > 0) {//秒杀
            YxStoreSeckill yxStoreSeckill = storeSeckillService.getSeckill(seckillId);
            if (ObjectUtil.isNull(yxStoreSeckill)) {
                throw new ErrorRequestException("该产品已下架或删除");
            }
            if (yxStoreSeckill.getStock() < cartNum) {
                throw new ErrorRequestException("该产品库存不足");
            }
            int seckillOrderCount = storeOrderService.count(new QueryWrapper<YxStoreOrder>().eq("uid", uid).eq("paid", 1).eq("seckill_id", seckillId));
            if (yxStoreSeckill.getNum() <= seckillOrderCount || yxStoreSeckill.getNum() < cartNum) {
                throw new ErrorRequestException("每人限购:" + yxStoreSeckill.getNum() + "件");
            }

        } else if (bargainId > 0) {//砍价
            YxStoreBargain yxStoreBargain = storeBargainService.getBargain(bargainId);
            if (ObjectUtil.isNull(yxStoreBargain)) {
                throw new ErrorRequestException("该产品已下架或删除");
            }
            if (yxStoreBargain.getStock() < cartNum) {
                throw new ErrorRequestException("该产品库存不足");
            }

        } else {
            YxStoreProductQueryVo productQueryVo = productService.getYxStoreProductById(productId);
            if (ObjectUtil.isNull(productQueryVo)) {
                throw new ErrorRequestException("该产品已下架或删除");
            }

            int stock = productService.getProductStock(productId, productAttrUnique);
            if (stock < cartNum) {
                throw new ErrorRequestException(productQueryVo.getStoreName() + "库存不足" + cartNum);
            }
        }
    }


}
