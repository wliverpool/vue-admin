package wfm.example.back.wx.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.model.user.UserInfoListGet;
import wfm.example.back.wx.model.wxuser.Wxuser;
import wfm.example.back.wx.service.WeiXinReqService;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信--用户
 *
 * @author 吴福明
 *
 */
@Slf4j
public class JwUserUtils {

    /**
     * 获取用户基本信息（包括UnionID机制）
     */
    private static String GET_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";


    /**
     * 根据user_openid 获取关注用户的基本信息
     *
     * @param accesstoken
     * @param user_openid
     * @return
     * @throws WexinReqException
     */
    public static Wxuser getWxuser(String accesstoken, String user_openid) throws WexinReqException {
        if (accesstoken != null) {
            String requestUrl = GET_USER_URL.replace("ACCESS_TOKEN", accesstoken).replace("OPENID", user_openid);
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", null);
            log.info(result.toString());
            // 正常返回
            Wxuser wxuser = null;
            Object error = result.get("errcode");
            wxuser = (Wxuser) JSONObject.toBean(result, Wxuser.class);
            return wxuser;
        }
        return null;
    }

    /**
     * 获取所有关注用户信息信息
     *
     * @return
     * @throws WexinReqException
     */
    public static List<Wxuser> getAllWxuser(String accesstoken, String next_openid) throws WexinReqException {
        if (accesstoken != null) {
            UserInfoListGet userInfoListGet = new UserInfoListGet();
            userInfoListGet.setAccess_token(accesstoken);
            userInfoListGet.setNext_openid(next_openid);
            JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(userInfoListGet);
            Object error = result.get("errcode");
            List<Wxuser> lstUser = null;
            Wxuser mxuser = null;
            int total = result.getInt("total");
            int count = result.getInt("count");
            String strNextOpenId = result.getString("next_openid");
            JSONObject data = result.getJSONObject("data");
            lstUser = new ArrayList<Wxuser>(total);
            if (count > 0) {
                JSONArray lstOpenid = data.getJSONArray("openid");
                int iSize = lstOpenid.size();
                for (int i = 0; i < iSize; i++) {
                    String openId = lstOpenid.getString(i);
                    mxuser = getWxuser(accesstoken, openId);
                    lstUser.add(mxuser);
                }
                if (strNextOpenId != null) {
                    lstUser.addAll(getAllWxuser(accesstoken, strNextOpenId));
                }
            }
            return lstUser;
        }
        return null;
    }

}
