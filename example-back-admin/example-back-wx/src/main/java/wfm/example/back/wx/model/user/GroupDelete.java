package wfm.example.back.wx.model.user;

import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 分组删除
 *
 * @author 吴福明
 *
 */
@ReqType("groupDelete")
public class GroupDelete extends WeixinReqParam {

    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}