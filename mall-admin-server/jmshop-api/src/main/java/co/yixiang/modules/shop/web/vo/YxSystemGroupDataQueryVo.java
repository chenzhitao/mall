package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 组合数据详情表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-19
 */
@Data
@ApiModel(value = "YxSystemGroupDataQueryVo对象", description = "组合数据详情表查询参数")
public class YxSystemGroupDataQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组合数据详情ID")
    private Integer id;

    @ApiModelProperty(value = "对应的数据名称")
    private String groupName;

    @ApiModelProperty(value = "数据组对应的数据值（json数据）")
    private String value;

    private Map<String, Object> map;

    @ApiModelProperty(value = "添加数据时间")
    private Integer addTime;

    @ApiModelProperty(value = "数据排序")
    private Integer sort;

    @ApiModelProperty(value = "状态（1：开启；2：关闭；）")
    private Boolean status;

}
