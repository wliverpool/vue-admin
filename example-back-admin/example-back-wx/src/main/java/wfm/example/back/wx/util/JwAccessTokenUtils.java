package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.constant.qywx.JwParames;
import wfm.example.back.wx.model.qywx.AccessToken;

/**
 * 企业微信--access token信息
 *
 * @author 吴福明
 *
 */
@Slf4j
public class JwAccessTokenUtils {

    /**
     * 获取access_token的接口地址（GET）
      */
    public final static String access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CorpID&corpsecret=SECRET";

    /**
     * 获取access_token
     *
     * @param corpID 企业Id
     * @param secret 管理组的凭证密钥，每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret
     * @return
     */
    public static AccessToken getAccessToken(String corpID, String secret) {
        AccessToken accessToken = null;
        String requestUrl = access_token_url.replace("CorpID", corpID).replace("SECRET", secret);
        JSONObject jsonObject = HttpUtils.sendGet(requestUrl);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setAccesstoken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
                log.info("[ACCESSTOKEN]", "获取ACCESSTOKEN成功:{}", new Object[]{accessToken});
            } catch (Exception e) {
                accessToken = null;
                // 获取token失败
                int errcode = jsonObject.getIntValue("errcode");
                String errmsg = jsonObject.getString("errmsg");
                log.info("[ACCESSTOKEN]", "获取ACCESSTOKEN失败 errcode:{} errmsg:{}", new Object[]{errcode,errmsg});
            }
        }
        return accessToken;
    }


    public static void main(String[] args){
        try {
            AccessToken s = JwAccessTokenUtils.getAccessToken(JwParames.corpId,JwParames.secret);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
