package co.yixiang.modules.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 门店店员表
 * </p>
 *
 * @author hupeng
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxSystemStoreStaff对象", description = "门店店员表")
public class YxSystemStoreStaff extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "微信用户id")
    private Integer uid;

    @ApiModelProperty(value = "推广元id")
    private Integer spreadUid;

    @ApiModelProperty(value = "推广员头像")
    private String spreadAvatar;

    @ApiModelProperty(value = "推广员昵称")
    private String spreadNickname;

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
