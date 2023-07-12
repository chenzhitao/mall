package co.yixiang.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Map;

/**
 * 建行微信支付签名工具类
 */
@Component
public class CcbSignUtil {

	private String key;

	private static final String url = "https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain?CCB_IBSVersion=V6";
	@Autowired
	private RedisUtils redisUtils;

	public String invokeMethod(Map<Object, Object> param) {
		try {
			// 基础参数
			param.put("MAC", signMac(param));
			// 请求参数

			String requstParam = createLinkStringByGet(param);
			System.out.println("请求参数：" + requstParam);

			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/json;charset=utf8");
			post.setEntity(new StringEntity(requstParam, "UTF-8"));
			HttpResponse response = client.execute(post);

			String result = EntityUtils.toString(response.getEntity());
			JSONObject resultObj = JSONObject.parseObject(result);
			String code = resultObj.getString("SUCCESS");
			String payUrl;
			if ("ture".equals(code)) {
				payUrl = resultObj.getString("PAYURL");
			} else {
				payUrl = "";
			}
			return payUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"code\":\"203\",\"message\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 签名参数封装
	 * @param params
	 * @return
	 */
	private String signMac(Map<Object,Object> params) throws Exception {
		params.put("PUB", "45a33e20b940ea1079f273c9020111");
		return (sign(createLinkStringByGet(params),"UTF-8"));
	}

	/**
	 * MD5加密
	 *
	 * @param str     内容
	 * @param charset 编码方式
	 * @throws Exception
	 */
	private String sign(String str, String charset) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes(charset));
		byte[] result = md.digest();
		StringBuilder sb = new StringBuilder(32);
		for (int i = 0; i < result.length; i++) {
			int val = result[i] & 0xff;
			if (val <= 0xf) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(val));
		}
		return sb.toString().toLowerCase();
	}

	public String getUrl() {
		return url;
	}

	/**
	 *把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	private String createLinkStringByGet(Map<Object, Object> params) {
		String param = Joiner.on("&").withKeyValueSeparator("=").join(params);
		return param;
	}
}
