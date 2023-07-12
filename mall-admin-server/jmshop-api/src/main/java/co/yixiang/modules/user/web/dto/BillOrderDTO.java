package co.yixiang.modules.user.web.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName BillDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class BillOrderDTO {
    private String time;
    private Integer count;
    private List<BillOrderRecordDTO> child;
}
