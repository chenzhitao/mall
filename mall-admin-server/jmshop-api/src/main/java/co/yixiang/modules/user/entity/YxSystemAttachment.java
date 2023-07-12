package co.yixiang.modules.user.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 附件管理表
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxSystemAttachment对象", description = "附件管理表")
public class YxSystemAttachment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "att_id", type = IdType.AUTO)
    private Integer attId;

    @ApiModelProperty(value = "附件名称")
    private String name;

    @ApiModelProperty(value = "附件路径")
    private String attDir;

    @ApiModelProperty(value = "压缩图片路径")
    private String sattDir;

    @ApiModelProperty(value = "附件大小")
    private String attSize;

    @ApiModelProperty(value = "附件类型")
    private String attType;

    @ApiModelProperty(value = "分类ID0编辑器,1产品图片,2拼团图片,3砍价图片,4秒杀图片,5文章图片,6组合数据图")
    private Integer pid;

    @ApiModelProperty(value = "上传时间")
    private Integer time;

    @ApiModelProperty(value = "图片上传类型 1本地 2七牛云 3OSS 4COS ")
    private Integer imageType;

    @ApiModelProperty(value = "图片上传模块类型 1 后台上传 2 用户生成")
    private Integer moduleType;

    private Integer uid;

    private String inviteCode;

}
