package wfm.example.back.wx.model.qywx;

import java.util.Arrays;

/**
 * 复杂按钮（父按钮）
 *
 * @author 吴福明
 *
 */
public class ComplexButton extends Button {
    private Button[] sub_button;

    public Button[] getSub_button() {
        return sub_button;
    }

    public void setSub_button(Button[] sub_button) {
        this.sub_button = sub_button;
    }

    @Override
    public String toString() {
        return "ComplexButton [sub_button=" + Arrays.toString(sub_button) + "]";
    }


}
