package wfm.example.back.wx.model.wxmedia;


import wfm.example.back.wx.model.wxsendmsg.WxArticle;

/**
 * 修改消息素材
 * @author 吴福明
 *
 */
public class WxUpdateArticle {

    private String media_id;

    private int index;

    private WxArticle articles = new WxArticle();


    public String getMedia_id() {
        return media_id;
    }



    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }



    public int getIndex() {
        return index;
    }



    public void setIndex(int index) {
        this.index = index;
    }



    public WxArticle getArticles() {
        return articles;
    }



    public void setArticles(WxArticle articles) {
        this.articles = articles;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WxUpdateArticle [media_id=");
        builder.append(media_id);
        builder.append(", index=");
        builder.append(index);
        builder.append(", articles=");
        builder.append(articles);
        builder.append("]");
        return builder.toString();
    }

}
