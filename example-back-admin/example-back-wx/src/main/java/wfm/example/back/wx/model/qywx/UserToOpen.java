package wfm.example.back.wx.model.qywx;

public class UserToOpen {

    /**
     * 企业号内的成员id
     */
    private String userid;

    /**
     * 整型，需要发送红包的应用ID，若只是使用微信支付和企业转账，则无需该参数
     */
    private int agentid;
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public int getAgentid() {
        return agentid;
    }
    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }


}
