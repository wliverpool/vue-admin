package wfm.example.back.sys.message.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import wfm.example.back.common.config.SystemConfig;
import wfm.example.back.common.message.ISendMsgHandle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送消息
 * @author 吴福明
 */

@Slf4j
public class EmailSendMsgHandle implements ISendMsgHandle {

    @Override
    public void sendMsg(String es_receiver, String es_title, String es_content) {
        String emailFrom = SystemConfig.getApplicationContext().getEnvironment().getProperty("spring.mail.username");
        if (StringUtils.isBlank(es_receiver)) {
            log.error("标题为:" + es_title + "的消息,通过短信发送时,缺少接收者");
            return;
        }
        JavaMailSender mailSender = (JavaMailSender) SystemConfig.getBean(MailSender.class);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            // 设置发送方邮箱地址
            helper.setFrom(emailFrom);
            helper.setTo(es_receiver);
            helper.setSubject(es_title);
            helper.setText(es_content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
        log.error("标题为:" + es_title + "的消息,通过邮件发送成功");
    }
}
