package co.yixiang.modules.user.web.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AddressDetailParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class AddressDetailParam  implements Serializable {
    private String city;
    private String district;
    private String province;
}
