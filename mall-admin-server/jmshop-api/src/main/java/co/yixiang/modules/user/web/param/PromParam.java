package co.yixiang.modules.user.web.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PromParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class PromParam  implements Serializable {
    private Integer grade;
    private String  keyword;
    private Integer limit;
    private Integer page;
    private String  sort;
}
