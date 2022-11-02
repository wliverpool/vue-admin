package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import wfm.example.back.wx.core.alipay.AlipayClientFactory;
import wfm.example.back.wx.core.alipay.AlipayConfig;
import wfm.example.back.wx.model.lable.LableGroup;
import wfm.example.back.wx.model.lable.LableId;
import wfm.example.back.wx.model.lable.UpDateLable;
import wfm.example.back.wx.model.lable.UserAddLable;

/**
 * 支付服务窗标签API
 *
 * @author 吴福明
 *
 */
public class JwLableUtils {

    /**
     * 创建标签
     *
     * @param appAuthToken
     * @param name
     *            标签名不能重复
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicLabelAddResponse lableadd(String appAuthToken, String name, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicLabelAddRequest request = new AlipayMobilePublicLabelAddRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        JSONObject json = new JSONObject();
        json.put("name", name);
        request.setBizContent(json.toJSONString());
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 查询标签
     *
     * @param appAuthToken
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicLabelQueryResponse lablequery(String appAuthToken, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicLabelQueryRequest request = new AlipayMobilePublicLabelQueryRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 修改标签
     *
     * @param appAuthToken
     * @param model
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicLabelUpdateResponse lableupdate(String appAuthToken, UpDateLable model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicLabelUpdateRequest request = new AlipayMobilePublicLabelUpdateRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 删除标签
     *
     * @param appAuthToken
     * @param model
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicLabelDeleteResponse labledel(String appAuthToken, LableId model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicLabelDeleteRequest request = new AlipayMobilePublicLabelDeleteRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 用户增加标签
     *
     * @param appAuthToken
     * @param model
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicLabelUserAddResponse lableUserAdd(String appAuthToken, UserAddLable model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicLabelUserAddRequest request = new AlipayMobilePublicLabelUserAddRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 查询用户标签
     *
     * @param appAuthToken
     * @param model
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicLabelUserQueryResponse lableUserQuery(String appAuthToken, UserAddLable model,AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicLabelUserQueryRequest request = new AlipayMobilePublicLabelUserQueryRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 删除用户标签
     *
     * @param appAuthToken
     * @param model
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicLabelUserDeleteResponse lableUserDel(String appAuthToken, UserAddLable model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicLabelUserDeleteRequest request = new AlipayMobilePublicLabelUserDeleteRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 根据标签组发
     *
     * @param appAuthToken
     * @param model
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicMessageLabelSendResponse sendMsg(String appAuthToken, LableGroup model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMessageLabelSendRequest request = new AlipayMobilePublicMessageLabelSendRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

}
