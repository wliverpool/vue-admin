package wfm.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.model.SysDepartRolePermission;

/**
 * 部门角色权限接口
 * @author: 吴福明
 */
public interface ISysDepartRolePermissionService extends IService<SysDepartRolePermission> {
    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param roleId
     * @param permissionIds
     * @param lastPermissionIds
     */
    public void saveDeptRolePermission(String roleId,String permissionIds,String lastPermissionIds);
}
