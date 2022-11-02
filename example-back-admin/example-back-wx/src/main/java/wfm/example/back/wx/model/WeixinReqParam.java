package wfm.example.back.wx.model;

/**
 * 微信请求父类
 * @author 吴福明
 *
 */
public class WeixinReqParam {

    /**
     * 微信获取信息令牌
     */
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

}