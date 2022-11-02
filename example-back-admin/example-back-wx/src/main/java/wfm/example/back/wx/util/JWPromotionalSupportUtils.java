package wfm.example.back.wx.util;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayMobilePublicQrcodeCreateRequest;
import com.alipay.api.request.AlipayMobilePublicShortlinkCreateRequest;
import com.alipay.api.response.AlipayMobilePublicQrcodeCreateResponse;
import com.alipay.api.response.AlipayMobilePublicShortlinkCreateResponse;
import wfm.example.back.wx.core.alipay.AlipayClientFactory;
import wfm.example.back.wx.core.alipay.AlipayConfig;
import wfm.example.back.wx.model.ShortLink;
import wfm.example.back.wx.model.promotionalSupport.PromotionalSupport;

/**
 * 推广二维码
 *
 * @author 吴福明
 *
 */
public class JWPromotionalSupportUtils {

    /**
     * 带参推广二维码
     *
     * @param appAuthToken
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicQrcodeCreateResponse qrcodeCreate(String appAuthToken, PromotionalSupport model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicQrcodeCreateRequest request = new AlipayMobilePublicQrcodeCreateRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 推广短链接
     *
     * @param appAuthToken
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicShortlinkCreateResponse shortlinkCreate(String appAuthToken, ShortLink model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicShortlinkCreateRequest request = new AlipayMobilePublicShortlinkCreateRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

}
