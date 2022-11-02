package wfm.example.back.wx.model.kfaccount;

import java.util.List;

/**
 * 取多媒体文件
 *
 * @author 吴福明
 *
 */
public class MsgNews {

    private List<MsgArticles> articles;

    public List<MsgArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<MsgArticles> articles) {
        this.articles = articles;
    }

}