package wfm.example.back.wx.model.user;

import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 取多媒体文件
 *
 * @author 吴福明
 *
 */
@ReqType("userRemarkUpdate")
public class UserRemarkUpdate extends WeixinReqParam {

    private String openid;

    private String remark;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}