package co.yixiang.rest;

import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.domain.QiniuContent;
import co.yixiang.service.LocalStorageService;
import co.yixiang.service.QiNiuService;
import co.yixiang.service.dto.LocalStorageDTO;
import co.yixiang.utils.HuaweiObsfaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hupeng
 * @date 2020-01-09
 */
@Api(tags = "上传统一管理")
@RestController
@RequestMapping("/api/upload")
@SuppressWarnings("unchecked")
public class UploadController {

    @Value("${file.localUrl}")
    private String localUrl;

    private final LocalStorageService localStorageService;

    //private final QiNiuService qiNiuService;

    private final HuaweiObsfaceService huaweiObsfaceService;

    public UploadController(LocalStorageService localStorageService,HuaweiObsfaceService huaweiObsfaceService) {
        this.localStorageService = localStorageService;
        this.huaweiObsfaceService = huaweiObsfaceService;
    }


    @ApiOperation("上传文件")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@RequestParam(defaultValue = "") String name, @RequestParam("file") MultipartFile file){
        String url = "";
        if(StrUtil.isNotEmpty(localUrl)){ //存在走本地
            LocalStorageDTO localStorageDTO = localStorageService.create(name, file);
            url = localUrl+"/file/"+localStorageDTO.getType()+"/"+localStorageDTO.getRealName();
        }else{//走七牛云
            //QiniuContent qiniuContent = qiNiuService.upload(file,qiNiuService.find());
            //url = qiniuContent.getUrl();
            try {
                url=  huaweiObsfaceService.upload(file.getInputStream(), file.getOriginalFilename());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        Map<String,Object> map = new HashMap<>(2);
        map.put("errno",0);
        map.put("link",url);
        return new ResponseEntity(map,HttpStatus.CREATED);
    }



}
