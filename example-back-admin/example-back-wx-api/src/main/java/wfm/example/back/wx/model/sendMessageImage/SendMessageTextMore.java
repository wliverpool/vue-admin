package wfm.example.back.wx.model.sendMessageImage;

public class SendMessageTextMore {

    /**
     * 消息类型，text
     */
    private String msgType;

    private ConTextMore text;

    public String getMsgType() {
        return msgType;
    }
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public ConTextMore getText() {
        return text;
    }

    public void setText(ConTextMore text) {
        this.text = text;
    }

}
