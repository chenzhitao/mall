package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author yushen
* @date 2023-05-11
*/
@Data
public class YxProductTemplateItemDto implements Serializable {

    /** id */
    private Integer id;

    /** 模板ID */
    private Integer templateId;

    /** 商品ID */
    private Integer productId;

    /** 商品名称 */
    private String productName;

    /** 备注 */
    private String description;

    /** 排序编号 */
    private Integer sortNo;

    /** 添加时间 */
    private Integer addTime;

    /** 是否参与排名  0否  1是 */
    private Integer rankFlag;
}