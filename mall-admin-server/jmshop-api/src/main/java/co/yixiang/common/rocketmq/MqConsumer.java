package co.yixiang.common.rocketmq;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.modules.order.entity.YxStoreOrder;
import co.yixiang.modules.order.service.YxStoreOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName 消费者
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/1/1
 **/
//@Component
//@RocketMQMessageListener(
//        topic = "jmshop-topic",
//        consumerGroup = "jmshop-group",
//        selectorExpression = "*"
//)
@Slf4j
@AllArgsConstructor
public class MqConsumer implements RocketMQListener<String> {

    private final YxStoreOrderService storeOrderService;

    @Override
    public void onMessage(String msg) {
        log.info("系统开始处理延时任务---订单超时未付款---订单id:" + msg);

        Integer id = Integer.valueOf(msg);

        YxStoreOrder order = storeOrderService.getOne(new QueryWrapper<YxStoreOrder>()
                .eq("id", id).eq("is_del",0).eq("paid",0));

        if(ObjectUtil.isNull(order)) {
            return;
        }

        storeOrderService.cancelOrderByTask(id);

        log.info("=====处理成功======");

    }
}
