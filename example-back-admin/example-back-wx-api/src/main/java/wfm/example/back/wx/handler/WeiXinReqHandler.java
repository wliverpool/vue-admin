package wfm.example.back.wx.handler;

import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 获取微信接口的信息
 * @author 吴福明
 *
 */
public interface WeiXinReqHandler {

    public String doRequest(WeixinReqParam weixinReqParam) throws WexinReqException;

}