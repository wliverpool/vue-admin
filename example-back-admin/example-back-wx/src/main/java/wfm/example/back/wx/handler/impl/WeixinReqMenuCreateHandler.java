package wfm.example.back.wx.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.handler.WeiXinReqHandler;
import wfm.example.back.wx.model.menu.MenuCreate;
import wfm.example.back.wx.model.menu.WeixinButton;
import wfm.example.back.wx.model.WeixinReqConfig;
import wfm.example.back.wx.model.WeixinReqParam;
import wfm.example.back.wx.util.HttpRequestProxy;
import wfm.example.back.wx.util.WeiXinReqUtil;

/**
 * 菜单创建的处理
 * @author 吴福明
 *
 */
@Slf4j
public class WeixinReqMenuCreateHandler implements WeiXinReqHandler {

    @SuppressWarnings("rawtypes")
    public String doRequest(WeixinReqParam weixinReqParam) throws WexinReqException {
        // TODO Auto-generated method stub
        String strReturnInfo = "";
        if(weixinReqParam.getClass().isAnnotationPresent(ReqType.class)){
            ReqType reqType = weixinReqParam.getClass().getAnnotation(ReqType.class);
            WeixinReqConfig objConfig = WeiXinReqUtil.getWeixinReqConfig(reqType.value());
            if(objConfig != null){
                String reqUrl = objConfig.getUrl();
                MenuCreate mc = (MenuCreate) weixinReqParam;
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("access_token", mc.getAccess_token());
                String jsonData = "{"+getMenuButtonJson("button",mc.getButton())+"}";
                log.info("处理创建菜单"+jsonData);
                strReturnInfo = HttpRequestProxy.doJsonPost(reqUrl, parameters, jsonData);
            }
        }else{
            log.info("没有找到对应的配置信息");
        }
        return strReturnInfo;
    }

    /**
     * 单独处理菜单 json信息
     * @param name
     * @param b
     * @return
     */
    private  String getMenuButtonJson(String name,List<WeixinButton> b){
        StringBuffer json = new StringBuffer();
        json.append("\""+name+"\":[");
        if(b==null || b.size() == 0){
            return json.append("]").toString();
        }
        List<WeixinButton> sub_button = null;
        //解决Gson对网页链接中的转义字符问题，改为不转义字符
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String objJson = "";
        for(WeixinButton m : b){
            sub_button = m.getSub_button();
            m.setSub_button(null);
            objJson = gson.toJson(m);
            json.append(objJson);
            if(sub_button != null && sub_button.size() > 0){
                json.setLength(json.length()-1);
                json.append(",");
                objJson = getMenuButtonJson("sub_button",sub_button);
                json.append(objJson);
                json.append("}");
            }
            m.setSub_button(sub_button);
            json.append(",");
        }
        json.setLength(json.length()-1);
        json.append("]");
        return json.toString();
    }

}