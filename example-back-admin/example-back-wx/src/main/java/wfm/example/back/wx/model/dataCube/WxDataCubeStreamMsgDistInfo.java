package wfm.example.back.wx.model.dataCube;

/**
 * 结果类--获取消息发送分布数据
 * @author 吴福明
 */
public class WxDataCubeStreamMsgDistInfo extends WxDataCubeStreamMsgInfo{

    private String  count_interval;

    public String getCount_interval() {
        return count_interval;
    }

    public void setCount_interval(String count_interval) {
        this.count_interval = count_interval;
    }

}
