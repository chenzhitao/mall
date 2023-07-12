package co.yixiang.modules.order.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PayDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/7
 **/
@Data
public class PayDTO implements Serializable {
    private String from;
    private String paytype;
    private String uni;
}
