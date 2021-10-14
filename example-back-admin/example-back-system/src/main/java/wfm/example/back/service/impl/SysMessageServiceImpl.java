package wfm.example.back.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wfm.example.back.mapper.SysMessageMapper;
import wfm.example.back.mapper.SysMessageTemplateMapper;
import wfm.example.common.message.ISendMsgHandle;
import wfm.example.back.model.SysMessage;
import wfm.example.back.model.SysMessageTemplate;
import wfm.example.back.service.ISysMessageService;
import wfm.example.common.enums.SendMsgStatusEnum;
import wfm.example.common.enums.SendMsgTypeEnum;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 消息服务实现类
 * @author: 吴福明
 */
@Service
@Slf4j
public class SysMessageServiceImpl extends BaseServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

    @Value("${spring.rabbitmq.admin-message-notify.exchange-name}")
    private String adminMessageNotifyExchangeName;
    @Value("${spring.rabbitmq.admin-message-notify.routing-key}")
    private String adminMessageNotifyRoutingKey;

    @Resource
    private SysMessageTemplateMapper sysMessageTemplateMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public boolean sendMessage(String msgType, String templateCode, Map<String, String> map, String sentTo) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateMapper.selectByCode(templateCode);
        SysMessage sysMessage = new SysMessage();
        if (sysSmsTemplates.size() > 0) {
            SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
            sysMessage.setEsType(msgType);
            sysMessage.setEsReceiver(sentTo);
            //模板标题
            String title = sysSmsTemplate.getTemplateName();
            //模板内容
            String content = sysSmsTemplate.getTemplateContent();
            if(map!=null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String str = "${" + entry.getKey() + "}";
                    title = title.replace(str, entry.getValue());
                    content = content.replace(str, entry.getValue());
                }
            }
            sysMessage.setEsTitle(title);
            sysMessage.setEsContent(content);
            sysMessage.setEsParam(JSONObject.toJSONString(map));
            sysMessage.setEsSendTime(new Date());
            sysMessage.setEsSendStatus(SendMsgStatusEnum.WAIT.getCode());
            sysMessage.setEsSendNum(0);
            if(save(sysMessage)) {
                String messageId = UUID.randomUUID().toString();
                CorrelationData correlationData = new CorrelationData("SendMessage-" + messageId);
                rabbitTemplate.convertAndSend(adminMessageNotifyExchangeName,adminMessageNotifyRoutingKey,messageId,correlationData);
                return true;
            }
        }
        return false;
    }

    @Override
    public void sendMessageBatch() {
        QueryWrapper<SysMessage> queryWrapper = new QueryWrapper<SysMessage>();
        queryWrapper.eq("es_send_status", SendMsgStatusEnum.WAIT.getCode())
                .or(i -> i.eq("es_send_status", SendMsgStatusEnum.FAIL.getCode()).lt("es_send_num", 6));
        List<SysMessage> sysMessages = list(queryWrapper);
        //System.out.println(sysMessages);
        // 2.根据不同的类型走不通的发送实现类
        for (SysMessage sysMessage : sysMessages) {
            ISendMsgHandle sendMsgHandle = null;
            try {
                if (sysMessage.getEsType().equals(SendMsgTypeEnum.EMAIL.getType())) {
                    sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.EMAIL.getImplClass()).newInstance();
                } else if (sysMessage.getEsType().equals(SendMsgTypeEnum.SMS.getType())) {
                    sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.SMS.getImplClass()).newInstance();
                } else if (sysMessage.getEsType().equals(SendMsgTypeEnum.WX.getType())) {
                    sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.WX.getImplClass()).newInstance();
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
            Integer sendNum = sysMessage.getEsSendNum();
            try {
                sendMsgHandle.sendMsg(sysMessage.getEsReceiver(), sysMessage.getEsTitle(),
                        sysMessage.getEsContent().toString());
                // 发送消息成功
                sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
            } catch (Exception e) {
                // 发送消息出现异常
                sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
            }
            sysMessage.setEsSendNum(++sendNum);
            // 发送结果回写到数据库
            updateById(sysMessage);
        }
    }

}
