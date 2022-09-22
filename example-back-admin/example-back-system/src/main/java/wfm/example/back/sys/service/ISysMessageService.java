package wfm.example.back.sys.service;

import wfm.example.back.sys.model.SysMessage;

import java.util.Map;

/**
 * 消息服务接口
 * @author: 吴福明
 */
public interface ISysMessageService extends IBaseService<SysMessage> {

    /**
     * @param msgType 消息类型 1短信 2邮件 3微信
     * @param templateCode    消息模板码
     * @param map     消息参数
     * @param sentTo  接收消息方
     */
    boolean sendMessage(String msgType, String templateCode, Map<String, String> map, String sentTo);

    /**
     * 批量发送状态为未发送或者发送失败的消息
     */
    void sendMessageBatch();

}
