package wfm.example.back.wx.util;


import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import wfm.example.back.common.util.JSONHelper;
import wfm.example.back.wx.model.product.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 微信小店 - 商品
 * @author 吴福明
 *
 */
@Slf4j
public class JwProductUtils {

    /**
     * 增加商品
     */
    private static String create_commodity_url = "https://api.weixin.qq.com/merchant/create?access_token=${ACCESS_TOKEN}";

    /**
     * 修改商品
     */
    private static String update_commodity_url = "https://api.weixin.qq.com/merchant/update?access_token=ACCESS_TOKEN";

    /**
     * 查询商品
     */
    private static String get_commodity_url = "https://api.weixin.qq.com/merchant/get?access_token=ACCESS_TOKEN";

    /**
     * 删除商品
     */
    private static String del_commodity_url = "https://api.weixin.qq.com/merchant/del?access_token=ACCESS_TOKEN";

    /**
     * 上传图片
     */
    private static String upload_img_commodity_url = "https://api.weixin.qq.com/merchant/common/upload_img?access_token=ACCESS_TOKEN&filename=IMG_NAME";

    /**
     * 获取指定状态商品
     */
    private static String getbystatus_commodity_url = "https://api.weixin.qq.com/merchant/getbystatus?access_token=ACCESS_TOKEN";

    /**
     * 商品上下架
     */
    private static String modproductstatus_commodity_url = "https://api.weixin.qq.com/merchant/modproductstatus?access_token=ACCESS_TOKEN";

    /**
     * 指定分类的所有子分类
     */
    private static String getsub_commodity_url = "https://api.weixin.qq.com/merchant/category/getsub?access_token=ACCESS_TOKEN";

    /**
     * 指定子分类的所有SKU
     */
    private static String getsku_commodity_url = "https://api.weixin.qq.com/merchant/category/getsku?access_token=ACCESS_TOKEN";

    /**
     * 指定分类的所有属性
     */
    private static String getproperty_commodity_url = "https://api.weixin.qq.com/merchant/category/getproperty?access_token=ACCESS_TOKEN";

    /**
     * 增加商品
     */
    public static CommodityRtnInfo doAddCommodity(String newAccessToken, Product product) {
        if (newAccessToken != null) {
            String requestUrl =  WeixinUtil.parseWeiXinHttpUrl(create_commodity_url, newAccessToken);
            JSONObject obj = JSONObject.fromObject(product);
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", obj.toString());
            CommodityRtnInfo commodityRtnInfo = (CommodityRtnInfo)JSONObject.toBean(result, CommodityRtnInfo.class);
            return commodityRtnInfo;
        }
        return null;
    }

    /**
     * 删除商品
     */
    public static CommodityRtnInfo doDelCommodity(String newAccessToken, String product_id) {
        if (newAccessToken != null) {
            String requestUrl = del_commodity_url.replace("ACCESS_TOKEN", newAccessToken);
            String json = "{\"product_id\": \""+product_id+"\"}";
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", json);
            CommodityRtnInfo commodityRtnInfo = (CommodityRtnInfo)JSONObject.toBean(result, CommodityRtnInfo.class);
            return commodityRtnInfo;
        }
        return null;
    }

    /**
     * 修改商品
     */
    public static CommodityRtnInfo doUpdateCommodity(String newAccessToken, Product product, String accountid) {
        if (newAccessToken != null) {
            String requestUrl = update_commodity_url.replace("ACCESS_TOKEN", newAccessToken);
            JSONObject obj = JSONObject.fromObject(product);
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", obj.toString());
            CommodityRtnInfo commodityRtnInfo = (CommodityRtnInfo)JSONObject.toBean(result, CommodityRtnInfo.class);
            return commodityRtnInfo;
        }
        return null;
    }

