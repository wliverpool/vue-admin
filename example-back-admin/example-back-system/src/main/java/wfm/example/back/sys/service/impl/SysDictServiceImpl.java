package wfm.example.back.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import wfm.example.back.sys.mapper.SysDictItemMapper;
import wfm.example.back.sys.mapper.SysDictMapper;
import wfm.example.back.sys.model.SysDict;
import wfm.example.back.sys.model.SysDictItem;
import wfm.example.back.sys.service.ISysDictService;
import wfm.example.back.common.vo.DictQueryVo;
import wfm.example.back.common.constant.CacheConstant;
import wfm.example.back.common.constant.CommonConstant;
import wfm.example.back.common.dto.DictDTO;
import wfm.example.back.common.dto.TreeSelectDTO;
import wfm.example.back.common.util.ObjectConvertUtils;
import wfm.example.back.common.vo.DuplicateCheckVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典表 服务实现类
 * @author 吴福明
 */
@Service
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#code")
    public List<DictDTO> queryDictItemsByCode(String code) {
        log.info("无缓存dictCache的时候调用这里！");
        return sysDictMapper.queryDictItemsByCode(code);
    }

    @Override
    public Map<String, List<DictDTO>> queryAllDictItems() {
        Map<String, List<DictDTO>> res = new HashMap<String, List<DictDTO>>();
        List<SysDict> ls = sysDictMapper.selectList(null);
        LambdaQueryWrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<SysDictItem>();
        queryWrapper.eq(SysDictItem::getStatus, 1);
        queryWrapper.orderByAsc(SysDictItem::getSortOrder);
        List<SysDictItem> sysDictItemList = sysDictItemMapper.selectList(queryWrapper);

        for (SysDict d : ls) {
            List<DictDTO> DictDTOList = sysDictItemList.stream().filter(s -> d.getId().equals(s.getDictId())).map(item -> {
                DictDTO DictDTO = new DictDTO();
                DictDTO.setText(item.getItemText());
                DictDTO.setValue(item.getItemValue());
                return DictDTO;
            }).collect(Collectors.toList());
            res.put(d.getDictCode(), DictDTOList);
        }
        log.info("-------登录加载系统字典-----" + res.toString());
        return res;
    }

    /**
     * 通过查询指定table的 text code 获取字典
     * dictTableCache采用redis缓存有效期10分钟
     * @param table
     * @param text
     * @param code
     * @return
     */
    @Override
    public List<DictDTO> queryTableDictItemsByCode(String table, String text, String code) {
        log.info("无缓存dictTableList的时候调用这里！");
        return sysDictMapper.queryTableDictItemsByCode(table,text,code);
    }

    @Override
    public List<DictDTO> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
        log.info("无缓存dictTableList的时候调用这里！");
        return sysDictMapper.queryTableDictItemsByCodeAndFilter(table,text,code,filterSql);
    }

    /**
     * 通过查询指定code 获取字典值text
     * @param code
     * @param key
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#code+':'+#key")
    public String queryDictTextByKey(String code, String key) {
        log.info("无缓存dictText的时候调用这里！");
        return sysDictMapper.queryDictTextByKey(code, key);
    }

    /**
     * 通过查询指定table的 text code 获取字典值text
     * dictTableCache采用redis缓存有效期10分钟
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public String queryTableDictTextByKey(String table, String text, String code, String key) {
        log.info("无缓存dictTable的时候调用这里！");
        return sysDictMapper.queryTableDictTextByKey(table,text,code,key);
    }

    /**
     * 通过查询指定table的 text code 获取字典，包含text和value
     * dictTableCache采用redis缓存有效期10分钟
     * @param table
     * @param text
     * @param code
     * @param keys (逗号分隔)
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public List<String> queryTableDictByKeys(String table, String text, String code, String keys) {
        if(ObjectConvertUtils.isEmpty(keys)){
            return null;
        }
        String[] keyArray = keys.split(",");
        List<DictDTO> dicts = sysDictMapper.queryTableDictByKeys(table, text, code, keyArray);
        List<String> texts = new ArrayList<>(dicts.size());
        // 查询出来的顺序可能是乱的，需要排个序
        for (String key : keyArray) {
            for (DictDTO dict : dicts) {
                if (key.equals(dict.getValue())) {
                    texts.add(dict.getText());
                    break;
                }
            }
        }
        return texts;
    }

    /**
     * 根据字典类型id删除关联表中其对应的数据
     */
    @Override
    public boolean deleteByDictId(SysDict sysDict) {
        sysDict.setDelFlag(CommonConstant.DEL_FLAG_1);
        return  this.updateById(sysDict);
    }

    @Override
    public Integer saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList) {
        int insert=0;
        try{
            insert = sysDictMapper.insert(sysDict);
            if (sysDictItemList != null) {
                for (SysDictItem entity : sysDictItemList) {
                    entity.setDictId(sysDict.getId());
                    entity.setStatus(1);
                    sysDictItemMapper.insert(entity);
                }
            }
        }catch(Exception e){
            return insert;
        }
        return insert;
    }

    @Override
    public List<DictDTO> queryAllDepartBackDictDTO() {
        return baseMapper.queryAllDepartBackDictModel();
    }

    @Override
    public List<DictDTO> queryAllUserBackDictDTO() {
        return baseMapper.queryAllUserBackDictModel();
    }

    @Override
    public List<DictDTO> queryTableDictItems(String table, String text, String code, String keyword) {
        return baseMapper.queryTableDictItems(table, text, code, "%"+keyword+"%");
    }

    @Override
    public List<TreeSelectDTO> queryTreeList(Map<String, String> query, String table, String text, String code, String pidField, String pid, String hasChildField) {
        return baseMapper.queryTreeList(query,table, text, code, pidField, pid,hasChildField);
    }

    @Override
    public void deleteOneDictPhysically(String id) {
        this.baseMapper.deleteOneById(id);
        this.sysDictItemMapper.delete(new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictId,id));
    }

    @Override
    public void updateDictDelFlag(int delFlag, String id) {
        baseMapper.updateDictDelFlag(delFlag,id);
    }

    @Override
    public List<SysDict> queryDeleteList() {
        return baseMapper.queryDeleteList();
    }

    @Override
    public List<DictDTO> queryDictTablePageList(DictQueryVo query, int pageSize, int pageNo) {
        Page page = new Page(pageNo,pageSize,false);
        Page<DictDTO> pageList = baseMapper.queryDictTablePageList(page, query);
        return pageList.getRecords();
    }

    @Override
    public Long duplicateCheckCount(DuplicateCheckVo duplicateCheckVo) {
        return baseMapper.duplicateCheckCountSql(duplicateCheckVo);
    }

    @Override
    public Long duplicateCheckCountNoDataId(DuplicateCheckVo duplicateCheckVo) {
        return baseMapper.duplicateCheckCountSqlNoDataId(duplicateCheckVo);
    }

}
