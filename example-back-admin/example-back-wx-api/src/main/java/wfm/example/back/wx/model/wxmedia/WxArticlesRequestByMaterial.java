package wfm.example.back.wx.model.wxmedia;

/**
 * 上传图文消息素材id
 * @author 吴福明
 *
 */
public class WxArticlesRequestByMaterial {

    private String mediaId;


    public String getMediaId() {
        return mediaId;
    }


    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }


    @Override
    public String toString() {
        return "WxArticlesRequestByMaterial [mediaId=" + mediaId + "]";
    }

}
