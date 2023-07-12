package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author yushen
* @date 2023-06-08
*/
@Data
public class YxVideoProductDto implements Serializable {

    /** id */
    private Integer id;

    /** 视频ID */
    private Integer videoId;

    /** 商品ID */
    private Integer productId;

    /** 商品名称 */
    private String productName;

    /** 排序编号 */
    private Integer sortNo;

    /** 添加时间 */
    private Integer addTime;
}