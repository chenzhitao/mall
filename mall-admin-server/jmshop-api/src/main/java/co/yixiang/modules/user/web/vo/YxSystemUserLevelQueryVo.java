package co.yixiang.modules.user.web.vo;

import co.yixiang.modules.user.web.dto.TaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 设置用户等级表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-12-06
 */
@Data
@ApiModel(value = "YxSystemUserLevelQueryVo对象", description = "设置用户等级表查询参数")
public class YxSystemUserLevelQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "会员名称")
    private String name;

    @ApiModelProperty(value = "购买金额")
    private BigDecimal money;

    @ApiModelProperty(value = "有效时间")
    private Integer validDate;

    @ApiModelProperty(value = "会员等级")
    private Integer grade;

    @ApiModelProperty(value = "享受折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "会员卡背景")
    private String image;

    @ApiModelProperty(value = "会员图标")
    private String icon;

    @ApiModelProperty(value = "说明")
    private String explain;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    private TaskDTO taskList;

    private Boolean isClear;

    private Integer isForever;

    /*是否购买,1=购买,0=不购买*/
    private Integer isPay;

    /*是否显示 1=显示,0=隐藏*/
    private Integer isShow;

    /*是否删除.1=删除,0=未删除*/
    private Integer isDel;



}
