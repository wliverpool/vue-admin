package wfm.example.back.wx.model.qywx;

/**
 * 按钮的基类
 *
 * @author 吴福明
 */
public class Button {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Button [name=" + name + "]";
    }


}
