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
@Table(name="yx_video_type")
public class YxVideoType implements Serializable {

    /** id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /** 类型名称 */
    @Column(name = "type_name")
    private String typeName;

    /** 备注 */
    @Column(name = "remark")
    private String remark;

    /** 是否展示 */
    @Column(name = "show_flag")
    private String showFlag;

    /** 排序 */
    @Column(name = "sort_no")
    private Integer sortNo;

    public void copy(YxVideoType source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
