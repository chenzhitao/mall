package co.yixiang.modules.quartz.task;

import co.yixiang.modules.monitor.service.VisitsService;
import co.yixiang.modules.shop.service.YxStoreOrderService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Zheng Jie
 * @date 2018-12-25
 */
@Slf4j
@Component
public class AutoOfflineProduct {

    private final YxStoreProductService yxStoreProductService;

    public AutoOfflineProduct(YxStoreProductService yxStoreProductService) {
        this.yxStoreProductService = yxStoreProductService;
    }

    public void run(){
        yxStoreProductService.autoOfflineProduct();
    }
}
