package wfm.example.back.sys.message.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import wfm.example.back.common.config.SystemConfig;
import wfm.example.back.common.message.ISendMsgHandle;
import wfm.example.back.common.util.DySmsHelperUtils;

import static wfm.example.back.common.enums.DySmsEnum.SMS_MESSAGE_TEMPLATE_CODE;

/**
 * 通过短信发送消息
 * @author 吴福明
 */

@Slf4j
public class SmsSendMsgHandle implements ISendMsgHandle {

    @Override
    public void sendMsg(String es_receiver, String es_title, String es_content) {
        String smsServer = SystemConfig.getApplicationContext().getEnvironment().getProperty("system.sms.server");
        Integer smsPort = SystemConfig.getApplicationContext().getEnvironment().getProperty("system.sms.port",Integer.class);
        String cxPrivateKey = SystemConfig.getApplicationContext().getEnvironment().getProperty("system.sms.private-key");
        if (StringUtils.isBlank(es_receiver)) {
            log.error("标题为:" + es_title + "的消息,通过短信发送时,缺少接收者");
            return;
        }
        DySmsHelperUtils.sendSms(smsServer, smsPort, es_content, es_receiver, SMS_MESSAGE_TEMPLATE_CODE.getTemplateCode(),cxPrivateKey);
        log.info("标题为:" + es_title + "的消息,通过短信发送成功");
    }

}