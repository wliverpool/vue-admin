package wfm.example.back.wx.model.promotionalSupport;

public class CodeInfo {

    /**
     * 场景Id，最长32位，英文字母、数字以及下划线，开发者自定义
     */
    private Scene scene;

    /**
     * 跳转URL，扫码关注服务窗后会直接跳转到此URL
     */
    private String gotoUrl;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public String getGotoUrl() {
        return gotoUrl;
    }

    public void setGotoUrl(String gotoUrl) {
        this.gotoUrl = gotoUrl;
    }

}
