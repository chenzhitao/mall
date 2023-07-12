package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author yushen
* @date 2023-06-08
*/
@Data
public class YxVideoTypeDto implements Serializable {

    /** id */
    private Integer id;
    private Integer typeId;

    /** 类型名称 */
    private String typeName;

    /** 备注 */
    private String remark;

    /** 是否展示 */
    private String showFlag;

    /** 排序 */
    private Integer sortNo;
}
