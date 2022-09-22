package wfm.example.back.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wfm.example.back.sys.mapper.SysCategoryMapper;
import wfm.example.back.sys.model.SysCategory;
import wfm.example.back.sys.service.ISysCategoryService;
import wfm.example.back.util.FillRuleUtils;
import wfm.example.back.common.constant.FillRuleConstant;
import wfm.example.back.common.dto.TreeSelectDTO;
import wfm.example.back.common.exception.BizException;
import wfm.example.back.common.util.ObjectConvertUtils;

import java.util.List;
import java.util.Map;

/**
 * 分类字典服务实现类
 * @author: 吴福明
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {

    @Override
    public void addSysCategory(SysCategory sysCategory) {
        String categoryCode = "";
        String categoryPid = ISysCategoryService.ROOT_PID_VALUE;
        String parentCode = null;
        if(ObjectConvertUtils.isNotEmpty(sysCategory.getPid())){
            categoryPid = sysCategory.getPid();

            //PID 不是根节点 说明需要设置父节点 hasChild 为1
            if(!ISysCategoryService.ROOT_PID_VALUE.equals(categoryPid)){
                SysCategory parent = baseMapper.selectById(categoryPid);
                parentCode = parent.getCode();
                if(parent!=null && !"1".equals(parent.getHasChild())){
                    parent.setHasChild("1");
                    baseMapper.updateById(parent);
                }
            }
        }
        JSONObject formData = new JSONObject();
        formData.put("pid",categoryPid);
        categoryCode = (String) FillRuleUtils.executeRule(FillRuleConstant.CATEGORY,formData);
        sysCategory.setCode(categoryCode);
        sysCategory.setPid(categoryPid);
        baseMapper.insert(sysCategory);
    }

    @Override
    public void updateSysCategory(SysCategory sysCategory) {
        if(ObjectConvertUtils.isEmpty(sysCategory.getPid())){
            sysCategory.setPid(ISysCategoryService.ROOT_PID_VALUE);
        }else{
            //如果当前节点父ID不为空 则设置父节点的hasChild 为1
            SysCategory parent = baseMapper.selectById(sysCategory.getPid());
            if(parent!=null && !"1".equals(parent.getHasChild())){
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.updateById(sysCategory);
    }

    @Override
    public List<TreeSelectDTO> queryListByCode(String pcode) throws BizException {
        String pid = ROOT_PID_VALUE;
        if(ObjectConvertUtils.isNotEmpty(pcode)) {
            List<SysCategory> list = baseMapper.selectList(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getCode, pcode));
            if(list==null || list.size() ==0) {
                throw new BizException("该编码【"+pcode+"】不存在，请核实!");
            }
            if(list.size()>1) {
                throw new BizException("该编码【"+pcode+"】存在多个，请核实!");
            }
            pid = list.get(0).getId();
        }
        return baseMapper.queryListByPid(pid,null);
    }

    @Override
    public List<TreeSelectDTO> queryListByPid(String pid) {
        if(ObjectConvertUtils.isEmpty(pid)) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid,null);
    }

    @Override
    public List<TreeSelectDTO> queryListByPid(String pid, Map<String, String> condition) {
        if(ObjectConvertUtils.isEmpty(pid)) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid,condition);
    }

    @Override
    public String queryIdByCode(String code) {
        return baseMapper.queryIdByCode(code);
    }

}