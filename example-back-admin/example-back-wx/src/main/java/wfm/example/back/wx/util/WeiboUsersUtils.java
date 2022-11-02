package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import wfm.example.back.common.exception.BizException;

@Slf4j
public class WeiboUsersUtils {

    /**
     * 根据用户ID获取用户信息的url
     */
    private static final String show_url="https://api.weibo.com/2/users/show.json?1=1";
    
    /**
     * 批量获取用户的粉丝数、关注数、微博数
     */
    private static final String counts_url="https://api.weibo.com/2/users/counts.json?1=1";
    /**
     * @param access_token  OAuth授权必填参数 
     * @param uid  需要查询的用户ID
     * @param screen_name  需要查询的用户昵称
     * 根据用户ID获取用户信息
     */
    public static JSONObject getShow(String access_token, String uid, String screen_name){
        JSONObject j=null;
        try {
            log.info("根据用户ID获取用户信息的参数为:access_token:"+access_token+"    需要查询的用户ID:"+uid+"     需要查询的用户昵称:"+screen_name);
            //验证请求参数
            getShowParmValidate(access_token,uid,screen_name);
            String requestUrl = getShowUrl(show_url, access_token, uid, screen_name);
            log.info("根据用户ID获取用户信息的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("根据用户ID获取用户信息的结果为:"+j.toString());
            }else{
                log.info("根据用户ID获取用户信息的结果为:null");
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
     * @param uids  需要获取数据的用户UID，多个之间用逗号分隔
     *
     * 批量获取用户的粉丝数、关注数、微博数接口
     */
    public static JSONArray getCounts(String access_token, String uids){
        JSONArray j=null;
        try {
            log.info("根据用户ID获取用户信息的参数为:access_token:"+access_token+"    需要查询的用户ID:"+uids);
            //验证请求参数
            getCountsParmValidate(access_token, uids);
            String requestUrl = getCountsUrl(counts_url, access_token, uids);
            log.info("根据用户ID获取用户信息的路径为:"+requestUrl);
            j = HttpUtils.httpRequestArr(requestUrl, "GET", null);
            if(j!=null){
                log.info("根据用户ID获取用户信息的结果为:"+j.toString());
            }else{
                log.info("根据用户ID获取用户信息的结果为:null");
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
    public static void getShowParmValidate(String access_token,String uid,String screen_name){
        if(StringUtils.isEmpty(access_token)){
            throw new BizException("access_token不能为空");
        }
        if(StringUtils.isEmpty(uid)&&StringUtils.isEmpty(screen_name)){
            throw new BizException("uid与screen_name二者不能全为空");
        }
        if(StringUtils.isNotEmpty(uid)&&StringUtils.isNotEmpty(screen_name)){
            throw new BizException("uid与screen_name二者只能选其一");
        }
    }

    /**
     * 获取@当前用户的最新微博的请求路径
     */
    public static String getShowUrl (String interUrl,String access_token,String uid,String screen_name){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(access_token)){
            requestUrl.append("&access_token="+access_token);
        }
        if(StringUtils.isNotEmpty(uid)){
            requestUrl.append("&uid="+uid);
        }
        if(StringUtils.isNotEmpty(screen_name)){
            requestUrl.append("&screen_name="+screen_name);
        }
        return requestUrl.toString();
    }

    /**
     * 批量获取用户的粉丝数、关注数、微博数的请求必填参数验证
     *
     */
    public static void getCountsParmValidate(String access_token,String uids){
        if(StringUtils.isEmpty(access_token)){
            throw new BizException("access_token不能为空");
        }
        if(StringUtils.isEmpty(uids)){
            throw new BizException("需要获取数据的用户uids不能为空");
        }else{
            String [] uidArr=uids.split(",");
            if(uidArr.length>100){
                throw new BizException("需要获取数据的用户个数不能超过100");
            }
        }
    }

    /**
     * 批量获取用户的粉丝数、关注数、微博数的请求路径
     */
    public static String getCountsUrl (String interUrl,String access_token,String uids){
        StringBuilder requestUrl=new StringBuilder();
        requestUrl.append(interUrl);
        if(StringUtils.isNotEmpty(access_token)){
            requestUrl.append("&access_token="+access_token);
        }
        if(StringUtils.isNotEmpty(uids)){
            requestUrl.append("&uids="+uids);
        }
        return requestUrl.toString();
    }

    public static void main(String[] args) {
        //==================根据用户ID获取用户信息接口测试========start==================================
		/*String access_token="2.00rj8pTCRV_yBB5addd99887yjfcyC";
		String uid="";
		//String uid="2273040767";
		String screen_name="联通超级炫铃";
		//String screen_name="";
		getShow(access_token, uid, screen_name);*/
        //===================根据用户ID获取用户信息接口测试========end===================================

        //==================根据用户ID获取用户信息接口测试========start==================================
	    /*	String access_token="2.00rj8pTCRV_yBB5addd99887yjfcyC";
		 String uids="4042534311692339,4042534311692377";
		//String uids="2273040767";
		getCounts(access_token, uids);*/
        //===================根据用户ID获取用户信息接口测试========end===================================
    }

}
