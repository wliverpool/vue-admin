package wfm.example.back.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.sys.model.SysUserAgent;
import wfm.example.back.sys.query.QueryGenerator;
import wfm.example.back.sys.service.ISysUserAgentService;
import wfm.example.back.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 用户代理人设置
 * @author: 吴福明
 */
@RestController
@RequestMapping("/sys/sysUserAgent")
@Slf4j
public class SysUserAgentController {

    @Autowired
    private ISysUserAgentService sysUserAgentService;

    /**
     * 分页列表查询
     * @param sysUserAgent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<SysUserAgent>> queryPageList(SysUserAgent sysUserAgent,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest req) {
        Result<IPage<SysUserAgent>> result = new Result<IPage<SysUserAgent>>();
        QueryWrapper<SysUserAgent> queryWrapper = QueryGenerator.initQueryWrapper(sysUserAgent, req.getParameterMap());
        Page<SysUserAgent> page = new Page<SysUserAgent>(pageNo, pageSize);
        IPage<SysUserAgent> pageList = sysUserAgentService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     *   添加
     * @param sysUserAgent
     * @return
     */
    @PostMapping(value = "/add")
    public Result<SysUserAgent> add(@RequestBody SysUserAgent sysUserAgent) {
        Result<SysUserAgent> result = new Result<SysUserAgent>();
        try {
            sysUserAgentService.save(sysUserAgent);
            result.success("代理人设置成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     *  编辑
     * @param sysUserAgent
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<SysUserAgent> edit(@RequestBody SysUserAgent sysUserAgent) {
        Result<SysUserAgent> result = new Result<SysUserAgent>();
        SysUserAgent sysUserAgentEntity = sysUserAgentService.getById(sysUserAgent.getId());
        if(sysUserAgentEntity==null) {
            result.error500("未找到对应实体");
        }else {
            boolean ok = sysUserAgentService.updateById(sysUserAgent);
            //TODO 返回false说明什么？
            if(ok) {
                result.success("代理人设置成功!");
            }
        }

        return result;
    }

    /**
     *   通过id删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<SysUserAgent> delete(@RequestParam(name="id",required=true) String id) {
        Result<SysUserAgent> result = new Result<SysUserAgent>();
        SysUserAgent sysUserAgent = sysUserAgentService.getById(id);
        if(sysUserAgent==null) {
            result.error500("未找到对应实体");
        }else {
            boolean ok = sysUserAgentService.removeById(id);
            if(ok) {
                result.success("删除成功!");
            }
        }

        return result;
    }

    /**
     *  批量删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysUserAgent> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        Result<SysUserAgent> result = new Result<SysUserAgent>();
        if(ids==null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        }else {
            this.sysUserAgentService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<SysUserAgent> queryById(@RequestParam(name="id",required=true) String id) {
        Result<SysUserAgent> result = new Result<SysUserAgent>();
        SysUserAgent sysUserAgent = sysUserAgentService.getById(id);
        if(sysUserAgent==null) {
            result.error500("未找到对应实体");
        }else {
            result.setResult(sysUserAgent);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 通过userName查询
     * @param userName
     * @return
     */
    @GetMapping(value = "/queryByUserName")
    public Result<SysUserAgent> queryByUserName(@RequestParam(name="userName",required=true) String userName) {
        Result<SysUserAgent> result = new Result<SysUserAgent>();
        LambdaQueryWrapper<SysUserAgent> queryWrapper = new LambdaQueryWrapper<SysUserAgent>();
        queryWrapper.eq(SysUserAgent::getUserName, userName);
        SysUserAgent sysUserAgent = sysUserAgentService.getOne(queryWrapper);
        if(sysUserAgent==null) {
            result.error500("未找到对应实体");
        }else {
            result.setResult(sysUserAgent);
            result.setSuccess(true);
        }
        return result;
    }

}
