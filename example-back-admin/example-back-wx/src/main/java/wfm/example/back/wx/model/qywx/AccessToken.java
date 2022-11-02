package wfm.example.back.wx.model.qywx;

/**
 * 微信通用接口凭证
 *
 * @author 吴福明
 */
public class AccessToken {

    /**
     * 获取到的凭证
     */
    private String accesstoken;

    /**
     * 凭证有效时间，单位：秒
      */
    private int expiresIn;

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "AccessToken [accesstoken=" + accesstoken + ", expiresIn="
                + expiresIn + "]";
    }

}
