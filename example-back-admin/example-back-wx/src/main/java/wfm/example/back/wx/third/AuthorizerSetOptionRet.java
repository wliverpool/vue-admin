package wfm.example.back.wx.third;

/**
 * 设置选项返回结果
 * 成功返回 errcode":0,"errmsg":"ok"
 */
public class AuthorizerSetOptionRet {

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    public Integer getErrcode() {
        return errcode;
    }
    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }
    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

}