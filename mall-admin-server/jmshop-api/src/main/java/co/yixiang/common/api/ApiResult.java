package co.yixiang.common.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * REST API 返回结果
 * </p>
 *
 * @author hupeng
 * @since 2019-10-16
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResult<T> implements Serializable {

    private int status;

    private T data;

    private String msg;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    public ApiResult() {

    }

    public static ApiResult result(boolean flag){
        if (flag){
            return ok();
        }
        return fail("");
    }

    public static ApiResult result(ApiCode apiCode){
        return result(apiCode,null);
    }

    public static ApiResult result(ApiCode apiCode,Object data){
        return result(apiCode,null,data);
    }

    public static ApiResult result(ApiCode apiCode,String msg,Object data){
        String message = apiCode.getMsg();
        if (StringUtils.isNotBlank(msg)){
            message = msg;
        }
        return ApiResult.builder()
                .status(apiCode.getCode())
                .msg(message)
                .data(data)
                .time(new Date())
                .build();
    }

    public static ApiResult ok(){
        return ok(null);
    }

    public static ApiResult ok(Object data){
        return result(ApiCode.SUCCESS,data);
    }

    public static ApiResult ok(Object data,String msg){
        return result(ApiCode.SUCCESS,msg,data);
    }

    public static ApiResult ok(String key,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        return ok(map);
    }

    public static ApiResult fail(ApiCode apiCode){
        return result(apiCode,null);
    }

    public static ApiResult fail(String msg){
        return result(ApiCode.FAIL,msg,null);

    }

    public static ApiResult fail(ApiCode apiCode,Object data){
        if (ApiCode.SUCCESS == apiCode){
            throw new RuntimeException("失败结果状态码不能为" + ApiCode.SUCCESS.getCode());
        }
        return result(apiCode,data);

    }

    public static ApiResult fail(String key,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        return result(ApiCode.FAIL,map);
    }

}