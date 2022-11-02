package wfm.example.back.wx.model.dingtalk;

import com.alibaba.fastjson.JSONObject;
import wfm.example.back.wx.constant.dingtalk.MessageType;

/**
 * 钉钉文本消息
 *
 * @author 吴福明
 */
public class TextMessage extends SuperMessage {

    private JSONObject text = new JSONObject();

    /**
     * 钉钉文本消息
     *
     * @param content 消息内容
     */
    public TextMessage(String content) {
        super(MessageType.TEXT);
        this.text.put("content", content);
    }

    public JSONObject getText() {
        return this.text;
    }

}
