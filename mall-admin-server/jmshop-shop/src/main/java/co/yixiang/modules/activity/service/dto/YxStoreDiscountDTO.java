package co.yixiang.modules.activity.service.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
* @author xuwenbo
* @date 2019-12-22
*/
@Data
public class YxStoreDiscountDTO implements Serializable {

    // 折扣率ID
    private Integer id;

    // 关联产品ID
    private Integer productId;

    // 会员等级
    private Integer grade;

    // 享受折扣
    private Integer discount;

    // 砍价活动名称
    private String title;

    // 砍价开启时间
    private Integer startTime;

    // 砍价结束时间
    private Integer stopTime;

    // 砍价状态 0(到砍价时间不自动开启)  1(到砍价时间自动开启时间)
    private Integer status;

    // 活动简介
    private String info;

    // 排序
    private Integer sort;

    // 是否删除 0未删除 1删除
    private Integer isDel;

    // 添加时间
    private Integer addTime;

    private String jsonStr;

    private Date startTimeDate;

    private Date endTimeDate;

}