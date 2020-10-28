package wfm.example.back.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;
import wfm.example.back.service.ISysBaseAPI;
import wfm.example.common.constant.CommonConstant;
import wfm.example.common.dto.FastDFSFileDto;
import wfm.example.common.fastdfs.FastDFSClient;
import wfm.example.common.util.ObjectConvertUtils;
import wfm.example.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

/**
 * 通用功能controller
 * @Author 吴福明
 */

@Slf4j
@RestController
@RequestMapping("/sys/common")
public class CommonController {

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Value("${system.upload.fast-dfs.http.browse-base-url}")
    private String fastDFSBrowseBaseUrl;


    /**
     * 文件上传统一方法
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/upload")
    public Result<?> upload(HttpServletRequest request, HttpServletResponse response)throws Exception {
        Result<?> result = new Result<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取上传文件对象
        MultipartFile file = multipartRequest.getFile("file");
        String fileName = file.getOriginalFilename();
        byte[] fileBuff = file.getBytes();
        FastDFSFileDto fastDFSFileDto = new FastDFSFileDto(fileName,fileBuff,"jpeg");
        String[] fileAbsolutePath = FastDFSClient.getInstance().upload(fastDFSFileDto);
        String savePath = fileAbsolutePath[0]+ "/" + fileAbsolutePath[1];
        log.info("上传到fastDFS上的路径：" + savePath);

        if(ObjectConvertUtils.isNotEmpty(savePath)){
            result.setMessage(savePath);
            result.setSuccess(true);
        }else {
            result.setMessage("上传失败！");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 预览图片&下载文件
     * 请求地址：http://localhost:8080/common/static/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/static/**")
    public void view(HttpServletRequest request, HttpServletResponse response) {
        // ISO-8859-1 ==> UTF-8 进行编码转换
        String imgPath = extractPathFromPattern(request);
        if(ObjectConvertUtils.isEmpty(imgPath) || imgPath == "null"){
            return;
        }
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String tokenUrl = FastDFSClient.getInstance().getTokenUri(imgPath);
            String imgUrl = fastDFSBrowseBaseUrl + "/" + tokenUrl;
            log.info("上传文件的下载路径:" + imgUrl);
            String fileName = tokenUrl.substring(tokenUrl.lastIndexOf("/") + 1,tokenUrl.indexOf("?"));
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
            URL url = new URL(imgUrl);
            inputStream = url.openStream();
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (Exception e) {
            log.error("预览文件失败" + e.getMessage(), e);
            response.setStatus(404);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     *  把指定URL后的字符串全部截断当成参数
     *  这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
     * @param request
     * @return
     */
    private static String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
    
}
