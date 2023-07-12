package co.yixiang.modules.order.web.dto;

import co.yixiang.modules.shop.web.vo.YxStoreCartQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CacheDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
public class CacheDTO implements Serializable {
    private List<YxStoreCartQueryVo> cartInfo;
    private PriceGroupDTO priceGroup;
    private OtherDTO other;
}
