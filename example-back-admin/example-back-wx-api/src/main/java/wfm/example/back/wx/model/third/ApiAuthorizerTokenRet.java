package wfm.example.back.wx.model.third;

/**
 * （刷新）授权公众号的令牌返回数据
 */
public class ApiAuthorizerTokenRet {

    /**
     * 授权方令牌
     */
    private String authorizer_access_token;

    /**
     * 有效期，为2小时 7200
     */
    private Integer expires_in;

    /**
     * 刷新令牌
     */
    private String authorizer_refresh_token;

    public String getAuthorizer_access_token() {
        return authorizer_access_token;
    }
    public void setAuthorizer_access_token(String authorizer_access_token) {
        this.authorizer_access_token = authorizer_access_token;
    }
    public Integer getExpires_in() {
        return expires_in;
    }
    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }
    public String getAuthorizer_refresh_token() {
        return authorizer_refresh_token;
    }
    public void setAuthorizer_refresh_token(String authorizer_refresh_token) {
        this.authorizer_refresh_token = authorizer_refresh_token;
    }

}