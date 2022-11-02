package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayOpenPublicMessageSingleSendModel;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import wfm.example.back.wx.core.alipay.AlipayClientFactory;
import wfm.example.back.wx.core.alipay.AlipayConfig;
import wfm.example.back.wx.model.sendMessageImage.*;

/**
 * 支付服务窗发送消息API
 *
 * @author 吴福明
 *
 */
public class JwSendMessageAlipayUtils {

    /**
     * 异步单发消息（文本）
     *
     * @param appAuthToken
     *            商户授权后获取的app_auth_token
     * @param content
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicMessageCustomSendResponse messageCustomSend(String appAuthToken, String content, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMessageCustomSendRequest request = new AlipayMobilePublicMessageCustomSendRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        request.setBizContent(content);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 异步单发消息（文本）
     *
     * @param appAuthToken
     *            商户授权后获取的app_auth_token
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    @Deprecated
    public static AlipayMobilePublicMessageCustomSendResponse messageCustomSendText(String appAuthToken, SendMessage model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMessageCustomSendRequest request = new AlipayMobilePublicMessageCustomSendRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = "";
        json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 异步单发消息（图文消息，支持6条）
     *
     * @param appAuthToken
     *            商户授权后获取的app_auth_token
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    @Deprecated
    public static AlipayMobilePublicMessageCustomSendResponse messageCustomSendImageText(String appAuthToken, SendMessageImageText model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMessageCustomSendRequest request = new AlipayMobilePublicMessageCustomSendRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = "";
        json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 群发消息（文本）
     *
     * @param appAuthToken
     *            商户授权后获取的app_auth_token
     * @param content
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicMessageTotalSendResponse messageTotalSend(String appAuthToken, String content, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMessageTotalSendRequest request = new AlipayMobilePublicMessageTotalSendRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        request.setBizContent(content);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 群发消息（文本）
     *
     * @param appAuthToken
     *            商户授权后获取的app_auth_token
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    @Deprecated
    public static AlipayMobilePublicMessageTotalSendResponse messageTotalSendText(String appAuthToken, SendMessageTextMore model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMessageTotalSendRequest request = new AlipayMobilePublicMessageTotalSendRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = "";
        json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 群发消息（图文）
     *
     * @param appAuthToken
     *            商户授权后获取的app_auth_token
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    @Deprecated
    public static AlipayMobilePublicMessageTotalSendResponse messageTotalSendImageText(String appAuthToken, SendMessageImageTextMore model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMessageTotalSendRequest request = new AlipayMobilePublicMessageTotalSendRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = "";
        json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 单发模板消息
     *
     * @param appAuthToken
     *            商户授权后获取的app_auth_token
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicMessageSingleSendResponse messageSingleSend(String appAuthToken, SendMessageImageModel model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMessageSingleSendRequest request = new AlipayMobilePublicMessageSingleSendRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = "";
        json = JSONObject.toJSONString(model, SerializerFeature.WriteMapNullValue);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 单发模板消息（新版API）
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayOpenPublicMessageSingleSendResponse messageSingleSendNew(AlipayOpenPublicMessageSingleSendModel model, AlipayConfig config) throws AlipayApiException{
        AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 获取模板详情
     * @param templateId
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayOpenPublicTemplateMessageGetResponse templateMessageGet(String templateId, AlipayConfig config) throws AlipayApiException{
        AlipayOpenPublicTemplateMessageGetRequest request = new AlipayOpenPublicTemplateMessageGetRequest();
        JSONObject json = new JSONObject();
        json.put("template_id", templateId);
        request.setBizContent(json.toJSONString());
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

}
