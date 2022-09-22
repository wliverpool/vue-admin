package wfm.example.back.common.message;

/**
 * 发送消息处理器
 * @author 吴福明
 */
public interface ISendMsgHandle {

    /**
     *
     * @param es_receiver  接收者
     * @param es_title    标题
     * @param es_content   内容
     */
    void sendMsg(String es_receiver, String es_title, String es_content);
}
