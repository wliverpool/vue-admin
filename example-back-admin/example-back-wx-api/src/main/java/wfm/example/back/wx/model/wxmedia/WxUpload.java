package wfm.example.back.wx.model.wxmedia;

/**
 * 微信多媒体文件
 * @author 吴福明
 *
 */
public class WxUpload {

    private String type;

    private String media_id;

    private String created_at;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
