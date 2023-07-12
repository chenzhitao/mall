package co.yixiang.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author hupeng
* @date 2019-10-06
*/
@Entity
@Data
@Table(name="yx_user_level")
public class YxUserLevel implements Serializable {

    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 用户id
    @Column(name = "uid")
    private Integer uid;

    // 等级vip
    @Column(name = "level_id",nullable = false)
    private Integer levelId;

    // 会员等级
    @Column(name = "grade",nullable = false)
    private Integer grade;

    // 过期时间
    @Column(name = "valid_time",nullable = false)
    private Integer validTime;

    // 是否永久
    @Column(name = "is_forever",nullable = false)
    private Integer isForever;

    // 商户id
    @Column(name = "mer_id",nullable = false)
    private Integer merId;

    // 0:禁止,1:正常
    @Column(name = "status",nullable = false)
    private Integer status;

    // 备注
    @Column(name = "mark",nullable = false)
    private String mark;

    // 是否已通知
    @Column(name = "remind",nullable = false)
    private Integer remind;

    // 是否删除,0=未删除,1=删除
    @Column(name = "is_del",nullable = false)
    private Integer isDel;

    // 添加时间
    @Column(name = "add_time",nullable = false)
    private Integer addTime;

    // 享受折扣
    @Column(name = "discount",nullable = false)
    private Integer discount;

    public void copy(YxUserLevel source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}