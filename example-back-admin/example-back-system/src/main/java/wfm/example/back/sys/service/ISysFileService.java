package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import wfm.example.back.sys.model.SysFile;

/**
 * 系统文件上传服务
 * @author 吴福明
 */
public interface ISysFileService extends IService<SysFile> {

    void upload(MultipartFile multipartFile) throws Exception;

    boolean delete(SysFile sysFile);

}