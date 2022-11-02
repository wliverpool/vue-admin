package wfm.example.back.wx.model.sendMessageImage;

import java.util.List;

public class SendMessageImageTextMore {

    /**
     * 消息类型，image
     */
    private String msgType;

    private List<Articles> articles;

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
