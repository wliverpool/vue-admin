package wfm.example.back.wx.model.menu;

import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.model.WeixinReqParam;

import java.util.List;

/**
 * 取多媒体文件
 *
 * @author sfli.sir
 *
 */
@ReqType("menuCreate")
public class MenuCreate extends WeixinReqParam {

    /**
     * button 的json信息
     *
     * {
     "type":"click",
     "name":"今日歌曲",
     "key":"V1001_TODAY_MUSIC"
     },
     {
     "name":"菜单",
     "sub_button":[
     {
     "type":"view",
     "name":"搜索",
     "url":"http://www.soso.com/"
     },
     {
     "type":"view",
     "name":"视频",
     "url":"http://v.qq.com/"
     }

     */
    private List<WeixinButton> button;

    public List<WeixinButton> getButton() {
        return button;
    }

    public void setButton(List<WeixinButton> button) {
        this.button = button;
    }

}