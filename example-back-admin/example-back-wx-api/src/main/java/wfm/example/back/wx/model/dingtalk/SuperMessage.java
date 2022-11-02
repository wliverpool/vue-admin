package wfm.example.back.wx.model.dingtalk;

/**
 * 钉钉消息超类
 *
 * @author 吴福明
 */
public class SuperMessage {

    protected SuperMessage(String msgtype) {
        this.msgtype = msgtype;
    }

    private String msgtype;

    public String getMsgtype() {
        return msgtype;
    }

}
