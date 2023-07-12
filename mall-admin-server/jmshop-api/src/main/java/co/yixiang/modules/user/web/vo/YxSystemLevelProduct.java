package co.yixiang.modules.user.web.vo;

import co.yixiang.modules.shop.entity.YxStoreDiscount;
import co.yixiang.modules.user.entity.YxSystemUserLevel;
import co.yixiang.modules.user.web.dto.TaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : godly.strong
 * mail : huangjunquan1109@163.com
 * @since : 2023/3/15 10:13
 * describe ：新增-商品会员
 */
@Data
public class YxSystemLevelProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员等级
     */
    private YxSystemUserLevel yxSystemUserLevel;

    /**
     * 原价
     */
    private Double price;

    /**
     * 折扣价
     */
    private Double discountPrice;
}
