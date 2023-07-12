/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.web.controller;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.web.param.YxSystemStoreQueryParam;
import co.yixiang.modules.shop.web.vo.YxProductTemplateVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import co.yixiang.modules.shop.web.vo.YxSystemStoreQueryVo;
import co.yixiang.modules.shop.wrapper.StoreProductWrapper;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName IndexController
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/19
 **/

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "首页模块", tags = "商城:首页模块", description = "首页模块")
public class IndexController
{

    private final YxSystemGroupDataService systemGroupDataService;
    private final YxStoreProductService storeProductService;
    private final YxSystemStoreService systemStoreService;
    private final YxStoreTemplateService storeTemplateService;

    private final YxStoreCategoryService yxStoreCategoryService;


    @AnonymousAccess
    //    @Cacheable(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY)
    @GetMapping("/indexV1")
    @ApiOperation(value = "首页数据", notes = "首页数据")
    public ApiResult<Map<String, Object>> index()
    {

        Map<String, Object> map = new LinkedHashMap<>();
        //banner
        map.put("banner", systemGroupDataService.getDatas(ShopConstants.JMSHOP_HOME_BANNER));
        //首页按钮
        map.put("menus", systemGroupDataService.getDatas(ShopConstants.JMSHOP_HOME_MENUS));
        //        //首页活动区域图片
        //        map.put("activity",new String[]{});
        //精品推荐
        map.put("bastList", StoreProductWrapper.listVO(storeProductService.getList(1, 300, 1)));
        //首发新品
        map.put("firstList", StoreProductWrapper.listVO(storeProductService.getList(1, 8, 3)));
        //促销单品
        map.put("benefit", StoreProductWrapper.listVO(storeProductService.getList(1, 8, 4)));
        //热门榜单
        map.put("likeInfo", StoreProductWrapper.listVO(storeProductService.getList(1, 8, 2)));
        //滚动
        map.put("roll", systemGroupDataService.getDatas(ShopConstants.JMSHOP_HOME_ROLL_NEWS));
        map.put("mapKey", RedisUtil.get(RedisKeyEnum.TENGXUN_MAP_KEY.getValue()));
        return ApiResult.ok(map);
    }

    @AnonymousAccess
    //    @Cacheable(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY)
    @GetMapping("/index")
    @ApiOperation(value = "首页数据", notes = "首页数据")
    public ApiResult<Map<String, Object>> indexV2()
    {

        Map<String, Object> map = new LinkedHashMap<>();
        //banner
        map.put("banner", systemGroupDataService.getDatas(ShopConstants.JMSHOP_HOME_BANNER));
        //首页按钮
        List<Map<String,Object>> list= systemGroupDataService.getDatas(ShopConstants.JMSHOP_HOME_MENUS);
        List<Map<String,Object>> advertisList= systemGroupDataService.getDatas(ShopConstants.JMSHOP_ADVERTISING_MENUS);

        List<Map<String,Object>> topMenus= list.stream().filter(obj->"1".equals(String.valueOf(obj.get("category")))).collect(Collectors.toList());
        List<Map<String,Object>> meddleMenus= list.stream().filter(obj->"2".equals(String.valueOf(obj.get("category")))).collect(Collectors.toList());
        map.put("menus",topMenus );
        map.put("meddleMenus",meddleMenus );

        Map<String,Object> adv1=advertisList.stream().filter(obj->"1".equals(String.valueOf(obj.get("category")))).findFirst().orElse(null);
        Map<String,Object> adv2=advertisList.stream().filter(obj->"2".equals(String.valueOf(obj.get("category")))).findFirst().orElse(null);
        List<Map<String,Object>> adv3=advertisList.stream().filter(obj->"3".equals(String.valueOf(obj.get("category")))).collect(Collectors.toList());
        List<Map<String,Object>> adv4=advertisList.stream().filter(obj->"4".equals(String.valueOf(obj.get("category")))).collect(Collectors.toList());
        Map<String,Object> adv5=advertisList.stream().filter(obj->"5".equals(String.valueOf(obj.get("category")))).findFirst().orElse(null);

        map.put("advertising1",adv1 );
        map.put("advertising2",adv2 );
        map.put("advertising3Array",adv3 );
        map.put("advertising4Array",adv4 );
        map.put("advertising5",adv5 );

        List<YxProductTemplateVo> templateList=storeTemplateService.selectProductTemplateList();
        map.put("templateList",templateList );
        //        //首页活动区域图片
        //        map.put("activity",new String[]{});
        //精品推荐
        List productList= StoreProductWrapper.listVO(storeProductService.getList(1, 300, 1));
        adv4.stream().forEach(adv->{
            adv.put("showType",2);
            boolean checkFlag= NumberUtil.isNumber(String.valueOf(adv.get("sort")));
            int sort=0;
            if(checkFlag){
                sort=Integer.parseInt(String.valueOf(adv.get("sort")));
                if(sort<=1){
                    sort=0;
                }
                if(sort>=productList.size()){
                    sort=productList.size()>=1?(productList.size()):0;
                }
            }
            productList.add(sort,adv);
        });

        map.put("bastList", productList);
        //首发新品
        map.put("firstList", StoreProductWrapper.listVO(storeProductService.getList(1, 8, 3)));
        //促销单品
        map.put("benefit", StoreProductWrapper.listVO(storeProductService.getList(1, 8, 4)));
        //热门榜单
        map.put("likeInfo", StoreProductWrapper.listVO(storeProductService.getList(1, 8, 2)));
        //滚动
        map.put("roll", systemGroupDataService.getDatas(ShopConstants.JMSHOP_HOME_ROLL_NEWS));
        map.put("mapKey", RedisUtil.get(RedisKeyEnum.TENGXUN_MAP_KEY.getValue()));
        return ApiResult.ok(map);
    }

