package wfm.example.back.wx.model.qywx;

public class FixSource {

    /**
     * 企业应用的id，整型。可在应用的设置页面查看
     */
    private int agentid;
    private MpnewEntity mpnews;

    public int getAgentid() {
        return agentid;
    }
    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }
    public MpnewEntity getMpnews() {
        return mpnews;
    }
    public void setMpnews(MpnewEntity mpnews) {
        this.mpnews = mpnews;
    }

}
