package wfm.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.model.SysUser;
import wfm.example.back.model.SysUserDepart;
import wfm.example.common.dto.DepartIdDTO;

import java.util.List;

/**
 * SysUserDpeart用户组织机构service
 * @author 吴福明
 *
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {


    /**
     * 根据指定用户id查询部门信息
     * @param userId
     * @return
     */
    List<DepartIdDTO> queryDepartIdsOfUser(String userId);


    /**
     * 根据部门id查询用户信息
     * @param depId
     * @return
     */
    List<SysUser> queryUserByDepId(String depId);
    /**
     * 根据部门code，查询当前部门和下级部门的用户信息
     */
    public List<SysUser> queryUserByDepCode(String depCode, String realname);

}
