package co.yixiang.modules.user.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 用户表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxUserQueryParam对象", description="用户表查询参数")
public class YxUserQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户id")
    private Integer uid;

    @NotBlank(message = "用户申请会员级别 ")
    private Integer applyLevel;

    @NotBlank(message = "用户申请会员材料 ")
    private String certInfo;

}
