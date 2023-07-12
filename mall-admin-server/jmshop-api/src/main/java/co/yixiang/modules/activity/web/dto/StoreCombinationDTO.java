package co.yixiang.modules.activity.web.dto;

import co.yixiang.modules.activity.web.vo.YxStoreCombinationQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreProductReplyQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 拼团产品表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-11-19
 */
@Data
public class StoreCombinationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<PinkDTO> pink;

    private List<Integer> pindAll;

    private List<String> pinkOkList;

    private Integer pinkOkSum;

    private YxStoreProductReplyQueryVo reply;

    private Integer replyCount = 0;

    private String replyChance;
    private YxStoreCombinationQueryVo storeInfo;

    private Boolean userCollect = false;



}