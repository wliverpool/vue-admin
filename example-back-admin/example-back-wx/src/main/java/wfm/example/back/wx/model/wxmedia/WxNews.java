package wfm.example.back.wx.model.wxmedia;

import java.util.List;

/**
 * 图文消息图文消息
 * @author 吴福明
 *
 */
public class WxNews {

    /** 媒体id */
    private String total_count;

    /**
     * 微信接口返回的结果会是多个，使用list接收
     */
    private List<WxItem> items;

    /** 文件名称 */
    private String item_count;


    public String getTotal_count() {
        return total_count;
    }


    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }


    /*public WxItem getItem() {
        return item;
    }


    public void setItem(WxItem item) {
        this.item = item;
    }*/
    public List<WxItem> getItems() {
        return items;
    }


    public void setItems(List<WxItem> items) {
        this.items = items;
    }


    public String getItem_count() {
        return item_count;
    }




    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WxNews [total_count=");
        builder.append(total_count);
        builder.append(", items=");
        builder.append(items);
        builder.append(", item_count=");
        builder.append(item_count);
        builder.append("]");
        return builder.toString();
    }
}
