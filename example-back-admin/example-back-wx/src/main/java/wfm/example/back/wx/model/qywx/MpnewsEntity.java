package wfm.example.back.wx.model.qywx;

public class MpnewsEntity {

    /**
     * 图文消息，一个图文消息支持1到8个图文
     */
    private MpnewsArticles[] articles;

    public MpnewsArticles[] getArticles() {
        return articles;
    }

    public void setArticles(MpnewsArticles[] articles) {
        this.articles = articles;
    }

}
