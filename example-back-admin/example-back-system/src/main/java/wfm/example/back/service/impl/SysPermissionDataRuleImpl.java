package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wfm.example.back.mapper.SysPermissionDataRuleMapper;
import wfm.example.back.mapper.SysPermissionMapper;
import wfm.example.back.model.SysPermission;
import wfm.example.back.model.SysPermissionDataRule;
import wfm.example.back.query.QueryGenerator;
import wfm.example.back.service.ISysPermissionDataRuleService;
import wfm.example.common.constant.CommonConstant;
import wfm.example.common.util.ObjectConvertUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单权限规则  服务实现类
 * @author 吴福明
 */
@Service
@Slf4j
public class SysPermissionDataRuleImpl extends ServiceImpl<SysPermissionDataRuleMapper, SysPermissionDataRule> implements ISysPermissionDataRuleService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 根据菜单id查询其对应的权限数据
     */
    @Override
    public List<SysPermissionDataRule> getPermRuleListByPermId(String permissionId) {
        LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<SysPermissionDataRule>();
        query.eq(SysPermissionDataRule::getPermissionId, permissionId);
        query.orderByDesc(SysPermissionDataRule::getCreateTime);
        List<SysPermissionDataRule> permRuleList = this.list(query);
        return permRuleList;
    }

    /**
     * 根据前端传递的权限名称和权限值参数来查询权限数据
     */
    @Override
    public List<SysPermissionDataRule> queryPermissionRule(SysPermissionDataRule permRule) {
        QueryWrapper<SysPermissionDataRule> queryWrapper = QueryGenerator.initQueryWrapper(permRule, null);
        return this.list(queryWrapper);
    }

    @Override
    public List<SysPermissionDataRule> queryPermissionDataRules(String username,String permissionId) {
        List<String> idsList = this.baseMapper.queryDataRuleIds(username, permissionId);
        if(idsList==null || idsList.size()==0) {
            return null;
        }
        Set<String> set = new HashSet<String>();
        for (String ids : idsList) {
            if(ObjectConvertUtils.isEmpty(ids)) {
                continue;
            }
            String[] arr = ids.split(",");
            for (String id : arr) {
                if(ObjectConvertUtils.isNotEmpty(id) && !set.contains(id)) {
                    set.add(id);
                }
            }
        }
        if(set.size()==0) {
            return null;
        }
        return this.baseMapper.selectList(new QueryWrapper<SysPermissionDataRule>().in("id", set).eq("status", CommonConstant.STATUS_1));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePermissionDataRule(SysPermissionDataRule sysPermissionDataRule) {
        this.save(sysPermissionDataRule);
        SysPermission permission = sysPermissionMapper.selectById(sysPermissionDataRule.getPermissionId());
        if(permission!=null && (permission.getRuleFlag()==null || permission.getRuleFlag().equals(CommonConstant.RULE_FLAG_0))) {
            permission.setRuleFlag(CommonConstant.RULE_FLAG_1);
            sysPermissionMapper.updateById(permission);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermissionDataRule(String dataRuleId) {
        SysPermissionDataRule dataRule = this.baseMapper.selectById(dataRuleId);
        if(dataRule!=null) {
            this.removeById(dataRuleId);
            Integer count =  this.baseMapper.selectCount(new LambdaQueryWrapper<SysPermissionDataRule>().eq(SysPermissionDataRule::getPermissionId, dataRule.getPermissionId()));
            //注:同一个事务中删除后再查询是会认为数据已被删除的 若事务回滚上述删除无效
            if(count==null || count==0) {
                SysPermission permission = sysPermissionMapper.selectById(dataRule.getPermissionId());
                if(permission!=null && permission.getRuleFlag().equals(CommonConstant.RULE_FLAG_1)) {
                    permission.setRuleFlag(CommonConstant.RULE_FLAG_0);
                    sysPermissionMapper.updateById(permission);
                }
            }
        }

    }

}
