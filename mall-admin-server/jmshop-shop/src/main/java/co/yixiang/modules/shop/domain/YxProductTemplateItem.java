package co.yixiang.modules.shop.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @author yushen
* @date 2023-05-11
*/
@Entity
@Data
@Table(name="yx_product_template_item")
public class YxProductTemplateItem implements Serializable {

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /** 模板ID */
    @Column(name = "template_id")
    private Integer templateId;

    /** 商品ID */
    @Column(name = "product_id")
    private Integer productId;

    /** 商品名称 */
    @Column(name = "product_name")
    private String productName;

    /** 备注 */
    @Column(name = "description")
    private String description;

    /** 排序编号 */
    @Column(name = "sort_no")
    private Integer sortNo;

    /** 添加时间 */
    @Column(name = "add_time")
    private Integer addTime;

    /** 是否参与排名  0否  1是 */
    @Column(name = "rank_flag")
    private Integer rankFlag;

    public void copy(YxProductTemplateItem source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}