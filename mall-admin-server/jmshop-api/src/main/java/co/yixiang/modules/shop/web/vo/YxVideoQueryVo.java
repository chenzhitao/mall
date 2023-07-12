package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
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
@ApiModel(value = "YxVideoVo对象", description = "视频查询参数")
public class YxVideoQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /** 类型ID */
    private Integer typeId;

    /** 类型名称 */
    private String typeName;

    /** 标题 */
    private String title;

    /** 视频封面 */
    private String coverImage;

    /** 视频地址 */
    private String videoUrl;

    /** 奖励积分数量 */
    private Integer scoreNum;

    /** 真实浏览量 */
    private Integer watchNum;

    /** 虚拟浏览量 */
    private Integer virtualWatchNum;

    /** 创建时间 */
    private Integer createTime;

    /** 备注 */
    private String remark;

    /** 是否展示 */
    private String showFlag;

    /** 排序 */
    private Integer sortNo;

    @Transient
    private List<YxStoreProductQueryVo> productList;

}
