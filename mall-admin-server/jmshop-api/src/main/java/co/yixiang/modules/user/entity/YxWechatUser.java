package co.yixiang.modules.user.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 微信用户表
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxWechatUser对象", description = "微信用户表")
public class YxWechatUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "微信用户id")
    @TableId(value = "uid", type = IdType.INPUT)
    private Integer uid;

    @ApiModelProperty(value = "只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段")
    private String unionid;

    @ApiModelProperty(value = "用户的标识，对当前公众号唯一")
    private String openid;

    @ApiModelProperty(value = "小程序唯一身份ID")
    private String routineOpenid;

    @ApiModelProperty(value = "用户的昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String headimgurl;

    @ApiModelProperty(value = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer sex;

    @ApiModelProperty(value = "用户所在城市")
    private String city;

    @ApiModelProperty(value = "用户的语言，简体中文为zh_CN")
    private String language;

    @ApiModelProperty(value = "用户所在省份")
    private String province;

    @ApiModelProperty(value = "用户所在国家")
    private String country;

    @ApiModelProperty(value = "公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注")
    private String remark;

    @ApiModelProperty(value = "用户所在的分组ID（兼容旧的用户分组接口）")
    private Integer groupid;

    @ApiModelProperty(value = "用户被打上的标签ID列表")
    private String tagidList;

    @ApiModelProperty(value = "用户是否订阅该公众号标识")
    private Integer subscribe;

    @ApiModelProperty(value = "关注公众号时间")
    private Integer subscribeTime;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "一级推荐人")
    private Integer stair;

    @ApiModelProperty(value = "二级推荐人")
    private Integer second;

    @ApiModelProperty(value = "一级推荐人订单")
    private Integer orderStair;

    @ApiModelProperty(value = "二级推荐人订单")
    private Integer orderSecond;

    @ApiModelProperty(value = "佣金")
    private BigDecimal nowMoney;

    @ApiModelProperty(value = "小程序用户会话密匙")
    private String sessionKey;

    @ApiModelProperty(value = "用户类型")
    private String userType;

}
