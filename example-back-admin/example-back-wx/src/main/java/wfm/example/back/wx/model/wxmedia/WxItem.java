package wfm.example.back.wx.model.wxmedia;

import java.util.List;

/**
 * 图文消息图文消息
 * @author 吴福明
 *
 */
public class WxItem {

    /** 媒体id */
    private String media_id;

    /** 图文消息的作者，微信接口返回的可能会是多个，使用list接收 */
    private List<WxNewsArticle> contents;

    /** 文件名称 */
    private String name;
    /** 这篇图文消息素材的最后更新时间 */
    private String update_time;
    /** 图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL */
    private String url;


    public String getMedia_id() {
        return media_id;
    }


    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }


	/*public WxArticle getContent() {
		return content;
	}


	public void setContent(WxArticle content) {
		this.content = content;
	}*/


    public List<WxNewsArticle> getContents() {
        return contents;
    }


    public void setContents(List<WxNewsArticle> contents) {
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUpdate_time() {
        return update_time;
    }


    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WxItem [media_id=");
        builder.append(media_id);
        builder.append(", contents=");
        builder.append(contents);
        builder.append(", name=");
        builder.append(name);
        builder.append(", update_time=");
        builder.append(update_time);
        builder.append(", url=");
        builder.append(url);
        builder.append("]");
        return builder.toString();
    }



}
