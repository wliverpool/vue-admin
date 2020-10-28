package wfm.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.model.SysDepartRoleUser;

import java.util.List;

/**
 * 部门角色人员信息
 * @author: 吴福明
 */
public interface ISysDepartRoleUserService extends IService<SysDepartRoleUser> {

    void deptRoleUserAdd(String userId,String newRoleId,String oldRoleId);

    /**
     * 取消用户与部门关联，删除关联关系
     * @param userIds
     * @param depId
     */
    void removeDeptRoleUser(List<String> userIds, String depId);
}
