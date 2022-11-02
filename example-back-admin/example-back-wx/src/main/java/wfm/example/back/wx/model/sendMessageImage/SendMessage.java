package wfm.example.back.wx.model.sendMessageImage;

public class SendMessage {

    /**
     * 消息接收用户的userid
     */
    private String toUserId;

    /**
     * 消息类型，text
     */
    private String msgType;

    private ConTent text;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public ConTent getText() {
        return text;
    }

    public void setText(ConTent text) {
        this.text = text;
    }

}
