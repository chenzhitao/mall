package co.yixiang.modules.shop.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
* @author yushen
* @date 2023-05-11
*/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxProductTemplate对象", description = "产品模板表")
public class YxProductTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /** 模板id */
    @TableId(value = "id", type = IdType.AUTO)
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

    @Transient
    private List<YxProductTemplateItem> itemList;
}
