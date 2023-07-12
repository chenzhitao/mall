/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.web.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.aop.log.Log;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.enums.ProductEnum;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.modules.order.web.vo.WxQrCode;
import co.yixiang.modules.shop.entity.YxStoreProduct;
import co.yixiang.modules.shop.entity.YxVideo;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.web.dto.ProductDTO;
import co.yixiang.modules.shop.web.param.YxStoreProductQueryParam;
import co.yixiang.modules.shop.web.param.YxStoreProductRelationQueryParam;
import co.yixiang.modules.shop.web.param.YxVideoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreProductQueryVo;
import co.yixiang.modules.shop.web.vo.YxVideoQueryVo;
import co.yixiang.modules.user.entity.YxSystemAttachment;
import co.yixiang.modules.user.service.YxSystemAttachmentService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.vo.YxSystemLevelProduct;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-10-19
 */
@Slf4j
@RestController
@Api(value = "产品模块", tags = "商城:产品模块", description = "产品模块")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoreProductController extends BaseController {

    private final YxStoreProductService storeProductService;
    private final YxStoreProductRelationService productRelationService;
    private final YxStoreProductReplyService replyService;
    private final YxSystemConfigService systemConfigService;
    private final YxSystemAttachmentService systemAttachmentService;
    private final YxUserService yxUserService;
    private final CreatShareProductService creatShareProductService;
    private final YxVideoTypeService videoTypeService;
    private final YxVideoService videoService;


    @Value("${file.path}")
    private String path;


    /**
     * 获取首页更多产品
     */
    @AnonymousAccess
    @GetMapping("/groom/list/{type}")
    @ApiOperation(value = "获取首页更多产品", notes = "获取首页更多产品")
    public ApiResult<Map<String, Object>> moreGoodsList(@PathVariable Integer type) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (type.equals(ProductEnum.TYPE_1.getValue())) {// 精品推荐
            map.put("list", storeProductService.getList(1, 20, 1));
        } else if (type.equals(ProductEnum.TYPE_2.getValue())) {// 热门榜单
            map.put("list", storeProductService.getList(1, 20, 2));
        } else if (type.equals(ProductEnum.TYPE_3.getValue())) {// 首发新品
            map.put("list", storeProductService.getList(1, 20, 3));
        } else if (type.equals(ProductEnum.TYPE_4.getValue())) {// 促销单品
            map.put("list", storeProductService.getList(1, 20, 4));
        }

        return ApiResult.ok(map);
    }

    /**
     * 获取首页更多产品
     */
    @AnonymousAccess
    @GetMapping("/products")
    @ApiOperation(value = "商品列表", notes = "商品列表")
    public ApiResult<List<YxStoreProductQueryVo>> goodsList(YxStoreProductQueryParam productQueryParam) {
        return ApiResult.ok(storeProductService.getGoodsList(productQueryParam));
    }

    @GetMapping("/templateProducts/{id}")
    @ApiOperation(value = "商品列表", notes = "商品列表")
    public ApiResult<List<YxStoreProductQueryVo>> templateGoodsList(@PathVariable Integer id) {
        YxStoreProductQueryParam param=new YxStoreProductQueryParam();
        param.setCateId(String.valueOf(id));
        return ApiResult.ok(storeProductService.getTemplateGoodsList(param));
    }

    @AnonymousAccess
    @GetMapping("/categoryProducts")
    @ApiOperation(value = "商品列表", notes = "商品列表")
    public ApiResult<List<YxStoreProductQueryVo>> categoryGoodsList(YxStoreProductQueryParam yxStoreProductQueryParam) throws Exception {
        List<YxStoreProductQueryVo> vodeList = storeProductService.getCategoryGoodsList(yxStoreProductQueryParam);
        return ApiResult.ok(vodeList);
    }

    /**
     * 为你推荐
     */
    @AnonymousAccess
    @GetMapping("/product/hot")
    @ApiOperation(value = "为你推荐", notes = "为你推荐")
    public ApiResult<List<YxStoreProductQueryVo>> productRecommend(YxStoreProductQueryParam queryParam) {
        return ApiResult.ok(storeProductService.getList(queryParam.getPage().intValue(),
                queryParam.getLimit().intValue(), 1));
    }


    @Autowired
    private YxUserService userService;

    /**
     * 商品详情海报
     */
    @GetMapping("/product/poster/{id}")
    @ApiOperation(value = "商品详情海报", notes = "商品详情海报")
    public ApiResult<String> prodoctPoster(@PathVariable Integer id, HttpServletRequest request) throws IOException, FontFormatException {
        int uid = SecurityUtils.getUserId().intValue();
        //
        YxStoreProduct storeProduct = storeProductService.getProductInfo(id);
        //详情
        YxSystemLevelProduct yxSystemLevelProduct = userService.setYxSystemLevelProduct(storeProduct.getPrice().doubleValue());
        if (yxSystemLevelProduct != null) {
            storeProduct.setOtPrice(BigDecimal.valueOf(yxSystemLevelProduct.getDiscountPrice()));
        }
        if(StringUtils.isNotEmpty(storeProduct.getHaibaoImage())){
            return ApiResult.ok(storeProduct.getHaibaoImage());
        }
        // 海报
        String siteUrl = systemConfigService.getData("site_url");
        if (StrUtil.isEmpty(siteUrl)) {
            return ApiResult.fail("未配置h5地址");
        }
        String apiUrl = systemConfigService.getData("api_url");
        if (StrUtil.isEmpty(apiUrl)) {
            return ApiResult.fail("未配置api地址");
        }
        //apiUrl = "http://127.0.0.1:8009";

        YxUserQueryVo userInfo = yxUserService.getYxUserById(uid);
        String userType = userInfo.getUserType();
        if (!userType.equals(AppFromEnum.ROUNTINE.getValue())) {
            userType = AppFromEnum.H5.getValue();
        }
        String name = id + "_" + uid + "_" + userType + "_product_detail_wap.jpg";
        YxSystemAttachment attachment = systemAttachmentService.getInfo(name);
        String fileDir = path + "qrcode" + File.separator;
        String qrcodeUrl = "";

        if (ObjectUtil.isNull(attachment) || true) {
            //file 文件
            File file = FileUtil.mkdir(new File(fileDir));
            //获取token
            String accessToken = WxQrCode.getAccessToken(RedisUtil.get(RedisKeyEnum.WXAPP_APPID.getValue()), RedisUtil.get(RedisKeyEnum.WXAPP_SECRET.getValue()));
            //生成的二维码
            WxQrCode.getWXCode(accessToken, "pages/home/index", "?id=" + id, fileDir + name);

            //如果类型是小程序
//            if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
////                https://open.weixin.qq.com/sns/getexpappinfo?appid=wx2981c19a3334784a&path=pages%2Fshop%2FGoodsCon%2Findex%3Fid%3D65820
////                pages/shop/GoodsCon/index?id=65820
//                //h5地址
//                siteUrl = siteUrl + "/product/";
//                //生成二维码
//                QrCodeUtil.generate(siteUrl + "?id=" + id + "&spread=" + uid + "&pageType=good&codeType=" + AppFromEnum.ROUNTINE.getValue(), 180, 180,
//                        FileUtil.file(fileDir + name));
//            } else if (userType.equals(AppFromEnum.APP.getValue())) {
//                //h5地址
//                siteUrl = siteUrl + "/product/";
//                //生成二维码
//                QrCodeUtil.generate(siteUrl + "?id=" + id + "&spread=" + uid + "&pageType=good&codeType=" + AppFromEnum.APP.getValue(), 180, 180,
//                        FileUtil.file(fileDir + name));
//            } else {//如果类型是h5
//                //生成二维码
//                QrCodeUtil.generate(siteUrl + "/detail/" + id + "?spread=" + uid, 180, 180,
//                        FileUtil.file(fileDir + name));
//            }

            systemAttachmentService.attachmentAdd(name, String.valueOf(FileUtil.size(file)),
                    fileDir + name, "qrcode/" + name);

            qrcodeUrl = apiUrl + "/api/file/qrcode/" + name;
        } else {
            qrcodeUrl = apiUrl + "/api/file/" + attachment.getSattDir();
        }

        String spreadPicName = id + "_" + uid + "_" + userType + "_product_user_spread.jpg";
        String spreadPicPath = fileDir + spreadPicName;
        String rr = creatShareProductService.creatProductPic(storeProduct, qrcodeUrl, spreadPicName, spreadPicPath, apiUrl);

        //productDTO.getStoreInfo().setCodeBase(rr);
        return ApiResult.ok(rr);
    }

