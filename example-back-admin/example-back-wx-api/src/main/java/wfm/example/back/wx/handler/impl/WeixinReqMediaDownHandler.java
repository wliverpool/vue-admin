package wfm.example.back.wx.handler.impl;

import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.handler.WeiXinReqHandler;
import wfm.example.back.wx.model.DownloadMedia;
import wfm.example.back.wx.model.WeixinReqConfig;
import wfm.example.back.wx.model.WeixinReqParam;
import wfm.example.back.wx.util.HttpRequestProxy;
import wfm.example.back.wx.util.WeiXinReqUtil;

import java.util.Map;

@Slf4j
public class WeixinReqMediaDownHandler implements WeiXinReqHandler {

    @SuppressWarnings("rawtypes")
    public String doRequest(WeixinReqParam weixinReqParam) throws WexinReqException {
        // TODO Auto-generated method stub
        String strReturnInfo = "";
        if(weixinReqParam.getClass().isAnnotationPresent(ReqType.class)){
            DownloadMedia downloadMedia = (DownloadMedia) weixinReqParam;
            ReqType reqType = weixinReqParam.getClass().getAnnotation(ReqType.class);
            WeixinReqConfig objConfig = WeiXinReqUtil.getWeixinReqConfig(reqType.value());
            if(objConfig != null){
                String reqUrl = objConfig.getUrl();
                String filePath = downloadMedia.getFilePath();
                Map parameters = WeiXinReqUtil.getWeixinReqParam(weixinReqParam);
                parameters.remove("filePathName");
                strReturnInfo = HttpRequestProxy.downMadGet(reqUrl, parameters, "UTF-8",filePath,downloadMedia.getMedia_id());
            }
        }else{
            log.info("没有找到对应的配置信息");
        }
        return strReturnInfo;
    }

}