    /**
     * 获取商品详细
     */
    public static Product getCommodity(String newAccessToken, String product_id) {
        if (newAccessToken != null) {
            String requestUrl = get_commodity_url.replace("ACCESS_TOKEN", newAccessToken);
            String json = "{\"product_id\": \""+product_id+"\"}";
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "GET", json);
            // 正常返回
            Product product = null;
            JSONObject info = result.getJSONObject("product_info");
            product = (Product)JSONObject.toBean(info, Product.class);
            return product;
        }
        return null;
    }

    /**
     * 获取指定状态的所有商品
     * 商品状态(0-全部, 1-上架, 2-下架)
     */
    public static CommodityRtnInfo getByStatus(String newAccessToken, Integer status) {
        if (newAccessToken != null) {
            String requestUrl = getbystatus_commodity_url.replace("ACCESS_TOKEN", newAccessToken);
            String json = "{\"status\": "+status+"}";
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", json);
            CommodityRtnInfo commodityRtnInfo = (CommodityRtnInfo)JSONObject.toBean(result, CommodityRtnInfo.class);
            return commodityRtnInfo;
        }
        return null;
    }



    /**
     * 上传图片
     * @param newAccessToken
     * @param filePath
     * @param fileName
     * @return
     */
    public static String uploadImg(String newAccessToken, String filePath, String fileName) {
        if (newAccessToken != null) {
            String requestUrl = upload_img_commodity_url.replace("ACCESS_TOKEN", newAccessToken).replace("IMG_NAME", fileName);
            byte[] fileByte;
            try {
                fileByte = fileData(filePath+fileName);
                JSONObject result = WxstoreUtils.httpRequest2(requestUrl, "POST", fileByte);
                if (result.getInt("errcode") == 0) {
                    return result.getString("image_url");
                }
                return "";
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return "";
    }

    private static byte[] fileData(String filePath) throws IOException {
        File file = new File(filePath);//存放照片的文件
        InputStream fis = null;
        byte[] imageByteArray = null;
        fis = new FileInputStream(file);
        imageByteArray= IOUtils.toByteArray(fis);
        return imageByteArray;
    }

    /**
     * 商品上下架
     * 上下架标识(0-下架, 1-上架)
     */
    public static CommodityRtnInfo doModproductstatus(String newAccessToken, String product_id, Integer status) {
        if (newAccessToken != null) {
            String requestUrl = modproductstatus_commodity_url.replace("ACCESS_TOKEN", newAccessToken);
            String json = "{\"product_id\":\""+product_id+"\",\"status\": "+status+"}";
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", json);
            CommodityRtnInfo commodityRtnInfo = (CommodityRtnInfo)JSONObject.toBean(result, CommodityRtnInfo.class);
            return commodityRtnInfo;
        }
        return null;
    }

    /**
     * 获取指定分类的所有子分类
     */
    public static List<CateInfo> getCateSub(String newAccessToken, Integer cate_id) {
        if (newAccessToken != null) {
            String requestUrl = getsub_commodity_url.replace("ACCESS_TOKEN", newAccessToken);
            String json = "{\"cate_id\": "+cate_id+"}";
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "GET", json);
            // 正常返回
            List<CateInfo> cateInfos = null;
            JSONArray info = result.getJSONArray("cate_list");
            cateInfos = JSONHelper.toList(info, CateInfo.class);
            return cateInfos;
        }
        return null;
    }

    /**
     * 获取指定子分类的所有SKU
     */
    public static List<SkuInfo> getCateSubSku(String newAccessToken, Integer cate_id) {
        if (newAccessToken != null) {
            String requestUrl = getsku_commodity_url.replace("ACCESS_TOKEN", newAccessToken);
            String json = "{\"cate_id\": "+cate_id+"}";
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "GET", json);
            // 正常返回
            List<SkuInfo> skuInfos = null;
            JSONArray info = result.getJSONArray("sku_table");
            skuInfos = JSONHelper.toList(info, SkuInfo.class);
            return skuInfos;
        }
        return null;
    }

    /**
     * 获取指定分类的所有属性
     */
    public static List<PropertiesInfo> getPropertyByCateId(String newAccessToken, Integer cate_id) {
        if (newAccessToken != null) {
            String requestUrl = getproperty_commodity_url.replace("ACCESS_TOKEN", newAccessToken);
            String json = "{\"cate_id\": "+cate_id+"}";
            JSONObject result = WxstoreUtils.httpRequest(requestUrl, "GET", json);
            // 正常返回
            List<PropertiesInfo> propertiesInfos = null;
            JSONArray info = result.getJSONArray("properties");
            propertiesInfos = JSONHelper.toList(info, PropertiesInfo.class);
            return propertiesInfos;
        }
        return null;
    }

}
