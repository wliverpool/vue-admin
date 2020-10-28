package wfm.example.back.service.impl;

import cn.hutool.core.io.FileTypeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wfm.example.back.mapper.SysFileMapper;
import wfm.example.back.model.SysFile;
import wfm.example.back.service.ISysFileService;
import wfm.example.common.dto.FastDFSFileDto;
import wfm.example.common.fastdfs.FastDFSClient;

import java.io.File;

/**
 * 文件上传无服务实现类
 * @author 吴福明
 */

@Service
@Slf4j
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    @Value("${system.upload.fast-dfs.http.browse-base-url}")
    private String fastDFSBrowseBaseUrl;

    @Override
    public void upload(MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        File uploadFile = new File(fileName);
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), uploadFile);
        String fileType = FileTypeUtil.getType(uploadFile);
        byte[] fileBuff = multipartFile.getBytes();
        FastDFSFileDto fastDFSFileDto = new FastDFSFileDto(fileName,fileBuff,fileType);
        String[] fileAbsolutePath = FastDFSClient.getInstance().upload(fastDFSFileDto);
        String savePath = fileAbsolutePath[0]+ "/" + fileAbsolutePath[1];
        log.info("上传到fastDFS上的路径：" + savePath + ",类型为:" + fileType);
        SysFile ossFile = new SysFile();
        ossFile.setFileName(fileName);
        ossFile.setUrl(savePath);
        this.save(ossFile);
    }

    @Override
    public boolean delete(SysFile sysFile) {
        try {
            this.removeById(sysFile.getId());
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }

}