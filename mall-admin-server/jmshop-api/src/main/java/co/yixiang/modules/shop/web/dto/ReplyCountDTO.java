package co.yixiang.modules.shop.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ReplyCount
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/4
 **/
@Data
public class ReplyCountDTO implements Serializable {
    private Integer sumCount;
    private Integer goodCount;
    private Integer inCount;
    private Integer poorCount;
    private String replyChance;
    private String replySstar;

}
