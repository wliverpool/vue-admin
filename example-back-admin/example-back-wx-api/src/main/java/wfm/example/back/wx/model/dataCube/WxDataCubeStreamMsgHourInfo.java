package wfm.example.back.wx.model.dataCube;

/**
 * 结果类--获取消息分送分时数据
 * @author 吴福明
 */
public class WxDataCubeStreamMsgHourInfo extends WxDataCubeStreamMsgInfo{

    /**时间**/
    private String  ref_hour;

    public String getRef_hour() {
        return ref_hour;
    }

    public void setRef_hour(String ref_hour) {
        this.ref_hour = ref_hour;
    }


}
