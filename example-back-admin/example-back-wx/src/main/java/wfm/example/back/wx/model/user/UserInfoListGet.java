package wfm.example.back.wx.model.user;

import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 取多媒体文件
 *
 * @author 吴福明
 *
 * 正常情况下，微信会返回下述JSON数据包给公众号
 * {"total":2,"count":2,"data":{"openid":["","OPENID1","OPENID2"]},"next_openid":"NEXT_OPENID"}
 *
 */
@ReqType("getUserInfoList")
public class UserInfoListGet extends WeixinReqParam {

    private String next_openid;

    public String getNext_openid() {
        return next_openid;
    }

    public void setNext_openid(String next_openid) {
        this.next_openid = next_openid;
    }

}