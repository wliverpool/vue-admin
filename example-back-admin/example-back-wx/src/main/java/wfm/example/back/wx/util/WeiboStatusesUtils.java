package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import wfm.example.back.common.exception.BizException;
import wfm.example.back.wx.model.weibo.WeiBoMentionsDto;
import wfm.example.back.wx.model.weibo.WeiboUserTimelineDto;

@Slf4j
public class WeiboStatusesUtils {

    /**
     * 获取用户发布的微博的url
     */
    private static final String user_timeline_url="https://api.weibo.com/2/statuses/user_timeline.json?1=1";

    /**
     * 获取用户发布的微博的ID的url
     */
    private static final String user_timeline_ids_url="https://api.weibo.com/2/statuses/user_timeline/ids.json?1=1";

    /**
     * 获取批量获取指定微博的转发数评论数的url
     */
    private static final String count_url="https://api.weibo.com/2/statuses/count.json?1=1";

    /**
     * 根据ID获取单条微博信息的url
     */
    private static final String show_url="https://api.weibo.com/2/statuses/show.json?1=1";

    /**
     * 获取@当前用户的最新微博的url
     */
    private static final String mentions_url="https://api.weibo.com/2/statuses/mentions.json?1=1";

    /**
     * 获取@当前用户的最新微博的ID的url
     */
    private static final String mentions_ids_url="https://api.weibo.com/2/statuses/mentions/ids.json?1=1";
    
