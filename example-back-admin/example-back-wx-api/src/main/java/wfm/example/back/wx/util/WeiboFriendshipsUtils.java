package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import wfm.example.back.common.exception.BizException;
import wfm.example.back.wx.model.weibo.WeiboFollowersDto;

@Slf4j
public class WeiboFriendshipsUtils {

    /**
     * 获取用户粉丝列表的url
     */
    private static final String followers_url="https://api.weibo.com/2/friendships/followers.json?1=1";

    /**
     * 获取用户粉丝UID列表
     */
    private static final  String followers_url_ids="https://api.weibo.com/2/friendships/followers/ids.json?1=1";
    
    /**
     * @param followers
     * 获取用户粉丝列表接口
     */
    public static JSONObject getFollowers(WeiboFollowersDto followers){
        JSONObject j=null;
        try {
            log.info("获取用户粉丝列表的参数为:"+followers.toString());
            //验证请求参数
            getFollowersParmValidate(followers);
            String requestUrl = getFollowersUrl(followers_url, followers);
            log.info("获取用户粉丝列表的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("获取用户粉丝列表的结果为:"+j.toString());
            }else{
                log.info("获取用户粉丝列表的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }
    /**
     * @param followers
     * 获取用户粉丝UID列表接口
     */
    public static JSONObject getFollowersIds(WeiboFollowersDto followers){
        JSONObject j=null;
        try {
            log.info("获取用户粉丝UID列表的参数为:"+followers.toString());
            //验证请求参数
            getFollowersIdsParmValidate(followers);
            String requestUrl = getFollowersIdsUrl(followers_url_ids, followers);
            log.info("获取用户粉丝UID列表的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("获取用户粉丝UID列表的结果为:"+j.toString());
            }else{
                log.info("获取用户粉丝UID列表的结果为:null");
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
    public static void getFollowersParmValidate(WeiboFollowersDto followers){
        if(StringUtils.isEmpty(followers.getAccess_token())){
            throw new BizException("access_token不能为空");
        }
        if(StringUtils.isEmpty(followers.getUid())&&StringUtils.isEmpty(followers.getScreen_name())){
            throw new BizException("uid与screen_name二者不能全为空");
        }
    }

    /**
     * 获取@当前用户的最新微博的请求路径
     */
    public static String getFollowersUrl (String interUrl,WeiboFollowersDto followers){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(followers.getAccess_token())){
            requestUrl.append("&access_token="+followers.getAccess_token());
        }
        if(StringUtils.isNotEmpty(followers.getUid())){
            requestUrl.append("&uid="+followers.getUid());
        }
        if(StringUtils.isNotEmpty(followers.getScreen_name())){
            requestUrl.append("&screen_name="+followers.getScreen_name());
        }
        if(StringUtils.isNotEmpty(followers.getCount())){
            requestUrl.append("&count="+followers.getCount());
        }
        if(StringUtils.isNotEmpty(followers.getCursor())){
            requestUrl.append("&cursor="+followers.getCursor());
        }
        if(StringUtils.isNotEmpty(followers.getTrim_status())){
            requestUrl.append("&trim_status="+followers.getTrim_status());
        }
        return requestUrl.toString();
    }

    /**
     * 获取用户粉丝UID列表的请求必填参数验证
     *
     */
    public static void getFollowersIdsParmValidate(WeiboFollowersDto followers){
        if(StringUtils.isEmpty(followers.getAccess_token())){
            throw new BizException("access_token不能为空");
        }
    }

    /**
     * 获取用户粉丝UID列表的请求路径
     */
    public static String getFollowersIdsUrl(String interUrl,WeiboFollowersDto followers){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(followers.getAccess_token())){
            requestUrl.append("&access_token="+followers.getAccess_token());
        }
        if(StringUtils.isNotEmpty(followers.getUid())){
            requestUrl.append("&uid="+followers.getUid());
        }
        if(StringUtils.isNotEmpty(followers.getScreen_name())){
            requestUrl.append("&screen_name="+followers.getScreen_name());
        }
        if(StringUtils.isNotEmpty(followers.getCount())){
            requestUrl.append("&count="+followers.getCount());
        }
        if(StringUtils.isNotEmpty(followers.getCursor())){
            requestUrl.append("&cursor="+followers.getCursor());
        }
        return requestUrl.toString();
    }

}
