package wfm.example.back.wx.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import wfm.example.back.wx.constant.WeiXinConstant;
import wfm.example.back.wx.exception.WexinReqException;
import wfm.example.back.wx.model.menu.*;
import wfm.example.back.wx.model.menu.config.CustomWeixinButtonConfig;
import wfm.example.back.wx.model.menu.config.WeixinButtonExtend;
import wfm.example.back.wx.model.wxsendmsg.WxArticleConfig;
import wfm.example.back.wx.service.WeiXinReqService;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信--menu
 *
 * @author 吴福明
 *
 */
public class JwMenuUtils {

    /**
     * 创建菜单
     *  button	是	一级菜单数组，个数应为1~3个
     sub_button	否	二级菜单数组，个数应为1~5个
     type	是	菜单的响应动作类型
     name	是	菜单标题，不超过16个字节，子菜单不超过40个字节
     key	click等点击类型必须	菜单KEY值，用于消息接口推送，不超过128字节
     url	view类型必须	网页链接，用户点击菜单可打开链接，不超过256字节
     appid 小程序appid ,在创建小程序菜单时候用到
     pagepath 小程序的页面路径,在创建小程序菜单时候用到
     * @param accessToken
     * @param button  的json字符串
     * @throws WexinReqException
     */
    public static String createMenu(String accessToken, List<WeixinButton> button) throws WexinReqException{
        MenuCreate m = new MenuCreate();
        m.setAccess_token(accessToken);
        m.setButton(button);
        JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(m);
        Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
        String msg = "";
        if(error == null){
            msg = result.getString("groupid");
        }else{
            msg = result.getString(WeiXinConstant.RETURN_ERROR_INFO_MSG);
        }
        return msg;
    }

    /**
     * 获取所有的菜单
     * @param accessToken
     * @return
     * @throws WexinReqException
     */
    public static List<WeixinButton> getAllMenu(String accessToken) throws WexinReqException{
        MenuGet g = new MenuGet();
        g.setAccess_token(accessToken);
        JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(g);
        Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
        List<WeixinButton> lstButton = null;
        JSONObject menu = result.getJSONObject("menu");
        JSONArray buttons = menu.getJSONArray("button");
        JSONArray subButtons = null;
        lstButton = new ArrayList<WeixinButton>();
        WeixinButton btn = null;
        WeixinButton subBtn = null;
        List<WeixinButton> lstSubButton = null;
        for (int i = 0; i < buttons.size(); i++) {
            btn = (WeixinButton) JSONObject.toBean(buttons.getJSONObject(i),
                    WeixinButton.class);
            subButtons = buttons.getJSONObject(i).getJSONArray("sub_button");
            if (subButtons != null) {
                lstSubButton = new ArrayList<WeixinButton>();
                for (int j = 0; j < subButtons.size(); j++) {
                    subBtn = (WeixinButton) JSONObject.toBean(
                            subButtons.getJSONObject(j), WeixinButton.class);
                    lstSubButton.add(subBtn);
                }
                btn.setSub_button(lstSubButton);
            }
            lstButton.add(btn);
        }
        return lstButton;
    }

    /**
     * 删除所有的菜单
     * @param accessToken
     * @return
     * @throws WexinReqException
     */
    public static String deleteMenu(String accessToken) throws WexinReqException{
        MenuDelete m = new MenuDelete();
        m.setAccess_token(accessToken);
        JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(m);
        String msg = result.getString(WeiXinConstant.RETURN_ERROR_INFO_MSG);
        return msg;
    }

    /**
     * 获取自定义接口配置
     * @param accessToken
     * @return
     * @throws WexinReqException
     */
    public static CustomWeixinButtonConfig getAllMenuConfigure(String accessToken) throws WexinReqException{
        MenuConfigureGet cmcg = new MenuConfigureGet();
        cmcg.setAccess_token(accessToken);
        JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(cmcg);
        Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);

        CustomWeixinButtonConfig customWeixinButtonConfig = (CustomWeixinButtonConfig) JSONObject.toBean(result, new CustomJsonConfig(CustomWeixinButtonConfig.class,"selfmenu_info"));

