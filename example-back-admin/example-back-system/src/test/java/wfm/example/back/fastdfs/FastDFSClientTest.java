package wfm.example.back.fastdfs;

import cn.hutool.core.io.FileTypeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wfm.example.common.dto.FastDFSFileDto;
import wfm.example.common.fastdfs.FastDFSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FastDFSClientTest {

    @Test
    public void testFastDFSUpload(){
        String fileName = "test.jpeg";
        byte[] file_buff=null;
        InputStream inputStream = null;
        try {
            File pngFile = new File("/Users/wufuming/Desktop/WechatIMG737.jpeg");
            inputStream = new FileInputStream(pngFile);
            if(inputStream!=null){
                int available = inputStream.available();
                file_buff=new byte[available];
                inputStream.read(file_buff);
            }
            FastDFSFileDto fastDFSFileDto = new FastDFSFileDto(fileName,file_buff,"jpeg");
            String[] fileAbsolutePath = FastDFSClient.getInstance().upload(fastDFSFileDto);
            String path=fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
            System.out.println("路径：" + path);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testGetDownloadToken()throws Exception{
        //group1/M00/00/00/CiDJJF5EqE-ALJE7ADPeIkSs2yE182.pdf
        //group1/M00/00/00/CiDJHV5Erz6AUrTZAACahFoLMEU61.docx
        String uri = FastDFSClient.getInstance().getTokenUri("group1/M00/00/05/CiDJJF-JZDWABAvEAAEa4al9AeM39.jpeg");
        System.out.println("uri:" + uri);
        System.out.println(URLEncoder.encode(uri,"UTF-8"));
        String fileName = uri.substring(uri.lastIndexOf("/") + 1,uri.indexOf("?"));
        System.out.println(fileName);
    }

    @Test
    public void testFileType() throws Exception{
        File file = new File("/Users/wufuming/Desktop/阿里Sentinel限流简介.md");
        System.out.println(FileTypeUtil.getType(file));
    }

}
