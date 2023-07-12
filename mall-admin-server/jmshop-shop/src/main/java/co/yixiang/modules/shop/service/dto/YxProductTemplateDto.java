package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author yushen
* @date 2023-05-11
*/
@Data
public class YxProductTemplateDto implements Serializable {

    /** 模板id */
    private Integer id;

    /** 标题 */
    private String title;

    /** 描述 */
    private String description;

    /** 模板分类 */
    private Integer type;

    /** 图片 */
    private String imageUrl;

    /** 商品ID */
    private Integer productId;

    /** 商品名称 */
    private String productName;

    /** 状态（0：未上架，1：上架） */
    private Integer isShow;

    /** 排序编号 */
    private Integer sortNo;

    /** 添加时间 */
    private Integer addTime;

    /** 是否删除 */
    private Integer isDel;
}