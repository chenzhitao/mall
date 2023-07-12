package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 门店店员表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-03-23
 */
@Data
@ApiModel(value = "YxSystemStoreStaffQueryVo对象", description = "门店店员表查询参数")
public class YxSystemStoreStaffQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "微信用户id")
    private Integer uid;

    private String nickname;

    @ApiModelProperty(value = "店员头像")
    private String avatar;

    @ApiModelProperty(value = "门店id")
    private Integer storeId;

    private String storeName;

    @ApiModelProperty(value = "店员名称")
    private String staffName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "核销开关")
    private Integer verifyStatus;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

}