package co.yixiang.modules.activity.web.dto;

import co.yixiang.modules.activity.web.vo.YxStoreSeckillQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductReplyQueryVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 秒杀产品表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-12-17
 */
@Data
@Builder
public class StoreSeckillDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private YxStoreProductReplyQueryVo reply;

    private Integer replyCount;

    private YxStoreSeckillQueryVo storeInfo;

    @Builder.Default
    private Boolean userCollect = false;



}