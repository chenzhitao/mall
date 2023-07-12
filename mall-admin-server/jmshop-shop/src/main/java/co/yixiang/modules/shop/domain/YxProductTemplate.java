package co.yixiang.modules.shop.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
* @author yushen
* @date 2023-05-11
*/
@Entity
@Data
@Table(name="yx_product_template")
public class YxProductTemplate implements Serializable {

    /** 模板id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /** 标题 */
    @Column(name = "title")
    private String title;

    /** 描述 */
    @Column(name = "description")
    private String description;

    /** 模板分类 */
    @Column(name = "type")
    private Integer type;

    /** 图片 */
    @Column(name = "image_url")
    private String imageUrl;

    /** 商品ID */
    @Column(name = "product_id")
    private Integer productId;

    /** 商品名称 */
    @Column(name = "product_name")
    private String productName;

    /** 状态（0：未上架，1：上架） */
    @Column(name = "is_show")
    private Integer isShow;

    /** 排序编号 */
    @Column(name = "sort_no")
    private Integer sortNo;

    /** 添加时间 */
    @Column(name = "add_time")
    private Integer addTime;

    /** 是否删除 */
    @Column(name = "is_del")
    private Integer isDel;

    @Transient
    private List<YxProductTemplateItem> itemList;

    public void copy(YxProductTemplate source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
