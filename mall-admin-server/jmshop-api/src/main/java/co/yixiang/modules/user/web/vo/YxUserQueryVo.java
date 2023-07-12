package co.yixiang.modules.user.web.vo;

import co.yixiang.modules.order.web.dto.OrderCountDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-16
 */
@Data
@ApiModel(value = "YxUserQueryVo对象", description = "用户表查询参数")
public class YxUserQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "用户账户(跟accout一样)")
    private String username;

    @ApiModelProperty(value = "用户账号")
    private String account;

    private Integer couponCount = 0;

    private OrderCountDTO orderStatusNum;

    private Integer statu;

    private Integer sumSignDay;

    private Boolean isDaySign;

    private Boolean isYesterDaySign;

    private Boolean checkStatus;



//    @ApiModelProperty(value = "用户密码（跟pwd）")
//    @JsonIgnore
//    private String password;
//
//    @ApiModelProperty(value = "用户密码")
//    @JsonIgnore
//    private String pwd;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "生日")
    private Integer birthday;

    @ApiModelProperty(value = "身份证号码")
    @JsonIgnore
    private String cardId;

    @ApiModelProperty(value = "用户备注")
    private String mark;

    @ApiModelProperty(value = "合伙人id")
    @JsonIgnore
    private Integer partnerId;

    @ApiModelProperty(value = "用户分组id")
    @JsonIgnore
    private Integer groupId;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "添加ip")
    private String addIp;

    @ApiModelProperty(value = "最后一次登录时间")
    private Integer lastTime;

    @ApiModelProperty(value = "最后一次登录ip")
    private String lastIp;

    @ApiModelProperty(value = "用户余额")
    private BigDecimal nowMoney;

    @ApiModelProperty(value = "佣金金额")
    private BigDecimal brokeragePrice;

    @ApiModelProperty(value = "用户剩余积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "连续签到天数")
    private Integer signNum;

    @ApiModelProperty(value = "1为正常，0为禁止")
    private Boolean status;

    @ApiModelProperty(value = "等级")
    private Integer level;

    @ApiModelProperty(value = "推广元id")
    private Integer spreadUid;

    @ApiModelProperty(value = "推广员关联时间")
    private Integer spreadTime;

    @ApiModelProperty(value = "用户类型")
    private String userType;

    @ApiModelProperty(value = "是否为推广员")
    private Integer isPromoter;

    @ApiModelProperty(value = "用户购买次数")
    private Integer payCount;

    @ApiModelProperty(value = "下级人数")
    private Integer spreadCount;

    @ApiModelProperty(value = "清理会员时间")
    private Integer cleanTime;

    @ApiModelProperty(value = "详细地址")
    private String addres;

    @ApiModelProperty(value = "管理员编号 ")
    private Integer adminid;

    @ApiModelProperty(value = "用户登陆类型，h5,wechat,routine")
    private String loginType;

    private Boolean vip;

    private Integer vipId;

    private String vipIcon;

    private String vipName;

    @ApiModelProperty(value = "用户申请会员级别 ")
    private Integer apply_level;

    @ApiModelProperty(value = "用户申请会员材料 ")
    private String certInfo;

    @ApiModelProperty(value = "是否审核 ")
    private Integer isCheck;

    @ApiModelProperty(value = "是否通过 ")
    private Integer isPass;

    @ApiModelProperty(value = "微信 ")
    private String weixin;

    @ApiModelProperty(value = "qq ")
    private String qq;

    @ApiModelProperty(value = "邮箱 ")
    private String mail;

    @ApiModelProperty(value = "电话 ")
    private String telephone;

    @ApiModelProperty(value = "钉钉 ")
    private String dingding;

}
