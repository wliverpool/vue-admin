package wfm.example.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.model.SysMessage;
import wfm.example.back.query.QueryGenerator;
import wfm.example.back.service.ISysMessageService;
import wfm.example.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 消息controller
 * @author: 吴福明
 */
@Slf4j
@RestController
@RequestMapping("/message/sysMessage")
public class SysMessageController {

    @Autowired
    private ISysMessageService sysMessageService;

    /**
     * 分页列表查询
     *
     * @param sysMessage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysMessage sysMessage, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysMessage> queryWrapper = QueryGenerator.initQueryWrapper(sysMessage, req.getParameterMap());
        Page<SysMessage> page = new Page<SysMessage>(pageNo, pageSize);
        IPage<SysMessage> pageList = sysMessageService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param sysMessage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysMessage sysMessage) {
        sysMessageService.save(sysMessage);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysMessage
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysMessage sysMessage) {
        sysMessageService.updateById(sysMessage);
        return Result.ok("修改成功!");

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysMessageService.removeById(id);
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

        this.sysMessageService.removeByIds(Arrays.asList(ids.split(",")));
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
        SysMessage sysMessage = sysMessageService.getById(id);
        return Result.ok(sysMessage);
    }

}
