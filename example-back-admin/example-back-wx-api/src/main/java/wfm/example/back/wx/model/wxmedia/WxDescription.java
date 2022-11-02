package wfm.example.back.wx.model.wxmedia;

/**
 * 图文消息
 * @author 吴福明
 *
 */
public class WxDescription {

    /** 视频素材的标题*/
    private String title;
    /** 视频素材的描述 */
    private String introduction;


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getIntroduction() {
        return introduction;
    }


    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }


    @Override
    public String toString() {
        return "WxDescription [title=" + title + ", introduction=" + introduction + "]";
    }



}