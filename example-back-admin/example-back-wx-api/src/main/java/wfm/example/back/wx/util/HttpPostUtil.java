package wfm.example.back.wx.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class HttpPostUtil {


    URL url;
    HttpURLConnection conn;
    String boundary = "--------httppost123";
    Map<String, String> textParams = new HashMap<String, String>();
    Map<String, File> fileparams = new HashMap<String, File>();
    DataOutputStream ds;

    //图片类型常量--
    public static final String IMG_JPG = ".jpg";
    public static final String IMG_PNG = ".png";
    public static final String IMG_GIF = ".gif";
    public static final String IMG_BMP = ".bmp";

    public HttpPostUtil(String url) throws Exception {
        this.url = new URL(url);
    }

    /**
     * 重新设置要请求的服务器地址，即上传文件的地址。
     * @param url
     * @throws Exception
     */
    public void setUrl(String url) throws Exception {
        this.url = new URL(url);
    }

    /**
     * 增加一个普通字符串数据到form表单数据中
     * @param name
     * @param value
     */
    public void addTextParameter(String name, String value) {
        textParams.put(name, value);
    }

    /**
     * 增加一个文件到form表单数据中
     * @param name
     * @param value
     */
    public void addFileParameter(String name, File value) {
        fileparams.put(name, value);
    }

    /**
     * 清空所有已添加的form表单数据
     */
    public void clearAllParameters() {
        textParams.clear();
        fileparams.clear();
    }

    /**
     * 发送数据到服务器，返回一个字节包含服务器的返回结果的数组
     * @return
     * @throws Exception
     */
    public byte[] send() throws Exception {
        initConnection();
        try {
            conn.connect();
        } catch (SocketTimeoutException e) {
            // something
            throw new RuntimeException();
        }
        ds = new DataOutputStream(conn.getOutputStream());
        writeFileParams();
        writeStringParams();
        paramsEnd();
        InputStream in = conn.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        conn.disconnect();
        return out.toByteArray();
    }

    /**
     * 文件上传的connection的一些必须设置
     * @throws Exception
     */
    private void initConnection() throws Exception {
        conn = (HttpURLConnection) this.url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(10000); //连接超时为10秒
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
    }

    /**
     * 普通字符串数据
     * @throws Exception
     */
    private void writeStringParams() throws Exception {
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String name = it.next();
            String value = textParams.get(name);
            ds.writeBytes("--" + boundary + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name
                    + "\"\r\n");
            ds.writeBytes("\r\n");
            ds.writeBytes(encode(value) + "\r\n");
        }
    }

    /**
     * 文件数据
     * @throws Exception
     */
    private void writeFileParams() throws Exception {
        Set<String> keySet = fileparams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            //update-begin-author:taoYan date:20180601 for:文件加入http请求，当文件非本地资源的时候需要作特殊处理--
            String name = it.next();
            File value = fileparams.get(name);
            String valuename = value.getName();
            if(value.exists()){
                ds.writeBytes("--" + boundary + "\r\n");
                ds.writeBytes("Content-Disposition: form-data; name=\"" + name
                        + "\"; filename=\"" + encode(valuename) + "\"\r\n");
                ds.writeBytes("Content-Type: " + getContentType(value) + "\r\n");
                ds.writeBytes("\r\n");
                ds.write(getBytes(value));
            }else{
                String myFilePath = value.getPath();
                if(myFilePath!=null && myFilePath.startsWith("http")){
                    byte[] netFileBytes =  getURIFileBytes(myFilePath);
                    String lowerValueName = valuename.toLowerCase();
                    if(lowerValueName.endsWith(IMG_BMP)||lowerValueName.endsWith(IMG_GIF)||lowerValueName.endsWith(IMG_JPG)||lowerValueName.endsWith(IMG_PNG)){
                        valuename = encode(valuename);
                    }else{
                        valuename = System.currentTimeMillis()+getPicType(netFileBytes);
                    }
                    ds.writeBytes("--" + boundary + "\r\n");
                    ds.writeBytes("Content-Disposition: form-data; name=\"" + name
                            + "\"; filename=\"" + valuename + "\"\r\n");
                    ds.writeBytes("Content-Type: " + getContentType(value) + "\r\n");
                    ds.writeBytes("\r\n");
                    ds.write(netFileBytes);
                }
            }
            //update-end-author:taoYan date:20180601 for:文件加入http请求，当文件非本地资源的时候需要作特殊处理--
            ds.writeBytes("\r\n");
        }
    }

    /**
     * 通过文件的网络地址转化成流再读到字节数组中去
     */
    private byte[] getURIFileBytes(String url) throws IOException{
        url = url.replace("http:"+File.separator,"http://").replace("\\","/");
        URL oracle = new URL(url);
        InputStream inStream = oracle.openStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
     * @param f
     * @return
     * @throws Exception
     */
    private String getContentType(File f) throws Exception {

//		return "application/octet-stream";  // 此行不再细分是否为图片，全部作为application/octet-stream 类型
        ImageInputStream imagein = ImageIO.createImageInputStream(f);
        if (imagein == null) {
            return "application/octet-stream";
        }
        Iterator<ImageReader> it = ImageIO.getImageReaders(imagein);
        if (!it.hasNext()) {
            imagein.close();
            return "application/octet-stream";
        }
        imagein.close();
        return "image/" + it.next().getFormatName().toLowerCase();//将FormatName返回的值转换成小写，默认为大写

    }

    /**
     * 把文件转换成字节数组
     * @param f
     * @return
     * @throws Exception
     */
    private byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        return out.toByteArray();
    }

    /**
     * 添加结尾数据
     * @throws Exception
     */
    private void paramsEnd() throws Exception {
        ds.writeBytes("--" + boundary + "--" + "\r\n");
        ds.writeBytes("\r\n");
    }

    /**
     * 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
     * @param value
     * @return
     * @throws Exception
     */
    private String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }

    /**
     * 根据文件流判断图片类型
     * @param src
     * @return jpg/png/gif/bmp
     */
    public String getPicType(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < 4; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        String type = stringBuilder.toString().toUpperCase();
        if (type.contains("FFD8FF")) {
            return IMG_JPG;
        } else if (type.contains("89504E47")) {
            return IMG_PNG;
        } else if (type.contains("47494638")) {
            return IMG_GIF;
        } else if (type.contains("424D")) {
            return IMG_BMP;
        }else{
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        HttpPostUtil u = new HttpPostUtil("https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=i3um002Np_n-mgNVbPP9JEIfft7_hRq3eHE86slxI7Uh_5q0K5rFfLRnhD20HTCcFt92ulWnndpGlyiNgXi6UiWQqKxPCBsfYKmiY6Ws-isUVLaAFAXYO");
        u.addFileParameter("img", new File("C:/Users/zhangdaihao/Desktop/2.png"));
        byte[] b = u.send();
        String result = new String(b);
        System.out.println(result);

    }

}