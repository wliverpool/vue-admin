package wfm.example.back.wx.handler.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.handler.WeiXinReqHandler;
import wfm.example.back.wx.model.LocationInfo;
import wfm.example.back.wx.model.WeixinReqConfig;
import wfm.example.back.wx.model.WeixinReqParam;
import wfm.example.back.wx.util.HttpRequestProxy;
import wfm.example.back.wx.util.WeiXinReqUtil;

@Slf4j
public class WeixinReqLogoUploadHandler implements WeiXinReqHandler {

    private static Logger logger = LoggerFactory.getLogger(WeixinReqLogoUploadHandler.class);

    @SuppressWarnings("rawtypes")
    public String doRequest(WeixinReqParam weixinReqParam) throws WexinReqException {
        // TODO Auto-generated method stub
        String strReturnInfo = "";
        if(weixinReqParam instanceof LocationInfo){
            LocationInfo uploadMedia = (LocationInfo) weixinReqParam;
            ReqType reqType = uploadMedia.getClass().getAnnotation(ReqType.class);
            WeixinReqConfig objConfig = WeiXinReqUtil.getWeixinReqConfig(reqType.value());
            if(objConfig != null){
                String reqUrl = objConfig.getUrl();
                String fileName = uploadMedia.getFilePathName();
                File file = new File(fileName) ;
                InputStream fileIn = null;
                try {
                    fileIn = new FileInputStream(file);
                    String extName = fileName.substring(fileName.lastIndexOf(".") + 1);//扩展名
                    String contentType = WeiXinReqUtil.getFileContentType(extName);
                    if(contentType == null){
                        logger.error("没有找到对应的文件类型");
                    }
                    Map parameters = WeiXinReqUtil.getWeixinReqParam(weixinReqParam);
                    parameters.remove("filePathName");
                    strReturnInfo = HttpRequestProxy.uploadMedia(reqUrl, parameters, "UTF-8", fileIn, file.getName(), contentType);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    throw new WexinReqException(e);
                }
            }
        }else if(weixinReqParam instanceof LocationInfo){
            LocationInfo uploadMedia = (LocationInfo) weixinReqParam;
            ReqType reqType = uploadMedia.getClass().getAnnotation(ReqType.class);
            WeixinReqConfig objConfig = WeiXinReqUtil.getWeixinReqConfig(reqType.value());
            if(objConfig != null){
                String reqUrl = objConfig.getUrl();
                String fileName = uploadMedia.getFilePathName();
                File file = new File(fileName) ;
                InputStream fileIn = null;
                try {
                    fileIn = new FileInputStream(file);
                    String extName = fileName.substring(fileName.lastIndexOf(".") + 1);//扩展名
                    String contentType = WeiXinReqUtil.getFileContentType(extName);
                    if(contentType == null || !contentType.equals("image/jpeg")){
                        throw new WexinReqException("上传LOGO 大小限制1MB，像素为300*300，支持JPG格式以达到最佳效果");
                    }
                    Map parameters = WeiXinReqUtil.getWeixinReqParam(weixinReqParam);
                    parameters.remove("filePathName");
                    strReturnInfo = HttpRequestProxy.uploadMedia(reqUrl, parameters, "UTF-8", fileIn, file.getName(), contentType);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    throw new WexinReqException(e);
                }
            }
        }else{
            logger.info("没有找到对应的配置信息");
        }
        return strReturnInfo;
    }

}