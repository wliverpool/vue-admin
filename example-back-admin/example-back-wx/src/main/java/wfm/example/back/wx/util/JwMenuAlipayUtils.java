package wfm.example.back.wx.util;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayMobilePublicMenuAddRequest;
import com.alipay.api.request.AlipayMobilePublicMenuGetRequest;
import com.alipay.api.request.AlipayMobilePublicMenuUpdateRequest;
import com.alipay.api.response.AlipayMobilePublicMenuAddResponse;
import com.alipay.api.response.AlipayMobilePublicMenuGetResponse;
import com.alipay.api.response.AlipayMobilePublicMenuUpdateResponse;
import wfm.example.back.wx.core.alipay.AlipayClientFactory;
import wfm.example.back.wx.core.alipay.AlipayConfig;
import wfm.example.back.wx.model.alipayMenu.BizContent;

/**
 * 支付服务窗菜单API
 *
 * @author 吴福明
 *
 */
public class JwMenuAlipayUtils {

    /**
     * 添加菜单方法
     *
     * @param appAuthToken
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicMenuAddResponse menuAdd(String appAuthToken, BizContent model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMenuAddRequest request = new AlipayMobilePublicMenuAddRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 更新菜单方法
     *
     * @param appAuthToken
     * @param model
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicMenuUpdateResponse menuUpdate(String appAuthToken, BizContent model, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMenuUpdateRequest request = new AlipayMobilePublicMenuUpdateRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        String json = JSONObject.toJSONString(model);
        //update-begin-author:zhangjiaqiang Date:20161011 for:TASK #1422 【支付窗】3. 菜单同步，没有二级菜单，同步不成功
        json = json.replace(",\"subButton\":[]", "");
        //update-end-author:zhangjiaqiang Date:20161011 for:TASK #1422 【支付窗】3. 菜单同步，没有二级菜单，同步不成功
        request.setBizContent(json);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

    /**
     * 查询菜单方法
     *
     * @param appAuthToken
     * @param config
     * @return
     * @throws AlipayApiException
     */
    public static AlipayMobilePublicMenuGetResponse menuGet(String appAuthToken, AlipayConfig config) throws AlipayApiException {
        AlipayMobilePublicMenuGetRequest request = new AlipayMobilePublicMenuGetRequest();
        request.putOtherTextParam("app_auth_token", appAuthToken);
        return AlipayClientFactory.getAlipayClientInstance(config).execute(request);
    }

}
