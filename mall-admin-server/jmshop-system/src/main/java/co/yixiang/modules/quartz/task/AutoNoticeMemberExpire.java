package co.yixiang.modules.quartz.task;

import co.yixiang.modules.activity.service.YxStoreCouponUserService;
import co.yixiang.modules.shop.service.YxUserLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Zheng Jie
 * @date 2018-12-25
 */
@Slf4j
@Component
public class AutoNoticeMemberExpire {

    private final YxUserLevelService yxUserLevelService;

    public AutoNoticeMemberExpire(YxUserLevelService yxUserLevelService) {
        this.yxUserLevelService = yxUserLevelService;
    }

    public void run(){
        yxUserLevelService.autoNoticeExpireUser();
    }
}
