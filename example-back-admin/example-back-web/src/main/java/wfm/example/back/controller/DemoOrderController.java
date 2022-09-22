package wfm.example.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.sys.model.DemoOrderCustomer;
import wfm.example.back.sys.model.DemoOrderMain;
import wfm.example.back.sys.model.DemoOrderTicket;
import wfm.example.back.sys.query.QueryGenerator;
import wfm.example.back.sys.service.IDemoOrderCustomerService;
import wfm.example.back.sys.service.IDemoOrderMainService;
import wfm.example.back.sys.service.IDemoOrderTicketService;
import wfm.example.back.vo.DemoOrderMainPage;
import wfm.example.back.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 一对多示例
 * @Author: 吴福明
 */
@RestController
@RequestMapping("/test/orderMain")
@Slf4j
public class DemoOrderController {

    @Autowired
    private IDemoOrderMainService orderMainService;
    @Autowired
    private IDemoOrderCustomerService orderCustomerService;
    @Autowired
    private IDemoOrderTicketService orderTicketService;

    /**
     * 分页列表查询
     *
     * @param orderMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(DemoOrderMain orderMain, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<DemoOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(orderMain, req.getParameterMap());
        Page<DemoOrderMain> page = new Page<DemoOrderMain>(pageNo, pageSize);
        IPage<DemoOrderMain> pageList = orderMainService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param orderMainPage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody DemoOrderMainPage orderMainPage) {
        DemoOrderMain orderMain = new DemoOrderMain();
        BeanUtils.copyProperties(orderMainPage, orderMain);
        orderMainService.saveMain(orderMain, orderMainPage.getOrderCustomerList(), orderMainPage.getOrderTicketList());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param orderMainPage
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody DemoOrderMainPage orderMainPage) {
        DemoOrderMain orderMain = new DemoOrderMain();
        BeanUtils.copyProperties(orderMainPage, orderMain);
        orderMainService.updateMain(orderMain, orderMainPage.getOrderCustomerList(), orderMainPage.getOrderTicketList());
        return Result.ok("编辑成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        orderMainService.delMain(id);
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
        this.orderMainService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        DemoOrderMain orderMain = orderMainService.getById(id);
        return Result.ok(orderMain);
    }

    /**
     * 通过id查询
     *
     * @param orderCustomer
     * @return
     */
    @GetMapping(value = "/listOrderCustomerByMainId")
    public Result<?> queryOrderCustomerListByMainId(DemoOrderCustomer orderCustomer,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<DemoOrderCustomer> queryWrapper = QueryGenerator.initQueryWrapper(orderCustomer, req.getParameterMap());
        Page<DemoOrderCustomer> page = new Page<DemoOrderCustomer>(pageNo, pageSize);
        IPage<DemoOrderCustomer> pageList = orderCustomerService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 通过id查询
     *
     * @param orderTicket
     * @return
     */
    @GetMapping(value = "/listOrderTicketByMainId")
    public Result<?> queryOrderTicketListByMainId(DemoOrderTicket orderTicket,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<DemoOrderTicket> queryWrapper = QueryGenerator.initQueryWrapper(orderTicket, req.getParameterMap());
        Page<DemoOrderTicket> page = new Page<DemoOrderTicket>(pageNo, pageSize);
        IPage<DemoOrderTicket> pageList = orderTicketService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param orderCustomer
     * @return
     */
    @PostMapping(value = "/addCustomer")
    public Result<?> addCustomer(@RequestBody DemoOrderCustomer orderCustomer) {
        orderCustomerService.save(orderCustomer);
        return Result.ok("添加成功!");
    }

    /**
     * 编辑
     *
     * @param orderCustomer
     * @return
     */
    @PutMapping("/editCustomer")
    public Result<?> editCustomer(@RequestBody DemoOrderCustomer orderCustomer) {
        orderCustomerService.updateById(orderCustomer);
        return Result.ok("添加成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteCustomer")
    public Result<?> deleteCustomer(@RequestParam(name = "id", required = true) String id) {
        orderCustomerService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatchCustomer")
    public Result<?> deleteBatchCustomer(@RequestParam(name = "ids", required = true) String ids) {
        this.orderCustomerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryOrderCustomerListByMainId")
    public Result<?> queryOrderCustomerListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<DemoOrderCustomer> orderCustomerList = orderCustomerService.selectCustomersByMainId(id);
        return Result.ok(orderCustomerList);
    }

    /**
     * 添加
     *
     * @param orderTicket
     * @return
     */
    @PostMapping(value = "/addTicket")
    public Result<?> addTicket(@RequestBody DemoOrderTicket orderTicket) {
        orderTicketService.save(orderTicket);
        return Result.ok("添加成功!");
    }

    /**
     * 编辑
     *
     * @param orderTicket
     * @return
     */
    @PutMapping("/editTicket")
    public Result<?> editTicket(@RequestBody DemoOrderTicket orderTicket) {
        orderTicketService.updateById(orderTicket);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteTicket")
    public Result<?> deleteTicket(@RequestParam(name = "id", required = true) String id) {
        orderTicketService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatchTicket")
    public Result<?> deleteBatchTicket(@RequestParam(name = "ids", required = true) String ids) {
        this.orderTicketService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryOrderTicketListByMainId")
    public Result<?> queryOrderTicketListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<DemoOrderTicket> orderTicketList = orderTicketService.selectTicketsByMainId(id);
        return Result.ok(orderTicketList);
    }

}

