package co.yixiang.modules.order.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName OrderExtendDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class OrderExtendDTO implements Serializable {
    private String key;
    private String orderId;
    private Map<String,String> jsConfig;
}
