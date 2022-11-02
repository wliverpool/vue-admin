package wfm.example.back.wx.util;

import net.sf.json.JSONObject;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.model.qrcode.Getticket;
import wfm.example.back.wx.model.qrcode.GetticketRtn;
import wfm.example.back.wx.model.qrcode.QrcodeInfo;
import wfm.example.back.wx.model.qrcode.QrcodeRtnInfo;
import wfm.example.back.wx.service.WeiXinReqService;

/**
 * 微信卡券 - 卡券投放
 * @author 吴福明
 *
 */
public class JwQrcodeUtils {

    /**
     * 创建二维码
     * @throws WexinReqException
     */
    public static QrcodeRtnInfo doAddQrcode(String accesstoken, QrcodeInfo qrcodeInfo) throws WexinReqException {
        if (accesstoken != null) {
            qrcodeInfo.setAccess_token(accesstoken);
            JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(qrcodeInfo);
            QrcodeRtnInfo qrcodeRtnInfo = (QrcodeRtnInfo) JSONObject.toBean(result, QrcodeRtnInfo.class);
            return qrcodeRtnInfo;
        }
        return null;
    }

    /**
     * 获取api_ticket
     */
    public static GetticketRtn doGetticket(String accesstoken)throws WexinReqException {
        if (accesstoken != null) {
            Getticket gk = new Getticket();
            gk.setAccess_token(accesstoken);
            JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(gk);
            GetticketRtn getticketRtn = (GetticketRtn)JSONObject.toBean(result, GetticketRtn.class);
            return getticketRtn;
        }
        return null;
    }

}
