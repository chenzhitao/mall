package co.yixiang.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 汽车之家接口调用
 * @author MaRongFu
 * @since 2021-03-29
 */
@Service
public class HuaweiObsfaceService {
    @Value("${huaweicloud.obs.end_point}")
    String endPoint;

    @Value("${huaweicloud.obs.accessible_domain_name}")
    String accessibleDomainName;

    @Value("${huaweicloud.obs.access_key_id}")
    String accessKeyId;

    @Value("${huaweicloud.obs.secret_access_key}")
    String secretAccessKey;

    @Value("${huaweicloud.obs.bucket_name}")
    String bucketName;

    @Value("${huaweicloud.obs.local_file_path}")
    String localFilePath;

    @Value("${huaweicloud.obs.cloud_file_path}")
    String cloudFilePath;


    public String upload(InputStream input, String fullName)throws Exception {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(accessKeyId, secretAccessKey, endPoint);
        //文件目录
        SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM/dd");
        String fileSufexx=fullName.substring(fullName.lastIndexOf(".")+1);
        //云存储目录位置
        String toFilePath = cloudFilePath+"/"+fileSufexx+dateFormat.format(new Date());
        //存储对象
        String objectName = toFilePath+"/"+fullName;
        File file = new File(localFilePath);
        //用来测试此路径名表示的文件或目录是否存在
        if (!file.exists()) {
            //创建目录
            file.mkdirs();
        }
        PutObjectRequest request = new PutObjectRequest(bucketName, objectName);
        // localfile为待上传的本地文件路径，需要指定到具体的文件名
        request.setFile(new File(localFilePath+"/"+fullName));

        //上传网络流
        PutObjectResult putObjectResult = obsClient.putObject(bucketName, objectName, input);
        return accessibleDomainName+"/"+putObjectResult.getObjectKey();
    }

}
