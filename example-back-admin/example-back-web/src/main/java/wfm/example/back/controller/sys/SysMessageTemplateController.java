package wfm.example.back.controller.sys;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.sys.model.SysMessageTemplate;
import wfm.example.back.sys.query.QueryGenerator;
import wfm.example.back.sys.service.ISysMessageService;
import wfm.example.back.sys.service.ISysMessageTemplateService;
import wfm.example.back.common.dto.MessageDTO;
import wfm.example.back.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * 消息模板controller
 * @author: 吴福明
 */
@Slf4j
@RestController
@RequestMapping("/message/sysMessageTemplate")
public class SysMessageTemplateController {

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    @Autowired
    private ISysMessageService sysMessageService;

    /**
     * 分页列表查询
     *
     * @param sysMessageTemplate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysMessageTemplate sysMessageTemplate, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysMessageTemplate> queryWrapper = QueryGenerator.initQueryWrapper(sysMessageTemplate, req.getParameterMap());
        Page<SysMessageTemplate> page = new Page<SysMessageTemplate>(pageNo, pageSize);
        IPage<SysMessageTemplate> pageList = sysMessageTemplateService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param sysMessageTemplate
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysMessageTemplate sysMessageTemplate) {
        sysMessageTemplateService.save(sysMessageTemplate);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysMessageTemplate
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysMessageTemplate sysMessageTemplate) {
        sysMessageTemplateService.updateById(sysMessageTemplate);
        return Result.ok("更新成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysMessageTemplateService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysMessageTemplateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysMessageTemplate sysMessageTemplate = sysMessageTemplateService.getById(id);
        return Result.ok(sysMessageTemplate);
    }

    /**
     * 发送消息
     */
    @PostMapping(value = "/sendMsg")
    public Result<SysMessageTemplate> sendMessage(@RequestBody MessageDTO msgParams) {
        Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
        Map<String, String> map = null;
        try {
            map = (Map<String, String>) JSON.parse(msgParams.getTestData());
        } catch (Exception e) {
            result.error500("解析Json出错！");
            return result;
        }
        boolean is_sendSuccess = sysMessageService.sendMessage(msgParams.getMsgType(), msgParams.getTemplateCode(), map, msgParams.getReceiver());
        if (is_sendSuccess) {
            result.success("发送消息任务添加成功！");
        } else {
            result.error500("发送消息任务添加失败！");
        }
        return result;
    }
}
