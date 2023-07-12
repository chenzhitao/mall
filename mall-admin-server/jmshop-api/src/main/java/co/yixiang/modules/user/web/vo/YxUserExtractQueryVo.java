package co.yixiang.modules.user.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户提现表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-11-11
 */
@Data
@ApiModel(value = "YxUserExtractQueryVo对象", description = "用户提现表查询参数")
public class YxUserExtractQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer uid;

    @ApiModelProperty(value = "名称")
    private String realName;

    @ApiModelProperty(value = "bank = 银行卡 alipay = 支付宝wx=微信")
    private String extractType;

    @ApiModelProperty(value = "银行卡")
    private String bankCode;

    @ApiModelProperty(value = "开户地址")
    private String bankAddress;

    @ApiModelProperty(value = "支付宝账号")
    private String alipayCode;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal extractPrice;

    private String mark;

    private BigDecimal balance;

    @ApiModelProperty(value = "无效原因")
    private String failMsg;

    private Integer failTime;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    @ApiModelProperty(value = "-1 未通过 0 审核中 1 已提现")
    private Integer status;

    @ApiModelProperty(value = "微信号")
    private String wechat;

}