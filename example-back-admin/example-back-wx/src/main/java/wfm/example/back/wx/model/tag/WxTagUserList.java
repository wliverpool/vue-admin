package wfm.example.back.wx.model.tag;

import java.util.ArrayList;
import java.util.List;

public class WxTagUserList {

    List<String> openid = new ArrayList<String>();

    public List<String> getOpenid() {
        return openid;
    }

    public void setOpenid(List<String> openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "WxTagUserList [openid=" + openid + "]";
    }

}
