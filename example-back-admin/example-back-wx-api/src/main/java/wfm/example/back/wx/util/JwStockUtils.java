package wfm.example.back.wx.util;

import net.sf.json.JSONObject;
import wfm.example.back.wx.model.stock.StockInfo;
import wfm.example.back.wx.model.stock.StockRtnInfo;

/**
 * 微信小店 - 库存
 * @author 吴福明
 *
 */
public class JwStockUtils {

    /**
     * 增加库存
     */
    private static String add_stock_url = "https://api.weixin.qq.com/merchant/stock/add?access_token=ACCESS_TOKEN";

    /**
     * 减少库存
     */
    private static String sub_stock_url = "https://api.weixin.qq.com/merchant/stock/reduce?access_token=ACCESS_TOKEN";

    /**
     * 增加库存
     */
    public static StockRtnInfo doAddStock(String newAccessToken, StockInfo stockInfo) {
        if (newAccessToken != null) {
            String requestUrl = add_stock_url.replace("ACCESS_TOKEN", newAccessToken);
            JSONObject obj = JSONObject.fromObject(stockInfo);
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", obj.toString());
            StockRtnInfo stockRtnInfo = (StockRtnInfo)JSONObject.toBean(result, StockRtnInfo.class);
            return stockRtnInfo;
        }
        return null;
    }

    /**
     * 减少库存
     */
    public static StockRtnInfo doSubStock(String newAccessToken, StockInfo stockInfo) {
        if (newAccessToken != null) {
            String requestUrl = sub_stock_url.replace("ACCESS_TOKEN", newAccessToken);
            JSONObject obj = JSONObject.fromObject(stockInfo);
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", obj.toString());
            StockRtnInfo stockRtnInfo = (StockRtnInfo)JSONObject.toBean(result, StockRtnInfo.class);
            return stockRtnInfo;
        }
        return null;
    }

}
