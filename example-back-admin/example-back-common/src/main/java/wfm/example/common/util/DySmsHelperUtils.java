package wfm.example.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import wfm.example.common.xml.DownNoteXML;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 短信帮助类
 * @author 吴福明
 */
@Slf4j
public class DySmsHelperUtils {

    /**
     * 发送短信
     * @param ip
     * @param port
     * @param comment
     * @param telephone
     * @param templateNo
     * @return
     */
    public static boolean sendSms(String ip,Integer port,String comment,String telephone,String templateNo){
        String responseMessage="";
        Socket socket =null;
        InputStream is =null;
        DataInputStream dataIs = null;
        try {
            socket = new Socket(ip,port);
            // 向服务器端发送数据
            OutputStream os =  socket.getOutputStream();
            DataOutputStream bos = new DataOutputStream(os);
            DownNoteXML downNoteXML = new DownNoteXML();
            //短信即刻发
            downNoteXML.setSmsType("0");
            //客户手机号码
            downNoteXML.setMobile(telephone);
            //短信模版号
            downNoteXML.setSms(templateNo);
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nowDate = dateFormat.format(now);
            downNoteXML.setRequestTime(nowDate);
            if(StringUtils.isNotBlank(comment)){
                downNoteXML.setCv(comment);
            }
            String xml = JaxbUtils.convertToXml(downNoteXML).trim();
            byte [] content = xml.getBytes("UTF-8");
            int len = content.length;
            String str = JaxbUtils.codeAddOne(String.valueOf(len), 4);
            //拼接报文，最前是长度
            String con = str + new String(content,"UTF-8");
            //转换编码
            log.info("报文内容"+con);
            bos.write(con.getBytes("UTF-8"));
            bos.flush();
            //接收服务器端数据
            is = socket.getInputStream();
            dataIs = new DataInputStream(is);
            byte[] header = new byte[4];
            dataIs.read(header);
            int msgLen = Integer.parseInt(new String(header));
            byte[] body = new byte[msgLen];
            dataIs.read(body);
            responseMessage = new String(body);
            log.info("收到的回复"+responseMessage);
            return true;
        } catch (UnknownHostException e) {
            log.error(e.getMessage(),e);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally{
            try {
                if(dataIs!=null){
                    dataIs.close();
                }
                if(is!=null){
                    is.close();
                }
                if(socket!=null){
                    socket.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        return false;
    }


}
