package wfm.example.back.wx.handler.impl;

import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.constant.WeiXinConstant;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.handler.WeiXinReqHandler;
import wfm.example.back.wx.model.WeixinReqConfig;
import wfm.example.back.wx.model.WeixinReqParam;
import wfm.example.back.wx.util.HttpRequestProxy;
import wfm.example.back.wx.util.WeiXinReqUtil;

import java.util.Map;

@Slf4j
public class WeixinReqDefaultHandler implements WeiXinReqHandler {

    @SuppressWarnings("rawtypes")
    public String doRequest(WeixinReqParam weixinReqParam) throws WexinReqException {
        // TODO Auto-generated method stub
        String strReturnInfo = "";
        if(weixinReqParam.getClass().isAnnotationPresent(ReqType.class)){
            ReqType reqType = weixinReqParam.getClass().getAnnotation(ReqType.class);
            WeixinReqConfig objConfig = WeiXinReqUtil.getWeixinReqConfig(reqType.value());
            if(objConfig != null){
                String reqUrl = objConfig.getUrl();
                String method = objConfig.getMethod();
                String datatype = objConfig.getDatatype();
                Map parameters = WeiXinReqUtil.getWeixinReqParam(weixinReqParam);
                if(WeiXinConstant.JSON_DATA_TYPE.equalsIgnoreCase(datatype)){
                    parameters.clear();
                    parameters.put("access_token", weixinReqParam.getAccess_token());
                    weixinReqParam.setAccess_token(null);
                    String jsonData = WeiXinReqUtil.getWeixinParamJson(weixinReqParam);
                    strReturnInfo = HttpRequestProxy.doJsonPost(reqUrl, parameters, jsonData);
                }else{
                    if(WeiXinConstant.REQUEST_GET.equalsIgnoreCase(method)){
                        strReturnInfo = HttpRequestProxy.doGet(reqUrl, parameters, "UTF-8");
                    }else{
                        strReturnInfo = HttpRequestProxy.doPost(reqUrl, parameters, "UTF-8");
                    }
                }
            }
        }else{
            log.info("没有找到对应的配置信息");
        }
        return strReturnInfo;
    }

}