package wfm.example.back.wx.model.qywx;

import java.util.Arrays;

/**
 * 菜单
 * @author 吴福明
 *
 */
public class Menu {

    private Button[] button;

    public Button[] getButton() {
        return button;
    }

    public void setButton(Button[] button) {
        this.button = button;
    }

    @Override
    public String toString() {
        return "Menu [button=" + Arrays.toString(button) + "]";
    }


}