        JSONObject selfmenuInfo = result.getJSONObject("selfmenu_info");
        if(selfmenuInfo!=null && !JSONUtils.isNull(selfmenuInfo)){
            /**处理父类菜单 */
            JSONArray buttons = selfmenuInfo.getJSONArray("button");
            List<WeixinButtonExtend> listButton = new ArrayList<WeixinButtonExtend>();
            for(int i=0;i<buttons.size();i++){
                WeixinButtonExtend weixinButtonExtend = (WeixinButtonExtend) JSONObject.toBean(buttons.getJSONObject(i),new CustomJsonConfig(WeixinButtonExtend.class,"sub_button"));
                /**处理子类菜单 */
                JSONObject subButtonJsonObj = buttons.getJSONObject(i).getJSONObject("sub_button");
                if(subButtonJsonObj!=null && !JSONUtils.isNull(subButtonJsonObj)){
                    JSONArray subButtons = subButtonJsonObj.getJSONArray("list");
                    if (subButtons != null) {
                        List<WeixinButtonExtend> listSubButton = new ArrayList<WeixinButtonExtend>();
                        for (int j = 0; j < subButtons.size(); j++) {
                            WeixinButtonExtend subBtn = (WeixinButtonExtend) JSONObject.toBean(subButtons.getJSONObject(j), new CustomJsonConfig(WeixinButtonExtend.class,"news_info"));
                            /**处理菜单关联的图文消息 */
                            JSONObject newsInfoJsonObj = subButtons.getJSONObject(j).getJSONObject("news_info");
                            if(newsInfoJsonObj!=null && !JSONUtils.isNull(newsInfoJsonObj)){
                                JSONArray newsInfos = newsInfoJsonObj.getJSONArray("list");
                                List<WxArticleConfig> listNewsInfo = new ArrayList<WxArticleConfig>();
                                for (int k = 0; k < newsInfos.size(); k++) {
                                    WxArticleConfig wxArticleConfig = (WxArticleConfig) JSONObject.toBean(newsInfos.getJSONObject(k), WxArticleConfig.class);
                                    listNewsInfo.add(wxArticleConfig);
                                }
                                subBtn.setNews_info(listNewsInfo);
                            }
                            listSubButton.add(subBtn);
                        }
                        weixinButtonExtend.setSub_button(listSubButton);
                    }
                }
                listButton.add(weixinButtonExtend);
            }
            customWeixinButtonConfig.setSelfmenu_info(listButton);
        }
        return customWeixinButtonConfig;
    }

    public static void main(String[] args){
        String s="";
        try {
            s = "3DGIfJqqupzTPxvq_P-0ATDC2MDjFLqaz8S41SPmRIqLaA3PSb8FgN_PuhpZ5jEB4D6w7ZNeX3gbC3CfSOAz2wt4DxVKi2HD5BCjoecrB0Q";
//			s = JwTokenAPI.getAccessToken("wx00737224cb9dbc7d","b9479ebdb58d1c6b6efd4171ebe718b5");
//			s = JwTokenAPI.getAccessToken("wx298c4cc7312063df","fbf8cebf983c931bd7c1bee1498f8605");
            System.out.println(s);
//			WeixinButton button = new WeixinButton();
            CustomWeixinButtonConfig cb = JwMenuUtils.getAllMenuConfigure(s);

//			for(WeixinButton bb : b){
//				System.out.println(bb.toString());
//			}
//			List<WeixinButton> sub_button = new ArrayList<WeixinButton>();
//			List<WeixinButton> testsUb = new ArrayList<WeixinButton>();
//			WeixinButton w = new WeixinButton();
//			w.setName("测试菜单");
//			testsUb.add(w);
//
//			WeixinButton w1 = new WeixinButton();
//			/*
//			   "type": "scancode_waitmsg",
//               "name": "扫码带提示",
//               "key": "rselfmenu_0_0",
//            */
//			w1.setName("测试sub菜单");
//			w1.setKey("rselfmenu_0_0");
//			w1.setType("scancode_waitmsg");
//			sub_button.add(w1);
//			w.setSub_button(sub_button);
//
//
//			//s = getMenuButtonJson("button",b);
//			/*Gson gson = new Gson();
//			System.out.println(json);*/
//			s= JwMenuAPI.createMenu(s,testsUb);
//			System.out.println(s);
        } catch (WexinReqException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



}
