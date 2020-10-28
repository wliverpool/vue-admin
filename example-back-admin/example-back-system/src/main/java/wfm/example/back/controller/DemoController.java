package wfm.example.back.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.model.Demo;
import wfm.example.back.query.QueryGenerator;
import wfm.example.back.redis.RedisDao;
import wfm.example.back.service.IDemoService;
import wfm.example.common.aspect.PermissionData;
import wfm.example.common.util.DateUtils;
import wfm.example.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * demo controller
 * @Author: 吴福明
 */
@Slf4j
@RestController
@RequestMapping("/test/demo")
public class DemoController {

    @Autowired
    private IDemoService demoService;

    @Autowired
    private RedisDao redisDao;

    /**
     * 分页列表查询
     *
     * @param demo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "demo/DemoList")
    public Result<?> list(Demo demo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<Demo> queryWrapper = QueryGenerator.initQueryWrapper(demo, req.getParameterMap());
        Page<Demo> page = new Page<Demo>(pageNo, pageSize);

        IPage<Demo> pageList = demoService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param demo
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody Demo demo) {
        demoService.save(demo);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param demo
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Demo demo) {
        demoService.updateById(demo);
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
        demoService.removeById(id);
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
        this.demoService.removeByIds(Arrays.asList(ids.split(",")));
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
        Demo demo = demoService.getById(id);
        return Result.ok(demo);
    }

    // =====Redis 示例===============================================================================================

    /**
     * redis操作 -- set
     */
    @GetMapping(value = "/redisSet")
    public void redisSet() {
        redisDao.set("name", "张三" + DateUtils.now());
    }

    /**
     * redis操作 -- get
     */
    @GetMapping(value = "/redisGet")
    public String redisGet() {
        return (String) redisDao.get("name");
    }

    /**
     * redis操作 -- setObj
     */
    @GetMapping(value = "/redisSetObj")
    public void redisSetObj() {
        Demo p = new Demo();
        p.setAge(10);
        p.setBirthday(new Date());
        p.setContent("hello");
        p.setName("张三");
        p.setSex("男");
        redisDao.set("user-zdh", p);
    }

    /**
     * redis操作 -- setObj
     */
    @GetMapping(value = "/redisGetObj")
    public Object redisGetObj() {
        return redisDao.get("user-zdh");
    }

    /**
     * redis操作 -- get
     */
    @GetMapping(value = "/redis/{id}")
    public Demo redisGetDemo(@PathVariable("id") String id) {
        Demo t = demoService.getByIdCacheable(id);
        log.info(t.toString());
        return t;
    }


    // ==========================================动态表单 JSON接收测试===========================================
    @PostMapping(value = "/testOnlineAdd")
    public Result<?> testOnlineAdd(@RequestBody JSONObject json) {
        log.info(json.toJSONString());
        return Result.ok("添加成功！");
    }

    /*----------------------------------------外部获取权限示例------------------------------------*/

    /**
     * 【数据权限示例 - 编程】mybatisPlus java类方式加载权限
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/mpList")
    @PermissionData(pageComponent = "demo/DemoList")
    public Result<?> loadMpPermissonList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        QueryWrapper<Demo> queryWrapper = new QueryWrapper<Demo>();
        //编程方式，给queryWrapper装载数据权限规则
        QueryGenerator.installAuthMplus(queryWrapper, Demo.class);
        Page<Demo> page = new Page<Demo>(pageNo, pageSize);
        IPage<Demo> pageList = demoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 【数据权限示例 - 编程】mybatis xml方式加载权限
     *
     * @param demo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/sqlList")
    @PermissionData(pageComponent = "demo/DemoList")
    public Result<?> loadSqlPermissonList(Demo demo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {
        IPage<Demo> pageList = demoService.queryListWithPermission(pageSize, pageNo);
        return Result.ok(pageList);
    }
    /*----------------------------------------外部获取权限示例------------------------------------*/

}
