package wfm.example.back.controller.sys;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import wfm.example.back.model.SysDict;
import wfm.example.back.query.QueryGenerator;
import wfm.example.back.service.ISysDictItemService;
import wfm.example.back.service.ISysDictService;
import wfm.example.back.vo.DictQueryVo;
import wfm.example.back.vo.SysDictVo;
import wfm.example.common.constant.CacheConstant;
import wfm.example.common.constant.CommonConstant;
import wfm.example.common.dto.DictDTO;
import wfm.example.common.dto.TreeSelectDTO;
import wfm.example.common.util.ObjectConvertUtils;
import wfm.example.common.util.SqlInjectionUtil;
import wfm.example.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 字典表 前端控制器
 * @author 吴福明
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
public class SysDictController {

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private ISysDictItemService sysDictItemService;

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysDict>> queryPageList(SysDict sysDict, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysDict>> result = new Result<IPage<SysDict>>();
        QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
        Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
        IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
        log.debug("查询当前页："+pageList.getCurrent());
        log.debug("查询当前页数量："+pageList.getSize());
        log.debug("查询结果数量："+pageList.getRecords().size());
        log.debug("数据总数："+pageList.getTotal());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @功能：获取树形字典数据
     * @param sysDict
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    public Result<List<SysDictVo>> treeList(SysDict sysDict, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<List<SysDictVo>> result = new Result<>();
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        // 构造查询条件
        String dictName = sysDict.getDictName();
        if(ObjectConvertUtils.isNotEmpty(dictName)) {
            query.like(true, SysDict::getDictName, dictName);
        }
        query.orderByDesc(true, SysDict::getCreateTime);
        List<SysDict> list = sysDictService.list(query);
        List<SysDictVo> treeList = new ArrayList<>();
        for (SysDict node : list) {
            treeList.add(new SysDictVo(node));
        }
        result.setSuccess(true);
        result.setResult(treeList);
        return result;
    }

    /**
     * 获取字典数据
     * @param dictCode 字典code
     * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
     * @return
     */
    @RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
    public Result<List<DictDTO>> getDictItems(@PathVariable String dictCode, @RequestParam(value = "sign",required = false) String sign, HttpServletRequest request) {
        log.info(" dictCode : "+ dictCode);
        Result<List<DictDTO>> result = new Result<List<DictDTO>>();
        List<DictDTO> ls = null;
        try {
            if(dictCode.indexOf(",")!=-1) {
                //关联表字典（举例：sys_user,realname,id）
                String[] params = dictCode.split(",");

                if(params.length<3) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                //SQL注入校验（只限制非法串改数据库）
                final String[] sqlInjCheck = {params[0],params[1],params[2]};
                SqlInjectionUtil.filterContent(sqlInjCheck);

                if(params.length==4) {
                    //SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
                    SqlInjectionUtil.specialFilterContent(params[3]);
                    ls = sysDictService.queryTableDictItemsByCodeAndFilter(params[0],params[1],params[2],params[3]);
                }else if (params.length==3) {
                    ls = sysDictService.queryTableDictItemsByCode(params[0],params[1],params[2]);
                }else{
                    result.error500("字典Code格式不正确！");
                    return result;
                }
            }else {
                //字典表
                ls = sysDictService.queryDictItemsByCode(dictCode);
            }

            result.setSuccess(true);
            result.setResult(ls);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

    /**
     * 获取全部字典数据
     *
     * @return
     */
    @RequestMapping(value = "/queryAllDictItems", method = RequestMethod.GET)
    public Result<?> queryAllDictItems(HttpServletRequest request) {
        Map<String, List<DictDTO>> res = new HashMap<String, List<DictDTO>>();
        res = sysDictService.queryAllDictItems();
        return Result.ok(res);
    }

    /**
     * 获取字典数据
     * @param dictCode
     * @return
     */
    @RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
    public Result<String> getDictText(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
        log.info(" dictCode : "+ dictCode);
        Result<String> result = new Result<String>();
        String text = null;
        try {
            text = sysDictService.queryDictTextByKey(dictCode, key);
            result.setSuccess(true);
            result.setResult(text);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
            return result;
        }
        return result;
    }

    /**
     * 大数据量的字典表 走异步加载  即前端输入内容过滤数据
     * @param dictCode
     * @return
     */
    @RequestMapping(value = "/loadDict/{dictCode}", method = RequestMethod.GET)
    public Result<List<DictDTO>> loadDict(@PathVariable String dictCode,@RequestParam(name="keyword") String keyword, @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request) {
        log.info(" 加载字典表数据,加载关键字: "+ keyword);
        Result<List<DictDTO>> result = new Result<List<DictDTO>>();
        List<DictDTO> ls = null;
        try {
            if(dictCode.indexOf(",")!=-1) {
                String[] params = dictCode.split(",");
                if(params.length!=3) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                ls = sysDictService.queryTableDictItems(params[0],params[1],params[2],keyword);
                result.setSuccess(true);
                result.setResult(ls);
                log.info(result.toString());
            }else {
                result.error500("字典Code格式不正确！");
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

    /**
     * 根据字典code加载字典text 返回
     */
    @RequestMapping(value = "/loadDictItem/{dictCode}", method = RequestMethod.GET)
    public Result<List<String>> loadDictItem(@PathVariable String dictCode,@RequestParam(name="key") String keys, @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request) {
        Result<List<String>> result = new Result<>();
        try {
            if(dictCode.indexOf(",")!=-1) {
                String[] params = dictCode.split(",");
                if(params.length!=3) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                List<String> texts = sysDictService.queryTableDictByKeys(params[0], params[1], params[2], keys);

                result.setSuccess(true);
                result.setResult(texts);
                log.info(result.toString());
            }else {
                result.error500("字典Code格式不正确！");
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

    /**
     * 根据表名——显示字段-存储字段 pid 加载树形数据
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/loadTreeData", method = RequestMethod.GET)
    public Result<List<TreeSelectDTO>> loadTreeData(@RequestParam(name="pid") String pid, @RequestParam(name="pidField") String pidField,
                                                    @RequestParam(name="tableName") String tbname,
                                                    @RequestParam(name="text") String text,
                                                    @RequestParam(name="code") String code,
                                                    @RequestParam(name="hasChildField") String hasChildField,
                                                    @RequestParam(name="condition") String condition,
                                                    @RequestParam(value = "sign",required = false) String sign, HttpServletRequest request) {
        Result<List<TreeSelectDTO>> result = new Result<List<TreeSelectDTO>>();
        Map<String, String> query = null;
        if(ObjectConvertUtils.isNotEmpty(condition)) {
            query = JSON.parseObject(condition, Map.class);
        }
        // SQL注入漏洞 sign签名校验(表名,label字段,val字段,条件)
        String dictCode = tbname+","+text+","+code+","+condition;
        List<TreeSelectDTO> ls = sysDictService.queryTreeList(query,tbname, text, code, pidField, pid,hasChildField);
        result.setSuccess(true);
        result.setResult(ls);
        return result;
    }

    /**
     * 【APP接口】根据字典配置查询表字典数据
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/queryTableData")
    public Result<List<DictDTO>> queryTableData(DictQueryVo query,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                @RequestParam(value = "sign",required = false) String sign, HttpServletRequest request){
        Result<List<DictDTO>> res = new Result<List<DictDTO>>();
        // SQL注入漏洞 sign签名校验
        String dictCode = query.getTable()+","+query.getText()+","+query.getCode();
        List<DictDTO> ls = this.sysDictService.queryDictTablePageList(query,pageSize,pageNo);
        res.setResult(ls);
        res.setSuccess(true);
        return res;
    }

    /**
     * @功能：新增
     * @param sysDict
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysDict> add(@RequestBody SysDict sysDict) {
        Result<SysDict> result = new Result<SysDict>();
        try {
            sysDict.setCreateTime(new Date());
            sysDict.setDelFlag(CommonConstant.DEL_FLAG_0);
            sysDictService.save(sysDict);
            result.success("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @功能：编辑
     * @param sysDict
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<SysDict> edit(@RequestBody SysDict sysDict) {
        Result<SysDict> result = new Result<SysDict>();
        SysDict sysdict = sysDictService.getById(sysDict.getId());
        if(sysdict==null) {
            result.error500("未找到对应实体");
        }else {
            sysDict.setUpdateTime(new Date());
            boolean ok = sysDictService.updateById(sysDict);
            if(ok) {
                result.success("编辑成功!");
            }
        }
        return result;
    }

    /**
     * @功能：删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries=true)
    public Result<SysDict> delete(@RequestParam(name="id",required=true) String id) {
        Result<SysDict> result = new Result<SysDict>();
        boolean ok = sysDictService.removeById(id);
        if(ok) {
            result.success("删除成功!");
        }else{
            result.error500("删除失败!");
        }
        return result;
    }

    /**
     * @功能：批量删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @CacheEvict(value= CacheConstant.SYS_DICT_CACHE, allEntries=true)
    public Result<SysDict> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        Result<SysDict> result = new Result<SysDict>();
        if(ObjectConvertUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        }else {
            sysDictService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * @功能：刷新缓存
     * @return
     */
    @RequestMapping(value = "/refleshCache")
    public Result<?> refleshCache() {
        Result<?> result = new Result<SysDict>();
        //清空字典缓存
        Set keys = redisTemplate.keys(CacheConstant.SYS_DICT_CACHE + "*");
        Set keys2 = redisTemplate.keys(CacheConstant.SYS_DICT_TABLE_CACHE + "*");
        Set keys3 = redisTemplate.keys(CacheConstant.SYS_DEPARTS_CACHE + "*");
        Set keys4 = redisTemplate.keys(CacheConstant.SYS_DEPART_IDS_CACHE + "*");
        redisTemplate.delete(keys);
        redisTemplate.delete(keys2);
        redisTemplate.delete(keys3);
        redisTemplate.delete(keys4);
        return result;
    }

    /**
     * 查询被删除的列表
     * @return
     */
    @RequestMapping(value = "/deleteList", method = RequestMethod.GET)
    public Result<List<SysDict>> deleteList() {
        Result<List<SysDict>> result = new Result<List<SysDict>>();
        List<SysDict> list = this.sysDictService.queryDeleteList();
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    /**
     * 物理删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletePhysic/{id}", method = RequestMethod.DELETE)
    public Result<?> deletePhysic(@PathVariable String id) {
        try {
            sysDictService.deleteOneDictPhysically(id);
            return Result.ok("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败!");
        }
    }

    /**
     * 取回
     * @param id
     * @return
     */
    @RequestMapping(value = "/back/{id}", method = RequestMethod.PUT)
    public Result<?> back(@PathVariable String id) {
        try {
            sysDictService.updateDictDelFlag(0,id);
            return Result.ok("操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败!");
        }
    }

}
