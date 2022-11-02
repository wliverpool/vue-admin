package wfm.example.back.wx.model.sendMessageImage;

import java.util.List;

public class SendMessageImageText {

    /**
     * 消息接收用户的userid
     */
    private String toUserId;

    /**
     * 消息类型，image-text
     */
    private String msgType;

    /**
     * 图文消息子消息项集合，单条消息最多6个子项，否则会发送失败
     */
    private List<Articles> articles;

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
    public List<Articles> getArticles() {
        return articles;
    }
    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }


}
