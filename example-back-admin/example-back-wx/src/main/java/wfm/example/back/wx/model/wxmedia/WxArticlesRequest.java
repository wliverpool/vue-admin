package wfm.example.back.wx.model.wxmedia;

import wfm.example.back.wx.model.wxsendmsg.WxArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传图文消息素材
 * @author 吴福明
 *
 */
public class WxArticlesRequest {


    List<WxArticle> articles = new ArrayList<WxArticle>();

    public List<WxArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<WxArticle> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "WxArticlesRequest [articles=" + articles + "]";
    }

}