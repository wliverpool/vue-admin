package wfm.example.back.wx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.google.gson.Gson;
import wfm.example.back.wx.constant.WeiXinConstant;
import wfm.example.back.wx.handler.WeiXinReqHandler;
import wfm.example.back.wx.model.WeixinReqConfig;
import wfm.example.back.wx.model.WeixinReqParam;
import wfm.example.back.wx.service.WeiXinReqService;

/**
 * 获取微信接口的信息
 * @author 吴福明
 *
 */
@Slf4j
public class WeiXinReqUtil {

    /**
     * 缓存请求配置
     */
    private static Map<String, WeixinReqConfig> REQ_MAPPING = new HashMap<String, WeixinReqConfig>();

    /**
     * 缓存处理请求handler
     */
    private static Map<String, WeiXinReqHandler> MAPPING_HANDLER = new HashMap<String, WeiXinReqHandler>();
    /**
     * 文件类型content_type
     */
    private static Properties file_content_type = null;

    /**
     * 通过名称获取对象
     * @param className
     * @return
     */
    public static Object getObjectByClassName(String className){
        Class obj = null;
        try {
            obj = Class.forName(className);
            return  obj.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 初始化映射请求
     * @param configName
     * @throws JDOMException
     * @throws IOException
     */
    public static void initReqConfig(String configName) throws JDOMException, IOException{
        InputStream is = WeiXinReqService.class.getClassLoader().getResourceAsStream(configName);
        SAXBuilder xmlBuilder = new SAXBuilder();
        Document doc = xmlBuilder.build(is);
        Element objRoot = doc.getRootElement();
        List<Element> lstMapping = objRoot.getChildren("req");
        WeixinReqConfig objConfig = null;
        for(Element mapping :lstMapping){
            String key = mapping.getAttribute("key").getValue();
            String method = mapping.getAttribute("method").getValue();
            String url = mapping.getAttribute("url").getValue();
            String mappingHandler ="wfm.example.back.wx.handler.impl.WeixinReqDefaultHandler";
            String datatype = WeiXinConstant.PARAM_DATA_TYPE;
            if(mapping.getAttribute("mappingHandler") != null){
                mappingHandler = mapping.getAttribute("mappingHandler").getValue();
            }
            if(mapping.getAttribute("datatype") != null){
                datatype = mapping.getAttribute("datatype").getValue();
            }
            objConfig = new WeixinReqConfig();
            objConfig.setKey(key);
            objConfig.setMappingHandler(mappingHandler);
            objConfig.setMethod(method);
            objConfig.setUrl(url);
            objConfig.setDatatype(datatype);
            WeiXinReqUtil.registerMapping(key, objConfig);
        }
    }

    /**
     * 注册映射服务处理
     * @param key
     * @param weixinReqConfig
     */
    public static void registerMapping(String key,WeixinReqConfig weixinReqConfig){
        WeiXinReqUtil.REQ_MAPPING.put(key, weixinReqConfig);
    }

    /**
     * 获取配置信息
     * @param key
     * @return
     */
    public static WeixinReqConfig getWeixinReqConfig(String key){
        return WeiXinReqUtil.REQ_MAPPING.get(key);
    }


    /**
     * 注册映射服务处理
     * @param className
     */
    public static WeiXinReqHandler getMappingHander(String className){
        WeiXinReqHandler handler = WeiXinReqUtil.MAPPING_HANDLER.get(className);
        if(handler==null){
            handler = (WeiXinReqHandler) getObjectByClassName(className);
            WeiXinReqUtil.MAPPING_HANDLER.put(className, handler);
        }
        return handler;
    }


    /**
     * 获取对象的map参数信息
     *
     * @param weixinReqParam
     * @return
     */
    public static Map getWeixinReqParam(WeixinReqParam weixinReqParam) {
        Gson gson = new Gson();
        Map params = null;
        try{
            String json = gson.toJson(weixinReqParam);
            params = gson.fromJson(json, Map.class);

        }catch(Exception e){
            log.error("处理参数解析出错: " + e.getMessage(), e);
            params = new HashMap();
            params.put("access_token", weixinReqParam.getAccess_token());
        }
        return params;
    }

    /**
     * 获取参数的json信息
     *
     * @param weixinReqParam
     * @return
     */
    public static String getWeixinParamJson(WeixinReqParam weixinReqParam) {
        Gson gson = new Gson();
        String json = gson.toJson(weixinReqParam);
        return json;
    }

    /**
     * 获取文件对应的 http流类型
     *
     * @param fileSuffix
     * @return
     */
    public static String getFileContentType(String fileSuffix) {
        if (file_content_type == null) {
            file_content_type = new Properties();
            InputStream in = WeiXinReqService.class.getClassLoader().getResourceAsStream("fie-content-type.properties");
            try {
                file_content_type.load(in);
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                file_content_type = new Properties();
            }
        }
        return (String) file_content_type.get(fileSuffix);
    }

    /**
     * 获取文件对应的 http流类型
     *
     * @param contentType
     * @return
     */
    public static String getFileSuffix(String contentType) {
        if (file_content_type == null) {
            file_content_type = new Properties();
            InputStream in = WeiXinReqService.class.getClassLoader().getResourceAsStream("fie-content-type.properties");
            try {
                file_content_type.load(in);
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
        String fileSuffix = "";
        Set type = file_content_type.entrySet();
        Iterator it = type.iterator();
        while (it.hasNext()) {
            Map.Entry entity = (Entry) it.next();
            if (entity.getValue().equals(contentType)) {
                fileSuffix = (String) entity.getKey();
                break;
            }
        }
        return fileSuffix;
    }


    public static void main(String[] args) throws JDOMException, IOException{
        String realPath = WeiXinReqUtil.class .getClassLoader().getResource("").getFile();
        System.out.println(realPath);
        WeiXinReqUtil.initReqConfig("weixin-reqcongfig.xml");
		/*Map<String,ActionConfig> mapActionConfig = MvcUtils.initStrutsConfig();
		Set<String> actionSet = mapActionConfig.keySet();
		for(Iterator it = actionSet.iterator();it.hasNext();){
			System.out.println(it.next().toString());
		}*/
    }
}