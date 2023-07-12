package co.yixiang.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.aop.log.Log;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.modules.shop.domain.YxStoreProduct;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.dto.YxStoreProductDTO;
import co.yixiang.modules.shop.service.dto.YxStoreProductQueryCriteria;
import co.yixiang.utils.HuaweiObsfaceService;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hupeng
 * @date 2019-10-04
 */
@Api(tags = "商城:商品管理")
@RestController
@RequestMapping("api")
public class StoreProductController
{

    private final YxStoreProductService yxStoreProductService;
    private final HuaweiObsfaceService huaweiObsfaceService;

    private final YxSystemConfigService systemConfigService;


    public StoreProductController(YxStoreProductService yxStoreProductService,HuaweiObsfaceService huaweiObsfaceService,YxSystemConfigService systemConfigService)
    {
        this.yxStoreProductService = yxStoreProductService;
        this.huaweiObsfaceService = huaweiObsfaceService;
        this.systemConfigService = systemConfigService;
    }

    private Map<String, Object> getYxStoreProductList(YxStoreProductQueryCriteria criteria, Pageable pageable)
    {
        return yxStoreProductService.queryAll(criteria, pageable);
    }

    @Log("查询商品")
    @ApiOperation(value = "查询商品")
    @GetMapping(value = "/yxStoreProduct")
    @PreAuthorize("@el.check('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_SELECT')")
    public ResponseEntity getYxStoreProducts(YxStoreProductQueryCriteria criteria, Pageable pageable)
    {
        return new ResponseEntity(getYxStoreProductList(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增商品")
    @ApiOperation(value = "新增商品")
    @CacheEvict(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PostMapping(value = "/yxStoreProduct")
    @PreAuthorize("@el.check('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YxStoreProduct resources)
    {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        int id = (int) (Math.random() * (352324 + 1));
        if (ObjectUtil.isNull(resources.getId()))
        {
            resources.setId(id);
        }
        resources.setAddTime(OrderUtil.getSecondTimestampTwo());
        if (ObjectUtil.isEmpty(resources.getGiveIntegral()))
        {
            resources.setGiveIntegral(BigDecimal.ZERO);
        }
        if (ObjectUtil.isEmpty(resources.getCost()))
        {
            resources.setCost(BigDecimal.ZERO);
        }
        YxStoreProductDTO product= yxStoreProductService.create(resources);
        String qrcodeImage=getWxProductQrcode(product.getId());
        YxStoreProduct imageProduct=new YxStoreProduct();
        imageProduct.setId(product.getId());
        imageProduct.setQrcodeImage(qrcodeImage);
        yxStoreProductService.update(imageProduct);
        return new ResponseEntity(resources, HttpStatus.CREATED);
    }

    public String getWxProductQrcode(Integer productId) {
        String appId=  systemConfigService.findByKey(RedisKeyEnum.WXAPP_APPID.getValue()).getValue();
        String APPSecret=systemConfigService.findByKey(RedisKeyEnum.WXAPP_SECRET.getValue()).getValue();
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&");
        url.append("appid=");//appid设置
        url.append(appId);
        url.append("&secret=");//secret设置
        url.append(APPSecret);
        Map qrcodeResult=new HashMap();
        String qrcodeImg="";
        try {
            HttpClient client = HttpClientBuilder.create().build();//构建一个Client
            HttpGet get = new HttpGet(url.toString());    //构建一个GET请求
            HttpResponse response = client.execute(get);//提交GET请求
            HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
            String content = EntityUtils.toString(result);
            System.out.println(content);//打印返回的信息
            JSONObject res = JSONObject.parseObject(content);//把信息封装为json
            String access_token=res.getString("access_token");



            URL qrcodeUrl = new URL("https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token="+access_token);
            HttpURLConnection httpURLConnection = (HttpURLConnection) qrcodeUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            JSONObject paramJson = new JSONObject();
            paramJson.put("access_token", access_token);
            paramJson.put("path", "/pages/shop/GoodsCon/index?id="+productId);
            paramJson.put("width", 430);
            System.out.println("paramJson--->"+paramJson.toJSONString());
            String fileName =productId+"-QRCode.png";

            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());

            String qrcodePath="/qrcode/"+fileName;

           qrcodeImg= huaweiObsfaceService.upload(bis,qrcodePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrcodeImg;
    }

    @Log("修改商品")
    @ApiOperation(value = "修改商品")
    @CacheEvict(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PutMapping(value = "/yxStoreProduct")
    @PreAuthorize("@el.check('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YxStoreProduct resources)
    {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxStoreProductService.update(resources);

        String qrcodeImage=getWxProductQrcode(resources.getId());
        YxStoreProduct imageProduct=new YxStoreProduct();
        imageProduct.setId(resources.getId());
        imageProduct.setQrcodeImage(qrcodeImage);
        yxStoreProductService.update(imageProduct);
        return new ResponseEntity(resources, HttpStatus.NO_CONTENT);
    }

    @Log("删除商品")
    @ApiOperation(value = "删除商品")
    @CacheEvict(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY, allEntries = true)
    @DeleteMapping(value = "/yxStoreProduct/{id}")
    @PreAuthorize("@el.check('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id)
    {
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxStoreProductService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "恢复数据")
    @CacheEvict(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY, allEntries = true)
    @DeleteMapping(value = "/yxStoreProduct/recovery/{id}")
    @PreAuthorize("@el.check('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_DELETE')")
    public ResponseEntity recovery(@PathVariable Integer id)
    {
        yxStoreProductService.recovery(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "商品上架/下架")
    @CacheEvict(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PostMapping(value = "/yxStoreProduct/onsale/{id}")
    public ResponseEntity onSale(@PathVariable Integer id, @RequestBody String jsonStr)
    {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = Integer.valueOf(jsonObject.get("status").toString());
        yxStoreProductService.onSale(id, status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "生成属性")
    @PostMapping(value = "/yxStoreProduct/isFormatAttr/{id}")
    public ResponseEntity isFormatAttr(@PathVariable Integer id, @RequestBody String jsonStr)
    {
        return new ResponseEntity(yxStoreProductService.isFormatAttr(id, jsonStr), HttpStatus.OK);
    }

    @ApiOperation(value = "设置保存属性")
    @CacheEvict(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PostMapping(value = "/yxStoreProduct/setAttr/{id}")
    public ResponseEntity setAttr(@PathVariable Integer id, @RequestBody String jsonStr)
    {
        yxStoreProductService.createProductAttr(id, jsonStr);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "清除属性")
    @CacheEvict(cacheNames = ShopConstants.JMSHOP_REDIS_INDEX_KEY, allEntries = true)
    @PostMapping(value = "/yxStoreProduct/clearAttr/{id}")
    public ResponseEntity clearAttr(@PathVariable Integer id)
    {
        yxStoreProductService.clearProductAttr(id, true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "获取属性")
    @GetMapping(value = "/yxStoreProduct/attr/{id}")
    public ResponseEntity attr(@PathVariable Integer id)
    {
        String str = yxStoreProductService.getStoreProductAttrResult(id);
        if (StrUtil.isEmpty(str))
        {
            return new ResponseEntity(HttpStatus.OK);
        }
        JSONObject jsonObject = JSON.parseObject(str);

        return new ResponseEntity(jsonObject, HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/yxStoreProduct/download")
    @PreAuthorize("@el.check('admin','yxStoreProduct:list')")
    public void download(HttpServletResponse response, YxStoreProductQueryCriteria criteria, Pageable pageable, @RequestParam(name = "listContent") String listContent) throws IOException, ParseException
    {
        List<YxStoreProductDTO> list = (List) getYxStoreProductList(criteria, pageable).get("content");
        List<YxStoreProductDTO> productList = new ArrayList<>();
        if (StringUtils.isEmpty(listContent))
        {
            yxStoreProductService.download(list, response);
        } else if (listContent.equals("template"))
        {
            yxStoreProductService.download(productList, response);
        } else
        {
            List<String> idList = JSONArray.parseArray(listContent).toJavaList(String.class);
            productList = yxStoreProductService.findByIds(idList);

            yxStoreProductService.download(productList, response);
        }
    }

    @Log("上传商品信息")
    @ApiOperation("上传商品信息")
    @PostMapping(value = "/yxStoreProduct/upload")
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException
    {
        String name = file.getOriginalFilename();
        return new ResponseEntity(yxStoreProductService.excelToList(file.getInputStream()), HttpStatus.CREATED);
    }


}
