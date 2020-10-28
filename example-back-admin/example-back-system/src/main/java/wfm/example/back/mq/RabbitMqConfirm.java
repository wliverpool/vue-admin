package wfm.example.back.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * 消息发送到RabbitMQ交换器后接收消息发送结果ack回调
 * @author 吴福明
 */

@Slf4j
public class RabbitMqConfirm implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("Mq Confirm Info:CorrelationData:{},is success:{},s:{}",null!=correlationData?correlationData.toString():"",ack,cause);

    }

}
