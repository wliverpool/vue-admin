package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.constant.qywx.JwParames;
import wfm.example.back.wx.model.qywx.AccessToken;
import wfm.example.back.wx.model.qywx.OpenToUser;
import wfm.example.back.wx.model.qywx.UserToOpen;

@Slf4j
public class JwCardMessageUtils {

    /**
     * userid转换成openid接口
     */
    private static String card_userConverOpen_url ="https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token=ACCESS_TOKEN";

    /**
     * openid转换成userid接口
     */
    private static String card_openConverUser_url="https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_userid?access_token=ACCESS_TOKEN";


    /**
     * userid转换成openid接口的api
     * @param userToOpen 用户实例
     * @param accessToken 有效的access_token
     * @return   0表示成功，其他值表示失败
     */
    public static int userConverOpen(UserToOpen userToOpen, String accessToken){
        int result = 0;
        log.info("[CREATEUSER]", "createUser param:userToOpen:{},accessToken:{}", new Object[]{userToOpen,accessToken});
        // 拼装获取成员列表的url
        String url = card_userConverOpen_url.replace("ACCESS_TOKEN", accessToken);
        // 将成员对象转换成json字符串
        String jsonUserTOOpen = JSONObject.toJSONString(userToOpen);
        log.info("[CREATEUSER]", "createUser param:jsonUser:{}", new Object[]{jsonUserTOOpen});
        // 调用接口创建用户
        JSONObject jsonObject = HttpUtils.sendPost(url, jsonUserTOOpen);

        log.info("[CREATEUSER]", "createUser response:{}", new Object[]{jsonObject.toJSONString()});
        // 调用接口创建用户
        if (null != jsonObject) {
            int errcode = jsonObject.getIntValue("errcode");
            result = errcode;
        }
        return result;
    }
    /**
     * openid转换成userid接口
     * @param openToUser 用户实例
     * @param accessToken 有效的access_token
     * @return   0表示成功，其他值表示失败
     */

    public static int openConverUser(OpenToUser openToUser, String accessToken){
        int result = 0;
        log.info("[CREATEUSER]", "createUser param:openToUser:{},accessToken:{}", new Object[]{openToUser,accessToken});
        // 拼装获取成员列表的url
        String url = card_userConverOpen_url.replace("ACCESS_TOKEN", accessToken);
        // 将成员对象转换成json字符串
        String jsonOpenToUser = JSONObject.toJSONString(openToUser);
        log.info("[CREATEUSER]", "createUser param:jsonOpenToUser:{}", new Object[]{jsonOpenToUser});
        // 调用接口创建用户
        JSONObject jsonObject = HttpUtils.sendPost(url, jsonOpenToUser);
        log.info("[CREATEUSER]", "createUser response:{}", new Object[]{jsonObject.toJSONString()});
        // 调用接口创建用户
        if (null != jsonObject) {
            int errcode = jsonObject.getIntValue("errcode");
            result = errcode;
        }
        return result;
    }
    public static void main(String[] args) {
        AccessToken accessToken = JwAccessTokenUtils.getAccessToken(JwParames.corpId,JwParames.secret);
        //测试userConverOpen 测试成功openid=oTFwXt76Y9nRM7itV1WmYS0OJ6WE token=DnwUIQ-4WrhvqoPqMEb9-KCuLmlZAOankR4b2SuVA_DkhT49J1uPI0pNivZ2ohVp
        UserToOpen user=new UserToOpen();
        user.setAgentid(1);
        user.setUserid("malimei");
        JwCardMessageUtils.userConverOpen(user, accessToken.getAccesstoken());

        //openid转换成userid接口,暂时没有测通过去
        /*OpenToUser open=new OpenToUser();
        open.setOpenid("oTFwXt76Y9nRM7itV1WmYS0OJ6WE");
        String accessToken="DnwUIQ-4WrhvqoPqMEb9-KCuLmlZAOankR4b2SuVA_DkhT49J1uPI0pNivZ2ohVp";
        JwCardMessageUtils.openConverUser(open, accessToken);*/


    }


}
