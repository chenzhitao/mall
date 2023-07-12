package co.yixiang.modules.order.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName StatusDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/30
 **/
@Data
public class StatusDTO implements Serializable {
    private String _class;
    private String _msg;
    private String _payType;
    private String _title;
    private String _type;
}
