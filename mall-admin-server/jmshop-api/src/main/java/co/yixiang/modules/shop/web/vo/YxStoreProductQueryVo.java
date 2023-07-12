package co.yixiang.modules.shop.web.vo;

import cn.hutool.core.util.StrUtil;
import co.yixiang.modules.shop.entity.YxStoreProductAttrValue;
import co.yixiang.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-19
 */
@Data
@ApiModel(value = "YxStoreProductQueryVo对象", description = "商品表查询参数")
public class YxStoreProductQueryVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Integer id;

    @ApiModelProperty(value = "商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)")
    private Integer merId;

    @ApiModelProperty(value = "商品图片")
    private String image;

    @ApiModelProperty(value = "商品海报")
    private String haibaoImage;

    @ApiModelProperty(value = "商品视频")
    private String video;

    private String image_base;

    private String codeBase;

    public String getImage_base()
    {
        return image;
    }

    private Boolean userCollect = false;

    private Boolean userLike = false;

    @ApiModelProperty(value = "轮播图")
    private String sliderImage;

    private List<String> sliderImageArr;

    @ApiModelProperty(value = "商品规格属性")
    private List<YxStoreProductAttrQueryVo> productAttr = new ArrayList();

    @ApiModelProperty(value = "商品规格属性值")
    private Map<String, YxStoreProductAttrValue> productValue = new LinkedHashMap<>();

    public List<String> getSliderImageArr()
    {
        //Arrays.asList(sliderImage.split(","));
        if (StrUtil.isNotEmpty(sliderImage))
        {
            return Arrays.asList(sliderImage.split(","));
        }

        return new ArrayList<>();

    }

    private YxStoreProductAttrValue attrInfo;

    @ApiModelProperty(value = "商品名称")
    private String storeName;

    @ApiModelProperty(value = "商品简介")
    private String storeInfo;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "产品条码（一维码）")
    private String barCode;

    @ApiModelProperty(value = "分类id")
    private String cateId;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "商品最高价格")
    private BigDecimal maxPrice;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "会员价格")
    private BigDecimal vipPrice;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "市场价")
    private BigDecimal otPrice;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "邮费")
    private BigDecimal postage;

    @ApiModelProperty(value = "单位名")
    private String unitName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "状态（0：未上架，1：上架）")
    private Integer isShow;

    @ApiModelProperty(value = "是否热卖")
    private Integer isHot;

    @ApiModelProperty(value = "是否优惠")
    private Integer isBenefit;

    @ApiModelProperty(value = "是否精品")
    private Integer isBest;

    @ApiModelProperty(value = "是否新品")
    private Integer isNew;

    @ApiModelProperty(value = "产品描述")
    private String description;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "是否包邮")
    private Integer isPostage;

    @ApiModelProperty(value = "是否删除")
    private Integer isDel;

    @ApiModelProperty(value = "是否参与排名")
    private Integer isRank;

    @ApiModelProperty(value = "商户是否代理 0不可代理1可代理")
    private Integer merUse;

    @ApiModelProperty(value = "获得积分")
    private BigDecimal giveIntegral;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "成本价")
    private BigDecimal cost;

    @ApiModelProperty(value = "秒杀状态 0 未开启 1已开启")
    private Integer isSeckill;

    @ApiModelProperty(value = "砍价状态 0未开启 1开启")
    private Integer isBargain;

    @ApiModelProperty(value = "是否优品推荐")
    private Integer isGood;

    @ApiModelProperty(value = "虚拟销量")
    private Integer ficti;

    @ApiModelProperty(value = "浏览量")
    private Integer browse;

    @ApiModelProperty(value = "产品二维码地址(用户小程序海报)")
    private String codePath;

    @ApiModelProperty(value = "淘宝京东1688类型")
    private String soureLink;

    @ApiModelProperty(value = "商品包装")
    private String packaging;

    @ApiModelProperty(value = "排名信息")
    private String rankInfo;


    private Integer salesNum;

    //展示类型 1商品2是广告
    private int showType;

    public String getRankInfo() {
        return rankInfo;
    }

    public Integer getSalesNum() {
        return this.sales;
    }
}
