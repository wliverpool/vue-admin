package wfm.example.back.wx.model.menu;

import java.util.List;

public class PersonalizedMenu {

    private List<WeixinButton> button;
    private WeixinMenuMatchrule matchrule;

    public List<WeixinButton> getButton() {
        return button;
    }

    public void setButton(List<WeixinButton> button) {
        this.button = button;
    }

    public WeixinMenuMatchrule getMatchrule() {
        return matchrule;
    }

    public void setMatchrule(WeixinMenuMatchrule matchrule) {
        this.matchrule = matchrule;
    }

}