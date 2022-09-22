package wfm.example.back.common.fastdfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import wfm.example.back.common.dto.FastDFSFileDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * fastdfs客户端
 * @author 吴福明
 */
@Slf4j
public class FastDFSClient {

    private static volatile FastDFSClient instance;

    private FastDFSClient()throws Exception{
        String fastDFSConfig = ClientGlobal.configInfo();
        if (StringUtils.isBlank(fastDFSConfig)){
            throw new Exception("请使用org.csource.fastdfs.ClientGlobal中的init或者initByProperties方法初始化fastDFS");
        }
    }

    public static FastDFSClient getInstance() throws Exception{
        if(instance == null){
            synchronized (FastDFSClient.class){
                if(instance == null){
                    instance = new FastDFSClient();
                }
            }
        }
        return instance;
    }

    /**
     * @Description 上传文件
     * @Param [fastDFSFileEntity]
     * @return java.lang.String[]
     **/
    public String[] upload(FastDFSFileDto file){
        log.info("File Name: " + file.getName() + "File Length:" + file.getContent().length);
        NameValuePair[] metalist=new NameValuePair[1];
        metalist[0]=new NameValuePair("author",file.getAuthor());
        long startTime = System.currentTimeMillis();
        String[] uploadResults= null;
        StorageClient storageClient=null;
        try {
            storageClient=getTrackerClient();
            uploadResults = storageClient.upload_file(file.getContent(),file.getExt(),metalist);
        }catch (IOException e){
            log.error("IO Exception when uploadind the file:"+file.getName(),e);
        }
        catch (Exception e){
            log.error("Non IO Exception when uploadind the file:"+file.getName(),e);
        }
        log.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");
        if(uploadResults==null && storageClient!=null){
            log.error("upload file fail, error code:" + storageClient.getErrorCode());
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];
        log.info("upload file successfully!!!" + "group_name:" + groupName + ", remoteFileName:" + " " + remoteFileName);
        return uploadResults;
    }

    public FileInfo getFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getTrackerClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            log.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    public InputStream downFileWithOutToken(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getTrackerClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (IOException e) {
            log.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    public String getTokenUri(String filePath){
        String substring = filePath.substring(filePath.indexOf("/")+1);
        //unix时间戳 以秒为单位
        int ts = (int) (System.currentTimeMillis() / 1000);
        String token = null;
        try {
//            System.out.println(ClientGlobal.getG_secret_key());
            token= ProtoCommon.getToken(substring, ts, ClientGlobal.getG_secret_key());
        } catch (Exception e) {
            log.error("get download token failed", e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(filePath);
        sb.append("?token=").append(token);
        sb.append("&ts=").append(ts);
        return sb.toString();
    }

    /**
     * @Description
     * @Param [remoteFileName]
     * @return int -1 失败 0成功
     **/
    public int deleteFile(String remoteFileName)
            throws Exception {
        StorageClient storageClient = getTrackerClient();
        int i = storageClient.delete_file("group1", remoteFileName);
        log.info("delete file successfully!!!" + i);
        return i;
    }

    public StorageServer[] getStoreStorages(String groupName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getStoreStorages(trackerServer, groupName);
    }

    public ServerInfo[] getFetchStorages(String groupName,
                                         String remoteFileName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    public String getTrackerUrl() throws IOException {
        return "http://"+getTrackerServer().getInetSocketAddress().getHostString()+":"+ClientGlobal.getG_tracker_http_port()+"/";
    }

    /**
     * @Description 获取 StorageClient
     * @Param []
     * @return org.csource.fastdfs.StorageClient
     **/
    private StorageClient getTrackerClient() throws IOException{
        TrackerServer trackerServer=getTrackerServer();
        StorageClient storageClient=new StorageClient(trackerServer,null);
        return storageClient;
    }

    /**
     * @Description 获取 TrackerServer
     * @Param []
     * @return org.csource.fastdfs.TrackerServer
     **/
    private TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

}
