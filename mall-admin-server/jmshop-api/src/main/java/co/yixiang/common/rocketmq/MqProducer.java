package co.yixiang.common.rocketmq;

import co.yixiang.exception.ErrorRequestException;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName 生成者
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/1/1
 **/
//@Component
@AllArgsConstructor
public class MqProducer {
     //注入rocketMQ的模板
    private final RocketMQTemplate rocketMQTemplate;


    /**
     * 发送延时消息10分钟
     *
     * @param topic
     * @param msg
     */
    public void sendMsg(String topic, String msg) {
        DefaultMQProducer defaultMQProducer = rocketMQTemplate.getProducer();
        System.out.println("RocketMQ服务没启动成功");
        Message message = new Message(topic,msg.getBytes());
        message.setDelayTimeLevel(14);

        try {
            defaultMQProducer.send(message);
        } catch (MQClientException e) {
            throw new ErrorRequestException("RocketMQ服务没启动哦");
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
