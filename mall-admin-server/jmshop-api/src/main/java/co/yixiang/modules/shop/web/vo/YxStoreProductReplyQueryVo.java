package co.yixiang.modules.shop.web.vo;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评论表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-23
 */
@Data
@ApiModel(value = "YxStoreProductReplyQueryVo对象", description = "评论表查询参数")
public class YxStoreProductReplyQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论ID")
    private Integer id;


    @ApiModelProperty(value = "产品id")
    private Integer productId;

    @ApiModelProperty(value = "某种商品类型(普通商品、秒杀商品）")
    private String replyType;

    @ApiModelProperty(value = "商品分数")
    private Integer productScore;

    @ApiModelProperty(value = "服务分数")
    private Integer serviceScore;

    @ApiModelProperty(value = "评论内容")
    private String comment;

    @ApiModelProperty(value = "评论图片")
    private String[] pics;

    private String pictures;

    private String[] picturesArr;

    public String[] getPicturesArr() {
        if(StrUtil.isNotEmpty(pictures)){
            return pictures.split(",");
        }else{
            return new String[]{};
        }

    }

    @ApiModelProperty(value = "评论时间")
    private Integer addTime;

    @ApiModelProperty(value = "管理员回复内容")
    private String merchantReplyContent;

    @ApiModelProperty(value = "管理员回复时间")
    private Integer merchantReplyTime;


    private String star;

    private String nickname;

    private String avatar;

    private String suk;



    @JsonIgnore
    private String cartInfo;



}