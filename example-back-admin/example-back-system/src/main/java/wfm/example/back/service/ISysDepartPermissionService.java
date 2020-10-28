package wfm.example.back.service;


import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.model.SysDepartPermission;
import wfm.example.back.model.SysPermissionDataRule;

import java.util.List;

/**
 * 部门权限表
 * @Author: 吴福明
 */
public interface ISysDepartPermissionService extends IService<SysDepartPermission> {
    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param departId
     * @param permissionIds
     * @param lastPermissionIds
     */
    public void saveDepartPermission(String departId,String permissionIds,String lastPermissionIds);

    /**
     * 根据部门id，菜单id获取数据规则
     * @param permissionId
     * @return
     */
    List<SysPermissionDataRule> getPermRuleListByDeptIdAndPermId(String departId, String permissionId);
}
