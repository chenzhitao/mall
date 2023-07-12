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
* @date 2023-06-08
*/
@Entity
@Data
@Table(name="yx_video")
public class YxVideo implements Serializable {

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /** 类型ID */
    @Column(name = "type_id")
    private Integer typeId;

    /** 类型名称 */
    @Column(name = "type_name")
    private String typeName;

    /** 标题 */
    @Column(name = "title")
    private String title;

    /** 视频封面 */
    @Column(name = "cover_image")
    private String coverImage;

    /** 视频地址 */
    @Column(name = "video_url")
    private String videoUrl;

    /** 奖励积分数量 */
    @Column(name = "score_num")
    private Integer scoreNum;

    /** 真实浏览量 */
    @Column(name = "watch_num")
    private Integer watchNum;

    /** 虚拟浏览量 */
    @Column(name = "virtual_watch_num")
    private Integer virtualWatchNum;

    /** 创建时间 */
    @Column(name = "create_time")
    private Integer createTime;

    /** 备注 */
    @Column(name = "remark")
    private String remark;

    /** 是否展示 */
    @Column(name = "show_flag")
    private String showFlag;

    /** 排序 */
    @Column(name = "sort_no")
    private Integer sortNo;

    @Transient
    private List<YxVideoProduct> itemList;

    public void copy(YxVideo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
