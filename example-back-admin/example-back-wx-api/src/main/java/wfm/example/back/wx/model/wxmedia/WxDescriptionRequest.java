package wfm.example.back.wx.model.wxmedia;

/**
 * 上传图文消息素材
 * @author 吴福明
 *
 */
public class WxDescriptionRequest {

    WxDescription description = new WxDescription();



    public WxDescription getDescription() {
        return description;
    }



    public void setDescription(WxDescription description) {
        this.description = description;
    }



    @Override
    public String toString() {
        return "WxDescriptionRequest [description=" + description + "]";
    }

}
