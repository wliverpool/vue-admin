package wfm.example.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wfm.example.back.model.SysFile;
import wfm.example.back.query.QueryGenerator;
import wfm.example.back.service.ISysFileService;
import wfm.example.common.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传controller
 * @author 吴福明
 */

@Slf4j
@Controller
@RequestMapping("/sys/file")
public class SysFileController {

    @Autowired
    private ISysFileService SysFileService;

    @ResponseBody
    @GetMapping("/list")
    public Result<IPage<SysFile>> queryPageList(SysFile file,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysFile>> result = new Result<>();
        QueryWrapper<SysFile> queryWrapper = QueryGenerator.initQueryWrapper(file, req.getParameterMap());
        Page<SysFile> page = new Page<>(pageNo, pageSize);
        IPage<SysFile> pageList = SysFileService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @ResponseBody
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile) {
        Result result = new Result();
        try {
            SysFileService.upload(multipartFile);
            result.success("上传成功！");
        }
        catch (Exception ex) {
            log.info(ex.getMessage(), ex);
            result.error500("上传失败");
        }
        return result;
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public Result delete(@RequestParam(name = "id") String id) {
        Result result = new Result();
        SysFile file = SysFileService.getById(id);
        if (file == null) {
            result.error500("未找到对应实体");
        }
        else {
            boolean ok = SysFileService.delete(file);
            if (ok) {
                result.success("删除成功!");
            }
        }
        return result;
    }

    /**
     * 通过id查询.
     */
    @ResponseBody
    @GetMapping("/queryById")
    public Result<SysFile> queryById(@RequestParam(name = "id") String id) {
        Result<SysFile> result = new Result<>();
        SysFile file = SysFileService.getById(id);
        if (file == null) {
            result.error500("未找到对应实体");
        }
        else {
            result.setResult(file);
            result.setSuccess(true);
        }
        return result;
    }

}
