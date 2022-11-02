package wfm.example.back.wx.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import wfm.example.back.wx.constant.qywx.JwParames;
import wfm.example.back.wx.model.qywx.AccessToken;
import wfm.example.back.wx.model.qywx.FixSource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class JwQyMediaUtils {

    /**
     * 获取临时性图文素材
     */
    private static String sendsource_download_url="https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    /**
     * 上传永久性图文素材
     */
    private static String fixsource_upload_url="https://qyapi.weixin.qq.com/cgi-bin/material/add_mpnews?access_token=ACCESS_TOKEN";

    /**
     * 删除永久性素材
     */
    private static String FixSource_delete_url="https://qyapi.weixin.qq.com/cgi-bin/material/del?access_token=ACCESS_TOKEN&agentid=AGENTID&media_id=MEDIA_ID";

    /**
     * 修改永久图文素材
     */
    private static String fixsource_update_url ="https://qyapi.weixin.qq.com/cgi-bin/material/update_mpnews?access_token=ACCESS_TOKEN";

    /**
     * 获取素材总数
     */
    private static String source_count_url  ="https://qyapi.weixin.qq.com/cgi-bin/material/get_count?access_token=ACCESS_TOKEN&agentid=AGENTID";

    /**
     * 获取素材列表
     */
    private static String source_list_url="https://qyapi.weixin.qq.com/cgi-bin/material/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID&agentid=AGENTID";
    
    /**
     * 上传临时性素材
     * @param fileType
     * @param filePath
     * @param token
     * @return
     * */

    public static JSONObject sendMedia(String fileType, String filePath, String token) {
        String result = null;
        JSONObject jsonobject = new JSONObject();
        jsonobject = null;
        File file = new File(filePath);
        if(!file.exists()||!file.isFile()){
            jsonobject = null;
        }else{
            try{
                String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="+ token + "&type="+fileType;
                URL urlObj = new URL(requestUrl);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false); // post方式不能使用缓存
                con.setRequestProperty("Connection", "Keep-Alive");// 设置请求头信息
                con.setRequestProperty("Charset", "UTF-8");
                String BOUNDARY = "----------" + System.currentTimeMillis();// 设置边界
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
                // 请求正文信息
                // 第一部分：
                StringBuilder sb = new StringBuilder();
                sb.append("--"); // 必须多两道线
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");
                sb.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] head = sb.toString().getBytes("utf-8");
                // 获得输出流
                OutputStream out = new DataOutputStream(con.getOutputStream());
                // 输出表头
                out.write(head);
                // 文件正文部分
                // 把文件已流文件的方式 推入到url中
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                in.close();
                // 结尾部分
                byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
                out.write(foot);
                out.flush();
                out.close();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = null;
                try {
                    // 定义BufferedReader输入流来读取URL的响应
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    if(result==null){
                        result = buffer.toString();
                    }
                } catch (IOException e) {
                    throw new IOException("数据读取异常");
                } finally {
                    if(reader!=null){
                        reader.close();
                    }
                }
                jsonobject = JSONObject.parseObject(result);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return jsonobject;
    }

    /**
     *
     * 上传永久性素材
     */
    public static int uploadfixsource(FixSource fixSource, String accessToken) {
        log.info("[CREATEMENU]", "createText param:accessToken:{},agentid:{},fixmpnews:{}", new Object[]{accessToken,fixSource});
        int result = 0;
        // 拼装发送信息的url
        String url = fixsource_upload_url.replace("ACCESS_TOKEN", accessToken) ;
        // 将信息对象转换成json字符串
        String jsonFixMpnews = JSONObject.toJSONString(fixSource);
        log.info("[CREATEMENU]", "sendMessage param:jsonText:{}", new Object[]{jsonFixMpnews});
        // 调用接口发送信息
        JSONObject jsonObject = HttpUtils.sendPost(url, jsonFixMpnews);
        log.info("[CREATEMENU]", "sendMessage response:{}", new Object[]{jsonObject.toJSONString()});
        if (null != jsonObject) {
            int errcode = jsonObject.getIntValue("errcode");
            result = errcode;
        }
        return result;
    }

    /**
     * 上传其他永久性素材
     *
     */
    public static JSONObject sendotherMedia(String fileType,String filePath,String token,String agentid ) {
        String result = null;
        JSONObject jsonobject = new JSONObject();
        jsonobject = null;
        File file = new File(filePath);
        if(!file.exists()||!file.isFile()){
            jsonobject = null;
        }else{
            try{
                String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/material/add_material?agentid="+agentid+"&access_token="+ token + "&type="+fileType;
                URL urlObj = new URL(requestUrl);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false); // post方式不能使用缓存
                con.setRequestProperty("Connection", "Keep-Alive");// 设置请求头信息
                con.setRequestProperty("Charset", "UTF-8");
                String BOUNDARY = "----------" + System.currentTimeMillis();// 设置边界
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
                // 请求正文信息
                // 第一部分：
                StringBuilder sb = new StringBuilder();
                sb.append("--"); // 必须多两道线
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");
                sb.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] head = sb.toString().getBytes("utf-8");
                // 获得输出流
                OutputStream out = new DataOutputStream(con.getOutputStream());
                // 输出表头
                out.write(head);
                // 文件正文部分
                // 把文件已流文件的方式 推入到url中
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                in.close();
                // 结尾部分
                byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
                out.write(foot);
                out.flush();
                out.close();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = null;
                try {
                    // 定义BufferedReader输入流来读取URL的响应
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    if(result==null){
                        result = buffer.toString();
                    }
                } catch (IOException e) {
                    throw new IOException("数据读取异常");
                } finally {
                    if(reader!=null){
                        reader.close();
                    }
                }
                jsonobject = JSONObject.parseObject(result);
            }catch(Exception e){
                log.error(e.getMessage(), e);
            }
        }
        return jsonobject;
    }

    /**
     * 删除永久性素材
     *
     */
    public static int deletefixSource(String accessToken,String agentid,String media_id) {
        log.info("[DELETEMENU]", "deletefixSource param:accessToken:{},agentid:{}", new Object[]{accessToken,agentid});
        int result = 0;
        // 拼装删除素材的url
        String url = FixSource_delete_url.replace("ACCESS_TOKEN", accessToken).replace("AGENTID", agentid).replace("MEDIA_ID", media_id);
        // 调用接口删除素材
        JSONObject jsonObject = HttpUtils.sendGet(url);
        log.info("[DELETEMENU]", "deletefixSource response:{}", new Object[]{jsonObject.toJSONString()});
        if (null != jsonObject) {
            int errcode = jsonObject.getIntValue("errcode");
            result = errcode;
        }
        return result;
    }

    /**
     * 获取素材总数
     */
    public static JSONObject getCountSource( String accessToken,String agentid) {
        log.info("[CREATEMENU]", "createText param:accessToken:{},agentid:{},image:{}", new Object[]{accessToken,agentid});
        int result = 0;
        // 拼装发送信息的url
        String url = fixsource_update_url.replace("ACCESS_TOKEN", accessToken).replace("AGENTID", agentid);

        // 调用接口发送信息
        JSONObject jsonObject = HttpUtils.sendGet(url);
        log.info("[CREATEMENU]", "jsonUpdateSource response:{}", new Object[]{jsonObject.toJSONString()});
        return jsonObject;
    }

    /**
     *
     * 上传图文消息内的图片
     *
     */
    public static JSONObject sendImageMedia(String fileType,String filePath,String token) {
        String result = null;
        JSONObject jsonobject = new JSONObject();
        jsonobject = null;
        File file = new File(filePath);
        if(!file.exists()||!file.isFile()){
            jsonobject = null;
        }else{
            try{
                String requestUrl="https://qyapi.weixin.qq.com/cgi-bin/media/uploadimg?access_token="+ token;
                URL urlObj = new URL(requestUrl);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false); // post方式不能使用缓存
                con.setRequestProperty("Connection", "Keep-Alive");// 设置请求头信息
                con.setRequestProperty("Charset", "UTF-8");
                String BOUNDARY = "----------" + System.currentTimeMillis();// 设置边界
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
                // 请求正文信息
                // 第一部分：
                StringBuilder sb = new StringBuilder();
                sb.append("--"); // 必须多两道线
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");
                sb.append("Content-Type:application/octet-stream\r\n\r\n");
                byte[] head = sb.toString().getBytes("utf-8");
                // 获得输出流
                OutputStream out = new DataOutputStream(con.getOutputStream());
                // 输出表头
                out.write(head);
                // 文件正文部分
                // 把文件已流文件的方式 推入到url中
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                in.close();
                // 结尾部分
                byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
                out.write(foot);
                out.flush();
                out.close();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = null;
                try {
                    // 定义BufferedReader输入流来读取URL的响应
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    if(result==null){
                        result = buffer.toString();
                    }
                } catch (IOException e) {
                    throw new IOException("数据读取异常");
                } finally {
                    if(reader!=null){
                        reader.close();
                    }
                }
                jsonobject = JSONObject.parseObject(result);
            }catch(Exception e){
                log.error(e.getMessage(), e);
            }
        }
        return jsonobject;
    }

    public static void main(String[] args) {

        AccessToken accessToken = JwAccessTokenUtils.getAccessToken(JwParames.corpId,JwParames.secret);//获取token


        /*上传临时性
         *
         */

        String fileType="image";
        String filePath="F:\\upload\\img\\cashprizes\\gh_f268aa85d1c7\\oDltNwWtFEEcFOc7XvnG2YzPglBQ_bm.jpg";
        JwQyMediaUtils.sendMedia(fileType,filePath, accessToken.getAccesstoken());//测试结果{"created_at":1462426106,"media_id":"1cjeJstIy7hU0FPsIEnXAj4QgTZZLVqI0Tjqamd8LzAUuGO6hMREqw-fUgiE2mpgD9Vw8qTWoIyhKa4-KF7PbBQ","type":"image"}


         /*获取临时性
          *
         String media_id="1gbGp35rCKh_CT-Zl-d6HJeLubqyHqHiMevE56o2X6Ba2pxOp9wm-WMcOKwA5vbHezQrV_AiApj2V09KhzCV9yA";
         String accessToken="eJ5FMzxYTeH12awmT33DAVUpPM2_zec8Nmct4N6rSMzE3f9L5ahksQ8dxqE0wjJE";
         JwQyMediaUtils.downloadsource(accessToken,media_id);*/
        /*
         * 上传永久性

        FixSource fixSource=new FixSource();
        fixSource.setAgentid(1);
        MpnewEntity mpnews=new MpnewEntity();

        MpnewArticles mpnewArticles=new MpnewArticles();
        mpnewArticles.setTitle("aaa");
        mpnewArticles.setThumb_media_id("1E2g4VgQjVCSTdVdM8f8B1JRcpk2YPJpjwlOohgMN0TE5MY69gyb-Er7LXlQUGRg9cyGm58cCHw1sn-e15590-g");
        mpnewArticles.setAuthor("张三");
        mpnewArticles.setContent("张三吃西瓜");

        mpnews.setArticles(new MpnewArticles[] {mpnewArticles});
        fixSource.setMpnews(mpnews);
        JwQyMediaUtils.uploadfixsource(fixSource, accessToken.getAccesstoken());//测试结果{"errcode":0,"errmsg":"ok","media_id":"2VJ7_-3M4R-hzDcCXP-LJjebxkbxh0ufqGcS19LSH8yGXh_mJHob2bV8KKZ-sZ5Nu"}
        //第二次：token:2PmTUAutNqjcO7PB75dVT4ozFsr3J33qbV6xp8SF3hqnyiSXC4DLnW26WVrrkImI {"errcode":0,"errmsg":"ok","media_id":"2VJ7_-3M4R-hzDcCXP-LJjRZbfAfIMYlgz16vzqbLIJCQZNQAilAS7K1t30d2UX4v"}

         */

        /*上传其他永久性素材
         *

         String fileType="image";
         String filePath="F:\\upload\\img\\cashprizes\\gh_f268aa85d1c7\\oDltNwWtFEEcFOc7XvnG2YzPglBQ_bm.jpg";
         String agentid="1";//token  qZxor_y_ux21TiGIwqQbUrDlNfSoXBLSkO3oCmaCPoqhEuI2XYd_ZfUbYPCltqd8
         JwQyMediaUtils.sendotherMedia(fileType, filePath, accessToken.getAccesstoken(), agentid);//测试结果{"errcode":0,"errmsg":"ok","media_id":"2RWVnjuNYmfG1e5azwyIlCZ0FK1nZmkF52MqkAsAp_eGvZ7vm_EFWBH_7fK0doAmtgL6dcNI843iB0Jqz3iHQvg"}
         */
         /*删除永久性素材
          *

        String media_id="2RWVnjuNYmfG1e5azwyIlCZ0FK1nZmkF52MqkAsAp_eGvZ7vm_EFWBH_7fK0doAmtgL6dcNI843iB0Jqz3iHQvg";
        String agentid="1";
        String accessToken="qZxor_y_ux21TiGIwqQbUrDlNfSoXBLSkO3oCmaCPoqhEuI2XYd_ZfUbYPCltqd8";
        JwQyMediaUtils.deletefixSource(accessToken, agentid, media_id);//测试结果{"errcode":0,"errmsg":"deleted"}

        */

        /*
         * 修改永久图文素材
         *

        UpdateFixSource updateFixSource=new UpdateFixSource();

        updateFixSource.setAgentid(1);
        updateFixSource.setMedia_id("2VJ7_-3M4R-hzDcCXP-LJjRZbfAfIMYlgz16vzqbLIJCQZNQAilAS7K1t30d2UX4v");

        UpdateFixSourceArticles updateFixSourceArticles=new UpdateFixSourceArticles();

        updateFixSourceArticles.setTitle("小马书籍");
        updateFixSourceArticles.setThumb_media_id("1E2g4VgQjVCSTdVdM8f8B1JRcpk2YPJpjwlOohgMN0TE5MY69gyb-Er7LXlQUGRg9cyGm58cCHw1sn-e15590-g");
        updateFixSourceArticles.setAuthor("小马");
        updateFixSourceArticles.setContent("旅游宝典");

        UpdateFixSourceEntity articles=new UpdateFixSourceEntity();
        articles.setArticles(new UpdateFixSourceArticles[] {updateFixSourceArticles});
        updateFixSource.setMpnews(articles);
        String accessToken="2PmTUAutNqjcO7PB75dVT4ozFsr3J33qbV6xp8SF3hqnyiSXC4DLnW26WVrrkImI";
        String agentid="1";
        JwQyMediaUtils.updateSource(updateFixSource, accessToken, agentid);//测试结果 {"errcode":0,"errmsg":"ok"}
         */

            /* 上传图文消息内的图片
             * String fileType="image";
             String filePath= "F:\\upload\\img\\cashprizes\\gh_f268aa85d1c7\\oDltNwWtFEEcFOc7XvnG2YzPglBQ_bm.jpg";
             JwMediaAPI.sendImageMedia(fileType, filePath, accessToken.getAccesstoken());//{"url":"http://shp.qpic.cn/bizmp/UBykN6XtpLSxK2mNnOcvPD7YthhbqTMK8sttheZGm8KLlpk1LCgFiaQ/"}
            */

    }

}
