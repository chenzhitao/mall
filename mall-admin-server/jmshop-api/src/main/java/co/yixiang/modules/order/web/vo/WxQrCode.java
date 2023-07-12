package co.yixiang.modules.order.web.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WxQrCode {

    //获取AccessToken路径
    private static final String AccessToken_URL
            = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";//小程序id
    //获取二维码路径
    private static final String WxCode_URL
            = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN";//小程序密钥

    /**
     * 用于获取access_token
     *
     * @return access_token
     * @throws Exception
     */
    public static String getAccessToken(String appid, String secret) {
        try {
            String requestUrl = AccessToken_URL.replace("APPID", appid).replace("APPSECRET", secret);
            URL url = new URL(requestUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 设置通用的请求属性
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // 得到请求的输出流对象
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes("");
            out.flush();
            out.close();

            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = null;
            if (requestUrl.contains("nlp"))
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
            else
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String result = "";
            String getLine;
            while ((getLine = in.readLine()) != null) {
                result += getLine;
            }
            in.close();
            JSONObject jsonObject = JSON.parseObject(result);
            String accesstoken = jsonObject.getString("access_token");
            return accesstoken;
        } catch (Exception e) {

        }
        return null;
    }


    public static ByteArrayInputStream sendPost(String URL, String json) {
        InputStream inputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(URL);
        httppost.addHeader("Content-type", "application/json; charset=utf-8");
        httppost.setHeader("Accept", "application/json");
        try {
            StringEntity s = new StringEntity(json, Charset.forName("UTF-8"));
            s.setContentEncoding("UTF-8");
            httppost.setEntity(s);
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 获取相应实体
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                // 创建一个Buffer字符串
                byte[] buffer = new byte[1024];
                // 每次读取的字符串长度，如果为-1，代表全部读取完毕
                int len = 0;
                // 使用一个输入流从buffer里把数据读取出来
                while ((len = inputStream.read(buffer)) != -1) {
                    // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                    outStream.write(buffer, 0, len);
                }
                // 关闭输入流
                inputStream.close();
                // 把outStream里的数据写入内存
                byteArrayInputStream = new ByteArrayInputStream(outStream.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArrayInputStream;
    }


    /**
     * 生成小程序二维码
     *
     * @param access_token
     * @param wxPath
     * @param wxMachineNo
     * @param path
     * @return
     */
    public static String getWXCode(String access_token, String wxPath, String wxMachineNo, String path) {
        String aiyunUrl = "";
        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + access_token;
            Map<String, String> param = new HashMap<>();
            //携带参数放在scene 内
            param.put("scene", wxMachineNo);
            //这里的page如果没有的话可以不写，默认是跳主页，如果写了没有的页面的话，会返回错误信息
//            param.put("page", wxPath);
            String json = JSON.toJSONString(param);
            ByteArrayInputStream inputStream = sendPost(url, json);
            //这里判断的是返回的图片还是错误信息，一般错误信息不会大于200
            if (inputStream.available() <= 200) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int i;
                byte[] buffer = new byte[200];
                while ((i = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, i);
                }
                String str = new String(byteArrayOutputStream.toByteArray());
                //错误信息的格式在官方文档里有
                JSONObject jsonObject = JSONObject.parseObject(str);
                if ("41030".equals(jsonObject.getString("errcode"))) {
                    log.error("所传page页面不存在，或者小程序没有发布");
                } else if ("45009".equals(jsonObject.getString("errcode"))) {
                    log.error("调用分钟频率受限");
                }
                byteArrayOutputStream.close();
            }
//            上传阿里云，也可以不上传
//            String aiyunUrl = ossClientUtil.UploadImgAndReturnImgUrlInputStream(inputStream, fileName, path);
            //输出到本地的代码
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            int i;
            byte[] buffer = new byte[200];
            while ((i = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, i);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            inputStream.close();
            log.error("二维码生成成功");
        } catch (Exception e) {
            log.error("获取二维码异常");
        }
        return aiyunUrl;
    }

    /**
     * 生成小程序二维码
     *
     * @param appId
     * @param secret
     * @param wxPath      页面路径
     * @param wxMachineNo 页面参数
     * @param path
     * @return
     */
    public static String getWXCode(String appId, String secret, String wxPath, String wxMachineNo, String path) {
        //这里调用的是上面的获取access_token方法
        String access_token = getAccessToken(appId, secret);
        //
        return getWXCode(access_token, wxPath, wxMachineNo, path);
    }

    /*
     * 获取 二维码图片
     *
     */
    public static String getminiqrQr(String accessToken, String uploadPath, String urlPath) {
        File file = new File(uploadPath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
//            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacode?access_token="+accessToken);
            String wxCodeURL = WxCode_URL.replace("ACCESS_TOKEN", accessToken);
            URL url = new URL(wxCodeURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
            paramJson.put("scene", "1234567890");
            paramJson.put("page", urlPath); //小程序暂未发布我没有带page参数
            paramJson.put("width", 430);
            paramJson.put("is_hyaline", true);
            paramJson.put("auto_color", true);
            /**
             * line_color生效
             * paramJson.put("auto_color", false);
             * JSONObject lineColor = new JSONObject();
             * lineColor.put("r", 0);
             * lineColor.put("g", 0);
             * lineColor.put("b", 0);
             * paramJson.put("line_color", lineColor);
             * */

            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            OutputStream os = new FileOutputStream(file);
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadPath;
    }

}
