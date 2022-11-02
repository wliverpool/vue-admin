package wfm.example.back.wx.model.qywx;

public class UpdateFixSourceEntity {

    /**
     * 图文消息，一个图文消息支持1到10个图文
     */
    private  UpdateFixSourceArticles[] articles;

    public UpdateFixSourceArticles[] getArticles() {
        return articles;
    }

    public void setArticles(UpdateFixSourceArticles[] articles) {
        this.articles = articles;
    }

}
