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
public class BillDTO {
    private String time;
    @JsonIgnore
    private String ids;
    private List<UserBillDTO> list;
}
