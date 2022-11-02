package wfm.example.back.wx.util;

import net.sf.json.JSONObject;
import wfm.example.back.wx.model.couponConsume.ConsumeRtnInfo;
import wfm.example.back.wx.model.couponConsume.EncryptRtnInfo;


/**
 * 微信卡券 - 核销接口
 * @author 吴福明
 */
public class JwCardConsumeUtils {

    /**
     * 消耗code 即核销一张卡券
     */
    private static final String consume_code_url = "https://api.weixin.qq.com/card/code/consume?access_token=ACCESS_TOKEN";

    /**
     * 解码code
     */
    private static final String decrypt_code_url = "https://api.weixin.qq.com/card/code/decrypt?access_token=ACCESS_TOKEN";

    /**
     * 核销一张卡券.
     *
     * @param newAccessToken
     * @param code
     * @param card_id
     * @return
     */
    public static ConsumeRtnInfo doConsume(String newAccessToken,String code,String card_id) {
        if (newAccessToken != null) {
            String requestUrl = consume_code_url.replace("ACCESS_TOKEN",
                    newAccessToken);
            String json = emptyStrJson("code",code,"card_id",card_id);
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", json);
            ConsumeRtnInfo consumeRtnInfo = (ConsumeRtnInfo) JSONObject.toBean(
                    result, ConsumeRtnInfo.class);
            return consumeRtnInfo;
        }
        return null;
    }

    /**
     * 解码code. 解码接口支持两种场景： 1.商家获取choos_card_info 后，将card_id 和encrypt_code
     * 字段通过解码接口，获取真实code。 2.卡券内跳转外链的签名中会对code 进行加密处理，通过调用解码接口获取真实code。
     * @param encrypt_code
     * @return
     */
    public static EncryptRtnInfo doDecrypt(String newAccessToken, String encrypt_code) {
        if (newAccessToken != null) {
            String requestUrl = decrypt_code_url.replace("ACCESS_TOKEN",
                    newAccessToken);
            String json = emptyStrJson("encrypt_code",encrypt_code);
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", json);
            EncryptRtnInfo encryptRtnInfo = (EncryptRtnInfo) JSONObject.toBean(
                    result, EncryptRtnInfo.class);
            return encryptRtnInfo;
        }
        return null;
    }

    /**
     * 主要解决传入参数为null或""生成json的问题。
     */
    private static String emptyStrJson(String field1,String value1){
        String json = "";
        if(value1!=null && value1.trim().length() > 0){
            json = "{\""+field1+"\":\""+value1+"\"}";
        }
        return json;
    }

    /**
     * 主要解决传入参数为null或""生成json的问题。
     */
    private static String emptyStrJson(String field1,String value1,String field2,String value2){
        String json = "";
        if(value1!=null && value1.trim().length() > 0){
            json = "{\""+field1+"\":\""+value1+"\"";
        }
        if(value2!=null && value2.trim().length() > 0){
            if(json.trim().length() > 0){
                json = json+",\""+field2+"\":\""+value2+"\"}";
            }else{
                json = "{\""+field2+"\":\""+value2+"\"}";
            }
        }
        if(json.trim().length() > 0 && !json.endsWith("}")){
            json = json + "}";
        }
        return json;
    }
}
