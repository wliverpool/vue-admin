package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.constant.dingtalk.ApiUrls;
import wfm.example.back.wx.model.dingtalk.ContactUser;

/**
 * 钉钉 Oauth2 接口
 * @author 吴福明
 */
@Slf4j
public class JdtOauth2Utils {

    /**
     * 获取用户token
     *
     * @param clientId     应用id。可使用扫码登录应用或者第三方个人小程序的appId。
     * @param clientSecret 应用密钥。
     * @param code         OAuth 2.0 临时授权码。
     * @return userAccessToken
     */
    public static String getUserAccessToken(String clientId, String clientSecret, String code) {
        JSONObject params = new JSONObject();
        params.put("clientId", clientId);
        params.put("clientSecret", clientSecret);
        params.put("code", code);
        params.put("grantType", "authorization_code");
        String url = ApiUrls.OAUTH2_USER_ACCESS_TOKEN;
        JSONObject response = HttpUtils.sendPost(url, params.toJSONString());
        log.info("[GET_USER_ACCESS_TOKEN] response:{}", new Object[]{JSON.toJSONString(response)});
        if (response != null) {
            String accessToken = response.getString("accessToken");
            if (accessToken != null && accessToken.length() > 0) {
                return accessToken;
            }
        }
        return null;
    }

    /**
     * 获取用户通讯录个人信息
     * 调用本接口获取企业用户通讯录中的个人信息。
     *
     * @param unionId     用户的unionId。 如需获取当前授权人的信息，unionId参数可以传me。
     * @param accessToken 调用服务端接口的授权凭证。使用个人用户的accessToken
     * @return ContactUser
     */
    public static ContactUser getContactUsers(String unionId, String accessToken) {
        String url = ApiUrls.get(ApiUrls.OAUTH2_CONTACT_USERS, unionId);
        JSONObject headers = new JSONObject();
        headers.put("x-acs-dingtalk-access-token", accessToken);
        JSONObject json = HttpUtils.httpRequest(url, "GET", null, headers);
        if (json != null) {
            log.info("[GET_CONTACT_USERS] response:{}", new Object[]{json.toJSONString()});
            return json.toJavaObject(ContactUser.class);
        }
        log.error("[GET_CONTACT_USERS] response: null");
        return null;
    }
    
}
