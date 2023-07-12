package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 文章管理表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-02
 */
@Data
@ApiModel(value = "YxProductTemplateVo对象", description = "文章模板查询参数")
public class YxProductTemplateVo implements Serializable {
    private static final long serialVersionUID = 1L;

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

    private YxStoreProductQueryVo productQueryVo;

    private List<YxStoreProductQueryVo> productList;

}
