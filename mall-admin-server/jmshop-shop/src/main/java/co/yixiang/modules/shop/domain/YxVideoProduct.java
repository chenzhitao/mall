package co.yixiang.modules.shop.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @author yushen
* @date 2023-06-08
*/
@Entity
@Data
@Table(name="yx_video_product")
public class YxVideoProduct implements Serializable {

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /** 视频ID */
    @Column(name = "video_id")
    private Integer videoId;

    /** 商品ID */
    @Column(name = "product_id")
    private Integer productId;

    /** 商品名称 */
    @Column(name = "product_name")
    private String productName;

    /** 排序编号 */
    @Column(name = "sort_no")
    private Integer sortNo;

    /** 添加时间 */
    @Column(name = "add_time")
    private Integer addTime;

    public void copy(YxVideoProduct source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}