package wfm.example.back.wx.model.dataCube;

import wfm.example.back.wx.annotation.ReqType;
import wfm.example.back.wx.model.WeixinReqParam;

/**
 * 参数类--获取消息发送概况数据
 * @author 吴福明
 */
@ReqType("getupstreammsg")
public class WxDataCubeStreamMsgParam extends WeixinReqParam {

    // 开始时间
    private String begin_date = null;

    // 结束时间
    private String end_date = null;

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

}