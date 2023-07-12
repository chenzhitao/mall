package co.yixiang.modules.activity.web.dto;

import co.yixiang.modules.activity.web.vo.YxStoreCombinationQueryVo;
import co.yixiang.modules.activity.web.vo.YxStorePinkQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PinkInfoDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/20
 **/
@Data
public class PinkInfoDTO implements Serializable {
    private Integer count;
    private String currentPinkOrder;
    private Integer isOk = 0;
    private List<YxStorePinkQueryVo> pinkAll;
    private Integer pinkBool = 0;
    private YxStorePinkQueryVo pinkT;
    private YxStoreCombinationQueryVo storeCombination;
    private String storeCombinationHost;
    private Integer userBool = 0;
    private YxUserQueryVo userInfo;

}
