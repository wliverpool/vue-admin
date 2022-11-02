package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import wfm.example.back.common.exception.BizException;
import wfm.example.back.wx.model.weibo.WeiBoMentionsDto;

@Slf4j
public class WeiboCommentsUtils {

    /**
     * 我发出的评论列表的url
     */
    private static final String by_me_url="https://api.weibo.com/2/comments/by_me.json?1=1";

    /**
     * 我收到的评论列表
     */
    private static final String to_me_url="https://api.weibo.com/2/comments/to_me.json?1=1";
    
    /**
     * @param mentions  OAuth授权必填参数
     *
     * 我发出的评论列表接口
     */
    public static JSONObject getByme(WeiBoMentionsDto mentions){
        JSONObject j=null;
        try {
            log.info("我发出的评论列表的参数为:"+mentions.toString());
            //验证请求参数
            getBymeParmValidate(mentions);
            String requestUrl = getTomeUrl(by_me_url, mentions);
            log.info("我发出的评论列表的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("我发出的评论列表的结果为:"+j.toString());
            }else{
                log.info("我发出的评论列表的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }

    /**
     * @param mentions  OAuth授权必填参数
     * 我收到的评论列表接口
     */
    public static JSONObject getTome(WeiBoMentionsDto mentions){
        JSONObject j=null;
        try {
            log.info("我收到的评论列表的参数为:"+mentions.toString());
            //验证请求参数
            getBymeParmValidate(mentions);
            String requestUrl = getBymeUrl(to_me_url, mentions);
            log.info("我收到的评论列表的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("我收到的评论列表的结果为:"+j.toString());
            }else{
                log.info("我收到的评论列表的结果为:null");
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
    public static void getBymeParmValidate(WeiBoMentionsDto mentions){
        if(StringUtils.isEmpty(mentions.getAccess_token())){
            throw new BizException("access_token不能为空");
        }
    }

    /**
     * 获取@当前用户的最新微博的请求路径
     */
    public static String getBymeUrl (String interUrl,WeiBoMentionsDto mentions){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(mentions.getAccess_token())){
            requestUrl.append("&access_token="+mentions.getAccess_token());
        }
        if(StringUtils.isNotEmpty(mentions.getSince_id())){
            requestUrl.append("&since_id="+mentions.getSince_id());
        }
        if(StringUtils.isNotEmpty(mentions.getMax_id())){
            requestUrl.append("&max_id="+mentions.getMax_id());
        }
        if(StringUtils.isNotEmpty(mentions.getCount())){
            requestUrl.append("&count="+mentions.getCount());
        }
        if(StringUtils.isNotEmpty(mentions.getPage())){
            requestUrl.append("&page="+mentions.getPage());
        }
        if(StringUtils.isNotEmpty(mentions.getFilter_by_source())){
            requestUrl.append("&filter_by_source="+mentions.getFilter_by_source());
        }
        return requestUrl.toString();
    }

    /**
     *
     * 获取@当前用户的最新微博的请求路径
     */
    public static String getTomeUrl (String interUrl,WeiBoMentionsDto mentions){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(mentions.getAccess_token())){
            requestUrl.append("&access_token="+mentions.getAccess_token());
        }
        if(StringUtils.isNotEmpty(mentions.getSince_id())){
            requestUrl.append("&since_id="+mentions.getSince_id());
        }
        if(StringUtils.isNotEmpty(mentions.getMax_id())){
            requestUrl.append("&max_id="+mentions.getMax_id());
        }
        if(StringUtils.isNotEmpty(mentions.getCount())){
            requestUrl.append("&count="+mentions.getCount());
        }
        if(StringUtils.isNotEmpty(mentions.getPage())){
            requestUrl.append("&page="+mentions.getPage());
        }
        if(StringUtils.isNotEmpty(mentions.getFilter_by_author())){
            requestUrl.append("&filter_by_author="+mentions.getFilter_by_author());
        }
        if(StringUtils.isNotEmpty(mentions.getFilter_by_source())){
            requestUrl.append("&filter_by_source="+mentions.getFilter_by_source());
        }
        return requestUrl.toString();
    }
    
}
