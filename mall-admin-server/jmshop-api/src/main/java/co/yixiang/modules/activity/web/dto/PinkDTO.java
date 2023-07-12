package co.yixiang.modules.activity.web.dto;

import io.swagger.annotations.ApiModelProperty;
import co.yixiang.serializer.DoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName PinkDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/19
 **/
@Data
public class PinkDTO implements Serializable {

    @ApiModelProperty(value = "拼团ID")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Long uid;

    @ApiModelProperty(value = "拼图总人数")
    private Integer people;

    @JsonSerialize(using = DoubleSerializer.class)
    @ApiModelProperty(value = "拼团产品单价")
    private Double price;

    @ApiModelProperty(value = "拼图结束时间")
    private Date stopTime;

    @ApiModelProperty(value = "拼团用户昵称")
    private String nickname;

    @ApiModelProperty(value = "拼团用户头像")
    private String avatar;

    @ApiModelProperty(value = "参与的拼团的id集合")
    private String count;

    @ApiModelProperty(value = "拼图时效：小时")
    private String h;

    @ApiModelProperty(value = "拼图时效：分钟")
    private String i;

    @ApiModelProperty(value = "拼图时效：秒")
    private String s;


}
