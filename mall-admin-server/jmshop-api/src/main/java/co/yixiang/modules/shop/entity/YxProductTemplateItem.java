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

/**
* @author yushen
* @date 2023-05-11
*/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxProductTemplate对象", description = "产品模板表")
public class YxProductTemplateItem extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /** 模板id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
