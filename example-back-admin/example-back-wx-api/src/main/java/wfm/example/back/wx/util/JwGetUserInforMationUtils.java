package wfm.example.back.wx.util;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayMobilePublicFollowListRequest;
import com.alipay.api.request.AlipayMobilePublicGisGetRequest;
import com.alipay.api.response.AlipayMobilePublicFollowListResponse;
import com.alipay.api.response.AlipayMobilePublicGisGetResponse;
import wfm.example.back.wx.core.alipay.AlipayClientFactory;
import wfm.example.back.wx.core.alipay.AlipayConfig;
import wfm.example.back.wx.model.getUserInfoMateon.GetAddress;
import wfm.example.back.wx.model.getUserInfoMateon.GetUserInfoMateon;

/**
 * 关注用户
 *
 * @author 吴福明
 *
 */
public class JwGetUserInforMationUtils {

    /**
     * 获取关注者列表
     *
     * @param appAuthToken
     * @param model
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicFollowListResponse followlistQuery(String appAuthToken, GetUserInfoMateon model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicFollowListRequest request = new AlipayMobilePublicFollowListRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 获取用户地理位置
     *
     * @param appAuthToken
     * @param model
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicGisGetResponse gisget(String appAuthToken, GetAddress model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicGisGetRequest request = new AlipayMobilePublicGisGetRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

}
