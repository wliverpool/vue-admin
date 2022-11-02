package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import wfm.example.back.common.exception.BizException;
import wfm.example.back.wx.model.weibo.WeiboSendDto;

import java.net.URLEncoder;

@Slf4j
public class WeiboSendUtils {

    /**
     * 指定一个图片URL地址抓取后上传并同时发布一条新微博
     */
    private static final String upload_url_text_url="https://api.weibo.com/2/statuses/upload_url_text.json?1=1";

    /**
     * 发布一条新微博
     */
    private static String update_url = "https://api.weibo.com/2/statuses/update.json?1=1";

    /**
     * 删除一条微博
     */
    private static String delete_url = "https://api.weibo.com/2/statuses/destroy.json?1=1";


    public static JSONObject sendWeibo(WeiboSendDto send){
        JSONObject j=null;
        try {
            log.info("发布新微博的参数为:"+send.toString());
            //验证请求参数
            getSendParmValidate(send);
            if(StringUtils.isEmpty(send.getUrl())){
                String sendUrl = getSendUrl(update_url, send);
                log.info("发布新微博的路径为："+sendUrl);
                j = HttpUtils.httpRequest(sendUrl, "POST", "");
            }else{
                String sendUrl = getSendUrl(upload_url_text_url, send);
                log.info("发布新微博的路径为："+sendUrl);
                j = HttpUtils.httpRequest(sendUrl, "POST", "");
            }
            if(j!=null){
                log.info("发布新微博的结果为:"+j.toString());
            }else{
                log.info("发布新微博的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }


    public static JSONObject delWeibo(WeiboSendDto send){
        JSONObject j=null;
        try {
            log.info("删除微博的参数为:"+send.toString());
            //验证请求参数
            delParmValidate(send);
            String delUrl = getDelUrl(delete_url, send);
            log.info("删除微博的路径为："+delUrl);
            j = HttpUtils.httpRequest(delUrl, "POST", "");
            if(j!=null){
                log.info("删除微博的结果为:"+j.toString());
            }else{
                log.info("删除微博的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }

    /**
     * 获取@当前用户的最新微博的请求必填参数验证
     *
     */
    public static void getSendParmValidate(WeiboSendDto send)throws Exception{
        if(StringUtils.isEmpty(send.getAccess_token())){
            throw new BizException("access_token不能为空");
        }
        if(StringUtils.isEmpty(send.getStatus())){
            throw new BizException("发布微博内容不能为空");
        }
    }

    /**
     * 获取@当前用户的最新微博的请求路径
     */
    @SuppressWarnings("deprecation")
    public static String getSendUrl (String interUrl,WeiboSendDto send) throws Exception{
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(send.getAccess_token())){
            requestUrl.append("&access_token="+send.getAccess_token());
        }
        if(StringUtils.isNotEmpty(send.getStatus())){
            requestUrl.append("&status="+send.getStatus());
        }
        if(StringUtils.isNotEmpty(send.getUrl())){
            String url = URLEncoder.encode(send.getUrl(), "UTF-8");
            requestUrl.append("&url="+url);
        }
        return requestUrl.toString();
    }

    public static void delParmValidate(WeiboSendDto send){
        if(StringUtils.isEmpty(send.getAccess_token())){
            throw new BizException("access_token不能为空");
        }
        if(StringUtils.isEmpty(send.getId())){
            throw new BizException("微博ID不能为空");
        }
    }

    @SuppressWarnings("deprecation")
    public static String getDelUrl (String interUrl,WeiboSendDto send){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(send.getAccess_token())){
            requestUrl.append("&access_token="+send.getAccess_token());
        }
        if(StringUtils.isNotEmpty(send.getId())){
            requestUrl.append("&id="+send.getId());
        }
        return requestUrl.toString();
    }

}
