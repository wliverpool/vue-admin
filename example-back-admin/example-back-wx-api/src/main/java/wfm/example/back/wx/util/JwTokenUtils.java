package wfm.example.back.wx.util;

import net.sf.json.JSONObject;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.model.AccessToken;
import wfm.example.back.wx.service.WeiXinReqService;

/**
 * 微信--token信息
 *
 * @author 吴福明
 *
 */
public class JwTokenUtils {

    private static AccessToken atoken = null;

    /**
     * 获取权限令牌信息
     * @param appid
     * @param appscret
     * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
     * @throws WexinReqException
     */
    public static String getAccessToken(String appid, String appscret) throws WexinReqException {
        String newAccessToken = "";
        atoken = new AccessToken();
        atoken.setAppid(appid);
        atoken.setSecret(appscret);
        JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(atoken);
        // 正常返回
        newAccessToken = result.getString("access_token");;
        return newAccessToken;
    }


    public static void main(String[] args){

        try {
            String s = JwTokenUtils.getAccessToken("wx00737224cb9dbc7d","b9479ebdb58d1c6b6efd4171ebe718b5");
            System.out.println(s);
        } catch (WexinReqException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
