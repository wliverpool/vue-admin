package wfm.example.back.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.common.vo.TreeVo;
import wfm.example.back.sys.model.SysPermission;
import wfm.example.back.common.exception.BizException;

import java.util.List;

/**
 * 菜单权限表 服务类
 * @author 吴福明
 */
public interface ISysPermissionService extends IService<SysPermission> {

    public List<TreeVo> queryListByParentId(String parentId);

    /**真实删除*/
    public void deletePermission(String id) throws BizException;
    /**逻辑删除*/
    public void deletePermissionLogical(String id) throws BizException;

    public void addPermission(SysPermission sysPermission) throws BizException;

    public void editPermission(SysPermission sysPermission) throws BizException;

    public List<SysPermission> queryByUser(String username);

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     *
     * @param id
     * @return
     */
    public void deletePermRuleByPermId(String id);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     * @return
     */
    public List<String> queryPermissionUrlWithStar();

    /**
     * 判断用户否拥有权限
     * @param username
     * @param sysPermission
     * @return
     */
    public boolean hasPermission(String username, SysPermission sysPermission);

    /**
     * 根据用户和请求地址判断是否有此权限
     * @param username
     * @param url
     * @return
     */
    public boolean hasPermission(String username, String url);
}