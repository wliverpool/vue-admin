package wfm.example.back.wx.model.third;

/**
 * 授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
 */
public class ApiGetAuthorizerRetAuthorizerSType {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
