package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.model.dingtalk.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpUtils {

    /**
     * http get请求
     * @param url
     * @return
     */
    public static JSONObject sendGet(String url) {
        JSONObject jsonObject = null;
        jsonObject = httpRequest(url,"GET",null);
        return jsonObject;
    }


    /**
     * http post请求
     * @param url
     * @return
     */
    public static JSONObject sendPost(String url) {
        JSONObject jsonObject = null;
        jsonObject = httpRequest(url,"POST",null);
        return jsonObject;
    }

    /**
     * http post请求
     * @param url
     * @param output json串
     * @return
     */
    public static JSONObject sendPost(String url,String output) {
        JSONObject jsonObject = null;
        jsonObject = httpRequest(url,"POST",output);
        return jsonObject;
    }

    /**
     * http post请求
     * @param url 请求地址
     * @param output json串
     */
    public static <T> Response<T> post(String url, String output, Type... types) {
        JSONObject json = httpRequest(url, "POST", output);
        return json.toJavaObject(new TypeReference<Response<T>>(types){});
    }

    public static JSONObject httpRequest(String request, String requestMethod, String output) {
        return httpRequest(request, requestMethod, output, null);
    }

    /**
     * 发起https请求并获取结果
     *
     * @param request       请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param output        提交的数据
     * @param headers       请求头
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String request, String requestMethod, String output, JSONObject headers) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            log.debug("[HTTP] http请求request:{},method:{},output{}", new Object[]{request,requestMethod,output});
            //建立连接
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(30000);
            connection.setUseCaches(false);
            connection.setRequestMethod(requestMethod);

            // begin 设置请求头
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.getString(key));
                }
            }
            if ("POST".equalsIgnoreCase(requestMethod)) {
                connection.setRequestProperty("Accept", "application/json;charset=UTF-8");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            }
            // end 设置请求头

            if(output!=null){
                OutputStream out = connection.getOutputStream();
                out.write(output.getBytes("UTF-8"));
                out.close();
            }
            //流处理
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line;
            while((line=reader.readLine())!=null){
                buffer.append(line);
            }
            //关闭连接、释放资源
            reader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            connection.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }finally {
            // 使用finally块来关闭输出流、输入流
            try {
                if (reader != null) {
                    reader.close();
                }
                if(inputStreamReader!=null){
                    inputStreamReader.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return jsonObject;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl
     *            请求地址
     * @param requestMethod
     *            请求方式（GET、POST）
     * @param outputStr
     *            提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONArray httpRequestArr(String requestUrl,
                                           String requestMethod, String outputStr) {
        JSONArray jsonArray = null;
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpUrlConn = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            URL url = new URL(requestUrl);
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            //HttpURLConnection设置网络超时
            httpUrlConn.setConnectTimeout(4500);
            httpUrlConn.setReadTimeout(4500);

//			httpUrlConn.setRequestProperty("content-type", "text/html");
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            log.info(buffer.toString());
            jsonArray = JSONArray.parseArray(buffer.toString());
//			jsonObject = JSONObject.parseObject(buffer.toString());
            // jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error(ce.getMessage(), ce);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally{
            try {
                httpUrlConn.disconnect();
            }catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return jsonArray;
    }
    
}
