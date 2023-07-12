package co.yixiang.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
* @author hupeng
* @date 2019-12-04
*/
@Data
public class YxSystemUserLevelDTO implements Serializable {

    private Integer id;

    // 商户id
    private Integer merId;

    // 会员名称
    private String name;

    // 购买金额
    private BigDecimal money;

    // 有效时间
    private Integer validDate;

    // 是否为永久会员
    private Integer isForever;

    // 是否购买,1=购买,0=不购买
    private Integer isPay;

    // 是否显示 1=显示,0=隐藏
    private Integer isShow;

    // 会员等级
    private Integer grade;

    // 享受折扣
    private BigDecimal discount;

    // 会员卡背景
    private String image;

    // 会员图标
    private String icon;

    // 说明
    private String explain;

    // 添加时间
    private Integer addTime;

    // 是否删除.1=删除,0=未删除
    private Integer isDel;
}