package wfm.example.back.wx.handler.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.handler.WeiXinReqHandler;
import wfm.example.back.wx.model.message.IndustryTemplateMessageSend;
import wfm.example.back.wx.model.message.TemplateMessage;
import wfm.example.back.wx.model.WeixinReqConfig;
import wfm.example.back.wx.model.WeixinReqParam;
import wfm.example.back.wx.util.HttpRequestProxy;
import wfm.example.back.wx.util.WeiXinReqUtil;

/**
 * 模板消息发送
 * @author sfli.sir
 *
 */
@Slf4j
public class WeixinReqTemplateMessageHandler implements WeiXinReqHandler {

    @SuppressWarnings("rawtypes")
    public String doRequest(WeixinReqParam weixinReqParam) throws WexinReqException {
        // TODO Auto-generated method stub
        String strReturnInfo = "";
        if(weixinReqParam.getClass().isAnnotationPresent(ReqType.class)){
            ReqType reqType = weixinReqParam.getClass().getAnnotation(ReqType.class);
            WeixinReqConfig objConfig = WeiXinReqUtil.getWeixinReqConfig(reqType.value());
            if(objConfig != null){
                String reqUrl = objConfig.getUrl();
                IndustryTemplateMessageSend mc = (IndustryTemplateMessageSend) weixinReqParam;
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("access_token", mc.getAccess_token());
                String jsonData = getMsgJson(mc) ;
                log.info("处理模板消息"+jsonData);
                strReturnInfo = HttpRequestProxy.doJsonPost(reqUrl, parameters, jsonData);
            }
        }else{
            log.info("没有找到对应的配置信息");
        }
        return strReturnInfo;
    }

    /**
     * 单独处理 json信息
     * @param mc
     * @return
     */
    private  String getMsgJson(IndustryTemplateMessageSend mc){
        StringBuffer json = new StringBuffer();
        Gson gson = new Gson();
        TemplateMessage tm = mc.getData();
        mc.setData(null);
        String objJson = gson.toJson(mc);
        mc.setData(tm);
        json.append(objJson);
        json.setLength(json.length()-1);
        json.append(",");
        json.append("\"data\":{");

        objJson = gson.toJson(tm.getFirst());
        json.append(" \"first\":");
        json.append(objJson);
        json.append(",");
        objJson = gson.toJson(tm.getKeynote1());
        json.append(" \"keynote1\":");
        json.append(objJson);
        json.append(",");
        objJson = gson.toJson(tm.getKeynote2());
        json.append(" \"keynote2\":");
        json.append(objJson);
        json.append(",");
        objJson = gson.toJson(tm.getKeynote3());
        json.append(" \"keynote3\":");
        json.append(objJson);
        json.append(",");
        objJson = gson.toJson(tm.getRemark());
        json.append(" \"remark\":");
        json.append(objJson);
        json.append("}}");
        return json.toString();
    }

}