    /**
     * @param userTimeline
     *
     * 获取用户发布的微博接口
     */
    public static JSONObject getUserTimeline(WeiboUserTimelineDto userTimeline){
        JSONObject j=null;
        try {
            log.info("请求获取用户发布的微博的参数为:"+userTimeline.toString());
            //验证请求参数
            getUserTimelineParmValidate(userTimeline);
            String requestUrl = getUserTimelineUrl(user_timeline_url, userTimeline);
            log.info("请求获取用户发布的微博的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("请求获取用户发布的微博的结果为:"+j.toString());
            }else{
                log.info("请求获取用户发布的微博的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }

    /**
     * @param userTimeline
     *
     * 获取用户发布的微博的ID的接口
     *
     */
    public static JSONObject getUserTimelineIds(WeiboUserTimelineDto userTimeline){
        JSONObject j=null;
        try {
            log.info("获取用户发布的微博的ID的参数为:"+userTimeline.toString());
            //验证请求参数
            getUserTimelineIdsParmValidate(userTimeline);
            String requestUrl = getUserTimelineUrl(user_timeline_ids_url, userTimeline);
            log.info("获取用户发布的微博的ID的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("获取用户发布的微博的ID的结果为:"+j.toString());
            }else{
                log.info("获取用户发布的微博的ID的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }

    /**
     * @param access_token  OAuth授权必填参数
     * @param ids 需要获取数据的微博ID,多个之间用逗号分隔
     * 批量获取指定微博的转发数评论数接口
     */
    public static JSONArray getCount(String access_token, String ids){
        JSONArray j=null;
        try {
            log.info("批量获取指定微博的转发数评论数的参数为:access_token:"+access_token+"     微博ID:"+ids);
            //验证请求参数
            getCountParmValidate(access_token,ids);
            String requestUrl = getCountUrl(count_url, access_token, ids);
            log.info("批量获取指定微博的转发数评论数的路径为:"+requestUrl);
            j = HttpUtils.httpRequestArr(requestUrl, "GET", null);
            if(j!=null){
                log.info("批量获取指定微博的转发数评论数的结果为:"+j.toString());
            }else{
                log.info("批量获取指定微博的转发数评论数的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }

    /**
     * @param access_token  OAuth授权必填参数
     * @param id
     *
     * 根据ID获取单条微博信息接口
     *
     */
    public static JSONObject getShow(String access_token,String id){
        JSONObject j=null;
        try {
            log.info("根据ID获取单条微博信息的参数为:access_token:"+access_token+"     微博ID:"+id);
            //验证请求参数
            getShowParmValidate(access_token,id);
            String requestUrl = getShowUrl(show_url, access_token, id);
            log.info("根据ID获取单条微博信息的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("根据ID获取单条微博信息的结果为:"+j.toString());
            }else{
                log.info("根据ID获取单条微博信息的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }

    /**
     *
     * @param mentions
     *
     * 获取@当前用户的最新微博接口
     */
    public static JSONObject getMentions(WeiBoMentionsDto mentions){
        JSONObject j=null;
        try {
            log.info("获取@当前用户的最新微博的参数为:"+mentions.toString());
            //验证请求参数
            getMentionsParmValidate(mentions);
            String requestUrl = getMentionsUrl(mentions_url, mentions);
            log.info("获取@当前用户的最新微博路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("获取@当前用户的最新微博的结果为:"+j.toString());
            }else{
                log.info("获取@当前用户的最新微博的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }

    /**
     *
     * @param mentions
     *
     * 获取@当前用户的最新微博的ID接口
     */
    public static JSONObject getMentionsIds(WeiBoMentionsDto mentions){
        JSONObject j=null;
        try {
            log.info("获取@当前用户的最新微博的ID的参数为:"+mentions.toString());
            //验证请求参数
            getMentionsParmValidate(mentions);
            String requestUrl = getMentionsUrl(mentions_ids_url, mentions);
            log.info("获取@当前用户的最新微博的ID路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("获取@当前用户的最新微博的ID的结果为:"+j.toString());
            }else{
                log.info("获取@当前用户的最新微博的ID的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }


    public static void main(String[] args) {
        //=========================获取用户发布的微博接口测试======start=================================
	    /*	WeiboUserTimelineDto userTimeline =new WeiboUserTimelineDto();
		userTimeline.setAccess_token("2.00rj8pTCRV_yBB5addd99887yjfcyC");
		//userTimeline.setUid("2273040767");
		//userTimeline.setScreen_name("联通超级炫铃");
		getUserTimeline(userTimeline);*/
        //==========================获取用户发布的微博接口测试======end===================================

        //=================获取用户发布的微博的ID的接口测试========start===================================
	    /*	WeiboUserTimelineDto userTimeline =new WeiboUserTimelineDto();
		userTimeline.setAccess_token("2.00rj8pTCRV_yBB5addd99887yjfcyC");
		//userTimeline.setUid("2273040767");
		userTimeline.setScreen_name("联通超级炫铃");
		getUserTimelineIds(userTimeline);*/
        //===================获取用户发布的微博的ID的接口测试=======end====================================

        //==================批量获取指定微博的转发数评论数接口测试========start==================================
		/*String access_token="2.00rj8pTCRV_yBB5addd99887yjfcyC";
		String ids="4042534311692339,4042534311692377";
		getCount(access_token, ids);*/
        //===================批量获取指定微博的转发数评论数接口测试========end===================================

        //==================根据ID获取单条微博信息接口测试========start==================================
		/*String access_token="2.00rj8pTCRV_yBB5addd99887yjfcyC";
		String id="3720211465180913";
		getShow(access_token, id);*/
        //===================根据ID获取单条微博信息接口测试========end===================================

        //==================获取@当前用户的最新微博接口测试========start==================================
		/*WeiBoMentionsDto mentions=new WeiBoMentionsDto();
		mentions.setAccess_token("2.00rj8pTCRV_yBB5addd99887yjfcyC");
		getMentions(mentions);*/
        //===================获取@当前用户的最新微博接口测试========end===================================

        //==================获取@当前用户的最新微博的ID接口测试========start==================================
		/*WeiBoMentionsDto mentions=new WeiBoMentionsDto();
		mentions.setAccess_token("2.00rj8pTCRV_yBB5addd99887yjfcyC");
		getMentionsIds(mentions);*/
        //===================获取@当前用户的最新微博的ID接口测试========end===================================
    }

    /**
     * 验证获取用户发布的微博的请求必填参数验证
     */
    public static void getUserTimelineParmValidate (WeiboUserTimelineDto userTimeline){
        if(StringUtils.isEmpty(userTimeline.getAccess_token())){
            throw new BizException("access_token 不能 为空");
        }
    }

    /**
     * 拼接获取用户发布的微博的请求路径
     */
    public static String getUserTimelineUrl (String interUrl,WeiboUserTimelineDto userTimeline){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(userTimeline.getAccess_token())){
            requestUrl.append("&access_token="+userTimeline.getAccess_token());
        }
        if(StringUtils.isNotEmpty(userTimeline.getUid())){
            requestUrl.append("&uid="+userTimeline.getUid());
        }
        if(StringUtils.isNotEmpty(userTimeline.getScreen_name())){
            requestUrl.append("&screen_name="+userTimeline.getScreen_name());
        }
        if(StringUtils.isNotEmpty(userTimeline.getSince_id())){
            requestUrl.append("&since_id="+userTimeline.getSince_id());
        }
        if(StringUtils.isNotEmpty(userTimeline.getMax_id())){
            requestUrl.append("&max_id="+userTimeline.getMax_id());
        }
        if(StringUtils.isNotEmpty(userTimeline.getCount())){
            requestUrl.append("&count="+userTimeline.getCount());
        }
        if(StringUtils.isNotEmpty(userTimeline.getPage())){
            requestUrl.append("&page="+userTimeline.getPage());
        }
        if(StringUtils.isNotEmpty(userTimeline.getBase_app())){
            requestUrl.append("&base_app="+userTimeline.getBase_app());
        }
        if(StringUtils.isNotEmpty(userTimeline.getFeature())){
            requestUrl.append("&feature="+userTimeline.getFeature());
        }
        if(StringUtils.isNotEmpty(userTimeline.getTrim_user())){
            requestUrl.append("trim_user="+userTimeline.getTrim_user());
        }
        return requestUrl.toString();
    }

    /**
     * 验证获取用户发布的微博的请求必填参数验证
     */
    public static void getUserTimelineIdsParmValidate (WeiboUserTimelineDto userTimeline){
        if(StringUtils.isEmpty(userTimeline.getAccess_token())){
            throw new BizException("access_token不能为空");
        }
        if(StringUtils.isEmpty(userTimeline.getUid())&&(StringUtils.isEmpty(userTimeline.getScreen_name()))){
            throw new BizException("uid与screen_name二者不能全为空");
        }
        if(StringUtils.isNotEmpty(userTimeline.getUid())&&(StringUtils.isNotEmpty(userTimeline.getScreen_name()))){
            throw new BizException("uid与screen_name二者只能选其一");
        }
    }

    /**
     * 验证批量获取指定微博的转发数评论数的请求必填参数验证
     */
    public static void getCountParmValidate(String access_token,String ids){
        if(StringUtils.isEmpty(access_token)){
            throw new BizException("access_token不能为空");
        }
        if(StringUtils.isEmpty(ids)){
            throw new BizException("微博ID不能为空");
        }else{
            String [] idArry=ids.split(",");
            if(idArry.length>100){
                throw new BizException("微博ID个数不能超过100");
            }
        }


    }

    /**
     * 拼接获取用户发布的微博的请求路径
     */
    public static String getCountUrl (String interUrl,String access_token,String ids){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(access_token)){
            requestUrl.append("&access_token="+access_token);
        }
        if(StringUtils.isNotEmpty(ids)){
            requestUrl.append("&ids="+ids);
        }
        return requestUrl.toString();
    }

    /**
     * 根据ID获取单条微博信息的请求必填参数验证
     */
    public static void getShowParmValidate(String access_token,String id){
        if(StringUtils.isEmpty(access_token)){
            throw new BizException("access_token不能为空");
        }
        if(StringUtils.isEmpty(id)){
            throw new BizException("微博ID不能为空");
        }else{
            String [] idArry=id.split(",");
            if(idArry.length>1){
                throw new BizException("微博ID个数只能为1");
            }
        }
    }

    /**
     * 根据ID获取单条微博信息的请求路径
     */
    public static String getShowUrl (String interUrl,String access_token,String id){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(access_token)){
            requestUrl.append("&access_token="+access_token);
        }
        if(StringUtils.isNotEmpty(id)){
            requestUrl.append("&id="+id);
        }
        return requestUrl.toString();
    }

    /**
     * 获取@当前用户的最新微博的请求必填参数验证
     *
     */
    public static void getMentionsParmValidate(WeiBoMentionsDto mentions){
        if(StringUtils.isEmpty(mentions.getAccess_token())){
            throw new BizException("access_token不能为空");
        }
    }

    /**
     * 获取@当前用户的最新微博的请求路径
     */
    public static String getMentionsUrl (String interUrl,WeiBoMentionsDto mentions){
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
        if(StringUtils.isNotEmpty(mentions.getFilter_by_type())){
            requestUrl.append("&filter_by_type="+mentions.getFilter_by_type());
        }
        return requestUrl.toString();
    }

}
