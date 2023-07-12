package co.yixiang.utils;

import cn.hutool.http.HttpUtil;
import co.yixiang.enums.RedisKeyEnum;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.SecurityUtils;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.*;

@Slf4j
public class TencentMsgUtil {

    public static String sendTemplateMsg(Map<String,Object> data,String templateId,String openId){
        Map<String, Object> tokenMap= getAccessToken();
        String wxAccessToken=String.valueOf(tokenMap.get("access_token"));
        Map<String, Object> body = new HashMap<>();
        body.put("access_token ",wxAccessToken );
        body.put("touser",openId);
        body.put("template_id",templateId);
//        body.put("miniprogram_state","trial");
        body.put("page","pages/home/index");
        body.put("data", JSON.toJSON(data));
        log.info(JSON.toJSONString(data));
        String res = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+wxAccessToken, JSONUtils.toJSONString(body));
        log.info(res);
        return res;
    }

    public static Map<String, Object> getAccessToken() {
        /*Map<String, Object> access_token = RedisUtil.get("zp_access_token");
        if (access_token!=null) {
            return access_token;
        }else{
            RedisUtil.del("zp_access_token");
        }*/
        //读取redis配置
        String appId = RedisUtil.get(RedisKeyEnum.WXAPP_APPID.getValue());
        String secret = RedisUtil.get(RedisKeyEnum.WXAPP_SECRET.getValue());
        //拼接url
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token?");
        //appid设置
        url.append("grant_type=");
        url.append("client_credential");
        //secret设置
        url.append("&appid=");
        url.append(appId);
        //code设置
        url.append("&secret=");
        url.append(secret);
        Map<String, Object> map = null;
        try {
            //构建一个Client
            HttpClient client = HttpClientBuilder.create().build();
            //构建一个GET请求
            HttpGet get = new HttpGet(url.toString());
            //提交GET请求
            HttpResponse response = client.execute(get);
            //拿到返回的HttpResponse的"实体"
            HttpEntity result = response.getEntity();
            String content = EntityUtils.toString(result);
            //打印返回的信息
            System.out.println(content);
            //把信息封装为json
            JSONObject res = JSONObject.parseObject(content);
            //把信息封装到map
            map = parseJSON2Map(res);

            Integer expiresIn = (Integer) map.get("expires_in");
            int expire = expiresIn - 200;
            RedisUtil.set("zp_access_token", map,expire);


        } catch (Exception e) {
            log.info("获取getAccessToken失败:{}", e.getMessage());
        }
        return map;
    }

    public static Map<String, Object> parseJSON2Map(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                @SuppressWarnings("unchecked")
                Iterator it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = (JSONObject) it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }
}
