package wfm.example.back.wx.model.dingtalk;


import com.alibaba.fastjson.JSONObject;
import wfm.example.back.wx.constant.dingtalk.MessageType;

/**
 * 钉钉图片消息
 *
 * @author 吴福明
 */
public class ImageMessage extends SuperMessage{

    private JSONObject image = new JSONObject();

    /**
     * 钉钉图片消息
     *
     * @param media_id 媒体文件 media_id。可以通过上传媒体文件接口获取。建议宽600像素 x 400像素，宽高比3 : 2。
     */
    public ImageMessage(String media_id) {
        super(MessageType.IMAGE);
        this.image.put("media_id", media_id);
    }

    public JSONObject getImage() {
        return this.image;
    }

}