    @AnonymousAccess
    @GetMapping("/search/keyword")
    @ApiOperation(value = "热门搜索关键字获取", notes = "热门搜索关键字获取")
    public ApiResult<List<String>> search()
    {
        List<Map<String, Object>> list = systemGroupDataService.getDatas(ShopConstants.JMSHOP_HOT_SEARCH);
        List<String> stringList = new ArrayList<>();
        for (Map<String, Object> map : list)
        {
            stringList.add(map.get("title").toString());
        }
        return ApiResult.ok(stringList);
    }

    @AnonymousAccess
    @PostMapping("/image_base64")
    @ApiOperation(value = "获取图片base64", notes = "获取图片base64")
    @Deprecated
    public ApiResult<List<String>> imageBase64()
    {
        return ApiResult.ok(null);
    }

    @AnonymousAccess
    @GetMapping("/citys")
    @ApiOperation(value = "获取城市json", notes = "获取城市json")
    public ApiResult<String> cityJson()
    {
        String path = "city.json";
        String name = "city.json";
        try
        {
            File file = FileUtil.inputStreamToFile(new ClassPathResource(path).getStream(), name);
            FileReader fileReader = new FileReader(file, "UTF-8");
            String string = fileReader.readString();
            System.out.println(string);
            JSONObject jsonObject = JSON.parseObject(string);
            return ApiResult.ok(jsonObject);
        } catch (Exception e)
        {
            e.printStackTrace();

            return ApiResult.fail("无数据");
        }

    }


    @AnonymousAccess
    @GetMapping("/store_list")
    @ApiOperation(value = "获取门店列表", notes = "获取门店列表")
    public ApiResult<Object> storeList(YxSystemStoreQueryParam param)
    {
        Map<String, Object> map = new LinkedHashMap<>();
        List<YxSystemStoreQueryVo> lists;
        if (StrUtil.isBlank(param.getLatitude()) || StrUtil.isBlank(param.getLongitude()))
        {
            lists = systemStoreService.getYxSystemStorePageList(param).getRecords();
        } else
        {
            lists = systemStoreService.getStoreList(param.getLatitude(), param.getLongitude(), param.getPage(), param.getLimit());
        }

        map.put("list", lists);
        // map.put("mapKey",RedisUtil.get("tengxun_map_key"));
        return ApiResult.ok(map);

    }


}
