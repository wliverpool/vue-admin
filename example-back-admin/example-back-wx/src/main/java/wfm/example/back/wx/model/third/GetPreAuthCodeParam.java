package wfm.example.back.wx.model.third;

import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 获取预授权码
 * @author 吴福明
 *
 */
public class GetPreAuthCodeParam  extends WeixinReqParam {

    private String component_appid;
    public String getComponent_appid() {
        return component_appid;
    }
    public void setComponent_appid(String component_appid) {
        this.component_appid = component_appid;
    }

}
