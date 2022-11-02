package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import wfm.example.back.common.exception.BizException;

@Slf4j
public class WeiboAccountUtils {

    /**
     * 获取授权用户的UID的url
     */
    private static final String get_uid_url="https://api.weibo.com/2/account/get_uid.json?1=1";
    /**
     * @param access_token  OAuth授权必填参数 
     *
     * 获取授权用户的UID 
     */
    public static JSONObject getUid(String access_token){
        JSONObject j=null;
        try {
            log.info("获取授权用户的UID的参数为:access_token:"+access_token);
            //验证请求参数
            if(StringUtils.isEmpty(access_token)){
                throw new BizException("access_token不能为空");
            }
            StringBuilder uidUrl=new StringBuilder();
            uidUrl.append(get_uid_url);
            uidUrl.append("&access_token="+access_token);
            String requestUrl = uidUrl.toString();
            log.info("获取授权用户的UID的路径为:"+requestUrl);
            j = HttpUtils.httpRequest(requestUrl, "GET", null);
            if(j!=null){
                log.info("获取授权用户的UID的结果为:"+j.toString());
            }else{
                log.info("获取授权用户的UID的结果为:null");
            }
        }catch(BizException e)	{
            log.info(e.getMessage(), e);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return j;
    }

}
