package wfm.example.back.wx.model;

public class ShortLink {

    /**
     * 短链接对应的场景ID，该ID由商户自己定义
     */
    private String sceneId;

    /**
     * 对于场景ID的描述，商户自己定义
     */
    private String remark;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
