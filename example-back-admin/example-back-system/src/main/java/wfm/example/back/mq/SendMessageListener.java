package wfm.example.back.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import wfm.example.back.config.SystemConfig;
import wfm.example.back.service.ISysMessageService;

/**
 * 监听支付交易通知mq监听器
 * @author 吴福明
 */

@Slf4j
public class SendMessageListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] body = null;
        String messageId = null;
        try {
            body = message.getBody();
            messageId = new String(body,"UTF-8");
            log.info("接受到发送消息的mq:" + messageId);
            ISysMessageService sysMessageService = SystemConfig.getBean(ISysMessageService.class);
            sysMessageService.sendMessageBatch();
        } catch (Exception e){
            log.error(messageId + "发送消息异常:" + e.getMessage(),e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
