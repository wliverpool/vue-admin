package wfm.example.back.wx.model.qywx;

public class DepartMsgResponse extends MsgResponse {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DepartMsgResponse [id=" + id + ",errcode=" + this.getErrcode() + ", errmsg=" + this.getErrmsg() +"]";
    }

}
