package wfm.example.back.wx.model.couponManage;

/**
 * 礼品券。
 *
 * @author 吴福明
 */
public class Gift {

    /**
     * 基本的卡券数据，见下表，所有卡券通用
      */
    private BaseInfo base_info;

    /**
     * 礼品券专用，表示礼品名字。
     */
    private String gift;

    public BaseInfo getBase_info() {
        return base_info;
    }

    public void setBase_info(BaseInfo base_info) {
        this.base_info = base_info;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

}