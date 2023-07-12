package co.yixiang.modules.user.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @ClassName BillDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class BillOrderRecordDTO {
    private String orderId;
    private String time;
    private Double number;
    private String avatar;
    private String nickname;
}
