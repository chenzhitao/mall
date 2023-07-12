package co.yixiang.modules.activity.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
* @author xuwenbo
* @date 2019-12-22
*/
@Entity
@Data
@Table(name="yx_store_discount")
public class YxStoreDiscount implements Serializable {

    // 折扣率ID
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 关联产品ID
    @Column(name = "product_id",nullable = false)
    private Integer productId;

    // 会员等级
    @Column(name = "grade",nullable = false)
    private Integer grade;

    // 享受折扣
    @Column(name = "discount",nullable = false)
    private Integer discount;

    // 砍价活动名称
    @Column(name = "title",nullable = false)
    private String title;

    // 砍价开启时间
    @Column(name = "start_time",nullable = false)
    private Integer startTime;

    // 砍价结束时间
    @Column(name = "stop_time",nullable = false)
    private Integer stopTime;

    // 砍价状态 0(到砍价时间不自动开启)  1(到砍价时间自动开启时间)
    @Column(name = "status")
    private Integer status;

    // 活动简介
    @Column(name = "info")
    private String info;

    // 排序
    @Column(name = "sort")
    private Integer sort;

    // 是否删除 0未删除 1删除
    @Column(name = "is_del")
    private Integer isDel;

    // 添加时间
    @Column(name = "add_time")
    private Integer addTime;

    @Column(name = "json_str")
    private String jsonStr;

    @NotNull(message = "开始时间不能为空")
    private Date startTimeDate;

    @NotNull(message = "结束时间不能为空")
    private Date endTimeDate;

    public void copy(YxStoreDiscount source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}