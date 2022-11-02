package wfm.example.back.wx.model.qywx;

public class MpnewEntity {

    /**
     * 图文消息，一个图文消息支持1到10个图文
     */
    private MpnewArticles[] articles;

    public MpnewArticles[] getArticles() {
        return articles;
    }

    public void setArticles(MpnewArticles[] articles) {
        this.articles = articles;
    }

}
