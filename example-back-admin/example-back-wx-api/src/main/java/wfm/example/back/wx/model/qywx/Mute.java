package wfm.example.back.wx.model.qywx;

public class Mute {

    private String userid;
    private Integer status;

    public Mute(String userid, Integer status) {
        this.userid = userid;
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
