package wfm.example.back.wx.model.wxmedia;

import wfm.example.back.wx.model.wxsendmsg.WxArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * 获得媒体信息
 * @author 吴福明
 *
 */
public class WxArticlesRespponseByMaterial {

    List<WxArticle> news_item = new ArrayList<WxArticle>();



    public List<WxArticle> getNews_item() {
        return news_item;
    }



    public void setNews_item(List<WxArticle> news_item) {
        this.news_item = news_item;
    }



    @Override
    public String toString() {
        return "WxArticlesRequest [news_item=" + news_item + "]";
    }

}