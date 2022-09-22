package wfm.example.back.common.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信帮助类
 * @author 吴福明
 */
@Slf4j
public class DySmsHelperUtils {

    private static final String SEND_EMAIL_URL = "cxjweb/sendMsg.htm";
    private static final String CX_SERVICE_RESPONSE_CODE = "code";
    private static final String CX_SERVICE_SUCCESS_CODE = "200";

    /**
     * 发送短信
     * @param ip
     * @param port
     * @param comment
     * @param telephone
     * @param templateNo
     * @return
     */
    public static boolean sendSms(String ip,Integer port,String comment,String telephone,String templateNo,String cxPrivateKey){
        Map<String, String> params = new HashMap<>();
        params.put("cv", comment);
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("templateId", templateNo);
        dataMap.put("mobile", telephone);
        dataMap.put("type", "1");
        dataMap.put("emailers","");
        dataMap.put("sender","");
        dataMap.put("senderNickname","");
        dataMap.put("params", JSON.toJSONString(params));
        String resultString = requestCX("https://" + ip + ":" + port + "/" + SEND_EMAIL_URL, dataMap, cxPrivateKey);
        JSONObject result = null;
        try {
            result = JSON.parseObject(resultString);
            return isSuccess(result);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private static String requestCX(String url,Map<String,String> dataMap ,String cxPrivateKey){
        Assert.notEmpty(dataMap,"请求参数不能为空");
        dataMap = RSASignUtils.buildRequestPara(dataMap, cxPrivateKey);
        Map<String, Object> params = new HashMap<>();
        for (String key: dataMap.keySet()){
            params.put(key, dataMap.get(key));
        }
        log.debug("添加签名后的请求参数 sPara：{}", params.toString());
        String resultString = HttpUtil.post(url,params);
        log.debug("请求结果:" + resultString);
        return resultString;
    }

    /**
     * 默认实现判断请求是否成功
     * @param result
     * @return
     */
    private static boolean isSuccess(JSONObject result) {
        return null!=result && StringUtils.isNotEmpty(result.getString(CX_SERVICE_RESPONSE_CODE)) && CX_SERVICE_SUCCESS_CODE.equals(result.getString(CX_SERVICE_RESPONSE_CODE));
    }

}
