package wfm.example.back.wx.model.user;

import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 取多媒体文件
 *
 * @author 吴福明
 *
 */
@ReqType("groupUpdate")
public class GroupUpdate extends WeixinReqParam {

    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }


}