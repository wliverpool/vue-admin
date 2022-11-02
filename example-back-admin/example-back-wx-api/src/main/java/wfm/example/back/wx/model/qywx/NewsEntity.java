package wfm.example.back.wx.model.qywx;

public class NewsEntity {

    /**
     * 定义数组图文消息，一个图文消息支持1到8条图文
     */
    private NewsArticle[] articles;

    public NewsArticle[] getArticles() {
        return articles;
    }

    public void setArticles(NewsArticle[] articles) {
        this.articles = articles;
    }

}
