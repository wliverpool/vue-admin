package wfm.example.back.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wfm.example.back.mq.RabbitMqConfirm;
import wfm.example.back.mq.SendMessageListener;

/**
 * rabbit mq配置文件
 * @author 吴福明
 */
@Configuration
@Slf4j
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.addresses}")
    private String rabbitmqAddresses;
    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;
    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;
    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitmqVirtualHost;
    @Value("${spring.rabbitmq.admin-message-notify.queue-name}")
    private String adminMessageNotifyQueueName;
    @Value("${spring.rabbitmq.admin-message-notify.exchange-name}")
    private String adminMessageNotifyExchangeName;
    @Value("${spring.rabbitmq.admin-message-notify.routing-key}")
    private String adminMessageNotifyRoutingKey;
    @Value("${spring.rabbitmq.admin-message-notify.max-consumers}")
    private int adminMessageNotifyMaxConsumers;
    @Value("${spring.rabbitmq.admin-message-notify.concurrent-consumers}")
    private int adminMessageNotifyConcurrentConsumers;
    @Value("${spring.rabbitmq.admin-message-notify.prefetch}")
    private int adminMessageNotifyPrefetch;

    /**
     * rabbit mq 连接 factory
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitmqAddresses);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        connectionFactory.setVirtualHost(rabbitmqVirtualHost);
        //生产者的channel设置成confirm模式
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean(name = "rabbitTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        //ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调
        rabbitTemplate.setConfirmCallback(new RabbitMqConfirm());
        // ReturnCallback接口用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调。
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("Pay 确认后回调return--message:"+new String(message.getBody())+",replyCode:"+replyCode+",replyText:"
                        +replyText+",exchange:"+exchange+",routingKey:"+routingKey);
            }
        });
        return rabbitTemplate;
    }

    @Bean(name = "adminMessageNotifyQueue")
    public Queue adminMessageNotifyQueue(){
        //配置支付交易成功的消息队列
        return new Queue(adminMessageNotifyQueueName);
    }

    @Bean
    public TopicExchange payTradeNotifyExchange(){
        return new TopicExchange(adminMessageNotifyExchangeName);
    }

    @Bean
    public Binding bindingExchange(@Qualifier("adminMessageNotifyQueue") Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with(adminMessageNotifyRoutingKey);
    }

    /**
     * rabbitmq消费者监听容器
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        Queue payChannelTradeNotifyQueue = new Queue(adminMessageNotifyQueueName,true,false,false,null);
        container.setQueues(payChannelTradeNotifyQueue);
        container.setExposeListenerChannel(true);
        //消费端最大并发数
        container.setMaxConcurrentConsumers(adminMessageNotifyMaxConsumers);
        //消费端最小并发数
        container.setConcurrentConsumers(adminMessageNotifyConcurrentConsumers);
        //手动应答
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //一次处理的消息数量
        container.setPrefetchCount(adminMessageNotifyPrefetch);
        container.setMessageListener(new SendMessageListener());
        return container;
    }


}
