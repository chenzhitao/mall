package co.yixiang.modules.activity.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
* @author xuwenbo
* @date 2019-12-22
*/
@Data
public class YxStoreBargainDTO implements Serializable {

    // 砍价产品ID
    private Integer id;

    // 关联产品ID
    private Integer productId;

    // 砍价活动名称
    private String title;

    // 砍价活动图片
    private String image;

    // 单位名称
    private String unitName;

    // 库存
    private Integer stock;

    // 销量
    private Integer sales;

    // 砍价产品轮播图
    private String images;

    // 砍价开启时间
    private Integer startTime;

    // 砍价结束时间
    private Integer stopTime;

    // 砍价产品名称
    private String storeName;

    // 砍价金额
    private BigDecimal price;

    // 砍价商品最低价
    private BigDecimal minPrice;

    // 每次购买的砍价产品数量
    private Integer num;

    // 用户每次砍价的最大金额
    private BigDecimal bargainMaxPrice;

    // 用户每次砍价的最小金额
    private BigDecimal bargainMinPrice;

    // 用户每次砍价的次数
    private Integer bargainNum;

    // 砍价状态 0(到砍价时间不自动开启)  1(到砍价时间自动开启时间)
    private Integer status;

    // 砍价详情
    private String description;

    // 反多少积分
    private BigDecimal giveIntegral;

    // 砍价活动简介
    private String info;

    // 成本价
    private BigDecimal cost;

    // 排序
    private Integer sort;

    // 是否推荐0不推荐1推荐
    private Integer isHot;

    // 是否删除 0未删除 1删除
    private Integer isDel;

    // 添加时间
    private Integer addTime;

    // 是否包邮 0不包邮 1包邮
    private Integer isPostage;

    // 邮费
    private BigDecimal postage;

    // 砍价规则
    private String rule;

    // 砍价产品浏览量
    private Integer look;

    // 砍价产品分享量
    private Integer share;

    private Date startTimeDate;

    private Date endTimeDate;
}