//
//
//            if (ObjectUtil.isNull(attachment)) {
//        File file = FileUtil.mkdir(new File(fileDir));
//        //如果类型是小程序
//        if (userType.equals(AppFromEnum.ROUNTINE.getValue())) {
////                https://open.weixin.qq.com/sns/getexpappinfo?appid=wx2981c19a3334784a&path=pages%2Fshop%2FGoodsCon%2Findex%3Fid%3D65820
////                pages/shop/GoodsCon/index?id=65820
//            //h5地址
//            siteUrl = siteUrl + "/product/";
//            //生成二维码
//            QrCodeUtil.generate(siteUrl + "?id=" + id + "&spread=" + uid + "&pageType=good&codeType=" + AppFromEnum.ROUNTINE.getValue(), 180, 180,
//                    FileUtil.file(fileDir + name));
//        } else if (userType.equals(AppFromEnum.APP.getValue())) {
//            //h5地址
//            siteUrl = siteUrl + "/product/";
//            //生成二维码
//            QrCodeUtil.generate(siteUrl + "?id=" + id + "&spread=" + uid + "&pageType=good&codeType=" + AppFromEnum.APP.getValue(), 180, 180,
//                    FileUtil.file(fileDir + name));
//        } else {//如果类型是h5
//            //生成二维码
//            QrCodeUtil.generate(siteUrl + "/detail/" + id + "?spread=" + uid, 180, 180,
//                    FileUtil.file(fileDir + name));
//        }
//        systemAttachmentService.attachmentAdd(name, String.valueOf(FileUtil.size(file)),
//                fileDir + name, "qrcode/" + name);
//
//        qrcodeUrl = apiUrl + "/api/file/qrcode/" + name;
//    } else {
//        qrcodeUrl = apiUrl + "/api/file/" + attachment.getSattDir();
//    }


    /**
     * 普通商品详情
     */
    @Log(value = "查看商品详情", type = 1)
    @GetMapping("/product/detail/{id}")
    @ApiOperation(value = "普通商品详情", notes = "普通商品详情")
    public ApiResult<ProductDTO> detail(@PathVariable Integer id,
                                        @RequestParam(value = "", required = false) String latitude,
                                        @RequestParam(value = "", required = false) String longitude,
                                        @RequestParam(value = "", required = false) String from) {
        int uid = SecurityUtils.getUserId().intValue();
        ProductDTO productDTO = storeProductService.goodsDetail(id, 0, uid, latitude, longitude);
        String freePostage = systemConfigService.getData("store_free_postage");
        productDTO.setPostageInfo(freePostage);
        // 海报
//        String siteUrl = systemConfigService.getData("site_url");
//        if(StrUtil.isEmpty(siteUrl)){
//            return ApiResult.fail("未配置h5地址");
//        }
//        String apiUrl = systemConfigService.getData("api_url");
//        if(StrUtil.isEmpty(apiUrl)){
//            return ApiResult.fail("未配置api地址");
//        }
//        YxUserQueryVo userInfo = yxUserService.getYxUserById(uid);
//        String userType = userInfo.getUserType();
//        if(!userType.equals(AppFromEnum.ROUNTINE.getValue())) {
//            userType = AppFromEnum.H5.getValue();
//        }
//            String name = id+"_"+uid + "_"+userType+"_product_detail_wap.jpg";
//            YxSystemAttachment attachment = systemAttachmentService.getInfo(name);
//            String fileDir = path+"qrcode"+ File.separator;
//            String qrcodeUrl = "";
//            if(ObjectUtil.isNull(attachment)){
//                File file = FileUtil.mkdir(new File(fileDir));
//                //如果类型是小程序
//                if(userType.equals(AppFromEnum.ROUNTINE.getValue())){
//                    //h5地址
//                    siteUrl = siteUrl+"/product/";
//                    //生成二维码
//                    QrCodeUtil.generate(siteUrl+"?productId="+id+"&spread="+uid+"&pageType=good&codeType="+AppFromEnum.ROUNTINE.getValue(), 180, 180,
//                            FileUtil.file(fileDir+name));
//                }
//                else if(userType.equals(AppFromEnum.APP.getValue())){
//                    //h5地址
//                    siteUrl = siteUrl+"/product/";
//                    //生成二维码
//                    QrCodeUtil.generate(siteUrl+"?productId="+id+"&spread="+uid+"&pageType=good&codeType="+AppFromEnum.APP.getValue(), 180, 180,
//                            FileUtil.file(fileDir+name));
//                }else{//如果类型是h5
//                    //生成二维码
//                    QrCodeUtil.generate(siteUrl+"detail/"+id+"?spread="+uid, 180, 180,
//                            FileUtil.file(fileDir+name));
//                }
//                systemAttachmentService.attachmentAdd(name,String.valueOf(FileUtil.size(file)),
//                        fileDir+name,"qrcode/"+name);
//
//               // qrcodeUrl = apiUrl + "/api/file/qrcode/"+name;
//            }else{
//               // qrcodeUrl = apiUrl + "/api/file/" + attachment.getSattDir();
//            }
//                //String spreadPicName = id+"_"+uid + "_"+userType+"_product_user_spread.jpg";
//               // String spreadPicPath = fileDir+spreadPicName;
////                String rr =  creatShareProductService.creatProductPic(productDTO,qrcodeUrl,spreadPicName,spreadPicPath,apiUrl);
////                productDTO.getStoreInfo().setCodeBase(rr);
        return ApiResult.ok(productDTO);
    }

    /**
     * 添加收藏
     */
    @Log(value = "添加收藏", type = 1)
    @PostMapping("/collect/add")
    @ApiOperation(value = "添加收藏", notes = "添加收藏")
    public ApiResult<Object> collectAdd(@Validated @RequestBody YxStoreProductRelationQueryParam param) {
        int uid = SecurityUtils.getUserId().intValue();
        productRelationService.addRroductRelation(param, uid, "collect");
        return ApiResult.ok("success");
    }

    /**
     * 取消收藏
     */
    @Log(value = "取消收藏", type = 1)
    @PostMapping("/collect/del")
    @ApiOperation(value = "取消收藏", notes = "取消收藏")
    public ApiResult<Object> collectDel(@Validated @RequestBody YxStoreProductRelationQueryParam param) {
        int uid = SecurityUtils.getUserId().intValue();
        productRelationService.delRroductRelation(param, uid, "collect");
        return ApiResult.ok("success");
    }

    /**
     * 获取产品评论
     */
    @GetMapping("/reply/list/{id}")
    @ApiOperation(value = "获取产品评论", notes = "获取产品评论")
    public ApiResult<Object> replyList(@PathVariable Integer id,
                                       YxStoreProductQueryParam queryParam) {
        return ApiResult.ok(replyService.getReplyList(id, Integer.valueOf(queryParam.getType()),
                queryParam.getPage().intValue(), queryParam.getLimit().intValue()));
    }

    /**
     * 获取产品评论数据
     */
    @GetMapping("/reply/config/{id}")
    @ApiOperation(value = "获取产品评论数据", notes = "获取产品评论数据")
    public ApiResult<Object> replyCount(@PathVariable Integer id) {
        return ApiResult.ok(replyService.getReplyCount(id));
    }

    @AnonymousAccess
    @GetMapping("/allVideoTypes")
    @ApiOperation(value = "视频类型", notes = "视频类型")
    public ApiResult<List<YxStoreProductQueryVo>> allVideoTypes() {
        return ApiResult.ok(videoTypeService.selectVideoTypeList());
    }

    @AnonymousAccess
    @GetMapping("/getVideos")
    @ApiOperation(value = "视频列表", notes = "视频列表")
    public ApiResult<List<YxVideo>> getVideos(YxVideoQueryParam videoQueryParam) {
        return ApiResult.ok(videoService.selectVideoList(videoQueryParam.getTypeId()));
    }

    @AnonymousAccess
    @GetMapping("/videoProducts/{id}")
    @ApiOperation(value = "商品列表", notes = "商品列表")
    public ApiResult<List<YxStoreProductQueryVo>> videoProducts(@PathVariable Integer id) {
        YxVideo video= videoService.getById(id);
        int watchNum=video.getWatchNum()!=null?video.getWatchNum():0;
        video.setWatchNum(watchNum+1);
        videoService.updateById(video);
        YxVideoQueryVo videoInfo=videoService.covertVideoQuery(video);
        YxStoreProductQueryParam param=new YxStoreProductQueryParam();
        param.setCateId(String.valueOf(id));
        List<YxStoreProductQueryVo>  productList=storeProductService.getVideoGoodsList(param);
        videoInfo.setProductList(productList);
        return ApiResult.ok(videoInfo);
    }


}

