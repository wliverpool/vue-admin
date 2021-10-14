package wfm.example.back.message.impl;

import lombok.extern.slf4j.Slf4j;
import wfm.example.common.message.ISendMsgHandle;

/**
 * 通过微信发送消息
 * @author 吴福明
 */

@Slf4j
public class WxSendMsgHandle implements ISendMsgHandle {

    @Override
    public void sendMsg(String es_receiver, String es_title, String es_content) {
        // TODO Auto-generated method stub
        log.info("发微信消息模板");
    }

}
