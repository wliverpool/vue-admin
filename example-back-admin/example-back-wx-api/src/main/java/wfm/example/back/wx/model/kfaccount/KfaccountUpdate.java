package wfm.example.back.wx.model.kfaccount;

import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 取多媒体文件
 *
 * @author 吴福明
 *
 */
@ReqType("kfaccountUpdate")
public class KfaccountUpdate extends WeixinReqParam {

    private String kf_account;

    private String nickname;

    private String password;

    public String getKf_account() {
        return kf_account;
    }

    public void setKf_account(String kf_account) {
        this.kf_account = kf_account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}