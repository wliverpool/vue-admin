package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.constant.dingtalk.ApiUrls;
import wfm.example.back.wx.model.dingtalk.AccessToken;

/**
 * 钉钉基础接口
 *
 * @author 吴福明
 */
@Slf4j
public class JdtBaseUtils {

    /**
     * 获取access_token
     * <br>
     * https://developers.dingtalk.com/document/app/obtain-orgapp-token
     *
     * @param appKey    应用的唯一标识key
     * @param appSecret 应用的密钥
     * @return AccessToken
     */
    public static AccessToken getAccessToken(String appKey, String appSecret) {
        AccessToken accessToken = null;
        String url = ApiUrls.get(ApiUrls.ACCESS_TOKEN, appKey, appSecret);
        JSONObject response = HttpUtils.sendGet(url);

        // 如果请求成功
        if (response != null) {
            try {
                String access_token = response.getString("access_token");
                int expires_in = response.getIntValue("expires_in");
                accessToken = new AccessToken(access_token, expires_in);
                log.info("[ACCESS_TOKEN] 获取ACCESS_TOKEN成功:{}", new Object[]{accessToken});
            } catch (Exception e) {
                // 获取token失败
                int errcode = response.getIntValue("errcode");
                String errmsg = response.getString("errmsg");
                log.info("[ACCESS_TOKEN] 获取ACCESS_TOKEN失败 errcode:{} errmsg:{}", new Object[]{errcode, errmsg});
            }
        }
        return accessToken;
    }

}
