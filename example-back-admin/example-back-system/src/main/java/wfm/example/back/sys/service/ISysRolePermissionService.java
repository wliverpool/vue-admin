package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.SysRolePermission;

/**
 * 角色权限表 服务类
 * @author 吴福明
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 保存授权/先删后增
     * @param roleId
     * @param permissionIds
     */
    public void saveRolePermission(String roleId,String permissionIds);

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param roleId
     * @param permissionIds
     * @param lastPermissionIds
     */
    public void saveRolePermission(String roleId,String permissionIds,String lastPermissionIds);

}
