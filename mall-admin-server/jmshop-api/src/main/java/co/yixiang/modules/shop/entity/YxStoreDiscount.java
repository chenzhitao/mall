package co.yixiang.modules.shop.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 用户等级记录表
 * </p>
 *
 * @author hupeng
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxStoreDiscount对象", description = "商品等级记录表")
public class YxStoreDiscount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // 折扣率ID
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联产品ID")
    private Integer productId;

    @ApiModelProperty(value = "会员等级")
    private Integer grade;

    @ApiModelProperty(value = "享受折扣")
    private Integer discount;

    @ApiModelProperty(value = "砍价活动名称")
    private String title;

    @ApiModelProperty(value = "砍价开启时间")
    private Integer startTime;

    @ApiModelProperty(value = "砍价结束时间")
    private Integer stopTime;

    @ApiModelProperty(value = "砍价状态 0(到砍价时间不自动开启)  1(到砍价时间自动开启时间)")
    private Integer status;

    @ApiModelProperty(value = "活动简介")
    private String info;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否删除 0未删除 1删除")
    private Integer isDel;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;

    private String jsonStr;

    private Date startTimeDate;

    private Date endTimeDate;

}
