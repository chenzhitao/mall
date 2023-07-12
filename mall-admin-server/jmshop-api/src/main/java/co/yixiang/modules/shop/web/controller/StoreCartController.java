/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.web.controller;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.shop.service.YxStoreCartService;
import co.yixiang.modules.shop.web.param.CartIdsParm;
import co.yixiang.modules.shop.web.param.YxStoreCartQueryParam;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 购物车控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-10-25
 */
@Slf4j
@RestController
@Api(value = "购物车", tags = "商城:购物车", description = "购物车")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoreCartController extends BaseController {

    private final YxStoreCartService storeCartService;

    /**
     * 购物车 获取数量
     */
    @GetMapping("/cart/count")
    @ApiOperation(value = "获取数量",notes = "获取数量")
    public ApiResult<Map<String,Object>> count(YxStoreCartQueryParam queryParam){
        Map<String,Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
        map.put("count",storeCartService.getUserCartNum(uid,"product",queryParam.getNumType().intValue()));
        return ApiResult.ok(map);
    }

    /**
     * 购物车 添加
     */
    @Log(value = "添加购物车",type = 1)
    @PostMapping("/cart/add")
    @ApiOperation(value = "添加购物车",notes = "添加购物车")
    public ApiResult<Map<String,Object>> add(@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Map<String,Object> map = new LinkedHashMap<>();
        int uid = SecurityUtils.getUserId().intValue();
        if(ObjectUtil.isNull(jsonObject.get("cartNum")) || ObjectUtil.isNull(jsonObject.get("productId"))){
            return ApiResult.fail("参数有误");
        }
        Integer cartNum = jsonObject.getInteger("cartNum");
        if(ObjectUtil.isNull(cartNum)){
            return ApiResult.fail("购物车数量参数有误");
        }
        if(cartNum <= 0){
            return ApiResult.fail("购物车数量必须大于0");
        }
        int isNew = 1;
        if(ObjectUtil.isNotNull(jsonObject.get("new"))){
            isNew = jsonObject.getInteger("new");
        }
        Integer productId = jsonObject.getInteger("productId");
        if(ObjectUtil.isNull(productId)){
            return ApiResult.fail("产品参数有误");
        }
        String uniqueId = jsonObject.get("uniqueId").toString();

        //拼团
        int combinationId = 0;
        if(ObjectUtil.isNotNull(jsonObject.get("combinationId"))){
            combinationId = jsonObject.getInteger("combinationId");
        }

        //秒杀
        int seckillId = 0;
        if(ObjectUtil.isNotNull(jsonObject.get("secKillId"))){
            seckillId = jsonObject.getInteger("secKillId");
        }

        //秒杀
        int bargainId = 0;
        if(ObjectUtil.isNotNull(jsonObject.get("bargainId"))){
            bargainId = jsonObject.getInteger("bargainId");
        }

        // 商品包装规格
        String packaging = "个";
        if(ObjectUtil.isNotNull(jsonObject.get("packaging"))){
            packaging = jsonObject.get("packaging").toString();
        }

        // 商品包装规格数量
        int packNum = 0;
        if(ObjectUtil.isNotNull(jsonObject.get("packNum"))){
            packNum = jsonObject.getInteger("packNum");
        }

        // 修改商品价格
        BigDecimal alterPrice = new BigDecimal(0.00);
        if(ObjectUtil.isNotNull(jsonObject.get("alterPrice"))){
            alterPrice = jsonObject.getBigDecimal("alterPrice");
        }



        map.put("cartId",storeCartService.addCart(uid,productId,cartNum,uniqueId
                ,"product",isNew,combinationId,seckillId,bargainId, packaging, packNum, alterPrice));
        return ApiResult.ok(map);
    }


    /**
     * 购物车列表
     */
    @Log(value = "查看购物车",type = 1)
    @GetMapping("/cart/list")
    @ApiOperation(value = "购物车列表",notes = "购物车列表")
    public ApiResult<Map<String,Object>> getList(){
        int uid = SecurityUtils.getUserId().intValue();
        return ApiResult.ok(storeCartService.getUserProductCartList(uid,"",0));
    }

    /**
     * 修改产品数量
     */
    @PostMapping("/cart/num")
    @ApiOperation(value = "修改产品数量",notes = "修改产品数量")
    public ApiResult<Object> cartNum(@RequestBody String jsonStr){
        int uid = SecurityUtils.getUserId().intValue();
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if(ObjectUtil.isNull(jsonObject.get("id")) || ObjectUtil.isNull(jsonObject.get("number")) || ObjectUtil.isNull(jsonObject.get("packNumber"))){
            ApiResult.fail("参数错误");
        }
        storeCartService.changeUserCartNum(jsonObject.getInteger("id"),
                jsonObject.getInteger("number"),jsonObject.getInteger("packNumber"),uid);
        return ApiResult.ok("ok");
    }

    /**
     * 购物车删除产品
     */
    @PostMapping("/cart/del")
    @ApiOperation(value = "购物车删除产品",notes = "购物车删除产品")
    public ApiResult<Object> cartDel(@Validated @RequestBody CartIdsParm parm){
        int uid = SecurityUtils.getUserId().intValue();
        storeCartService.removeUserCart(uid, parm.getIds());

        return ApiResult.ok("ok");
    }





}

