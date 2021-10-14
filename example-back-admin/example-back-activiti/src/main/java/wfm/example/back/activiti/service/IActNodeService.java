package wfm.example.back.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.activiti.model.ActNode;
import wfm.example.common.dto.DepartmentDto;
import wfm.example.common.dto.LoginUserDto;
import wfm.example.common.dto.RoleDto;

import java.util.List;

/**
 * 流程节点扩展表
 * @Author: 吴福明
 */
public interface IActNodeService extends IService<ActNode> {

    List<String> getRoleByUserName(String username);

    void deleteByNodeId(String id,String procDefId);

    List<LoginUserDto> findUserByNodeId(String nodeId, String procDefId);

    List<RoleDto> findRoleByNodeId(String nodeId, String procDefId);

    List<DepartmentDto> findDepartmentByNodeId(String nodeId, String procDefId);

    List<DepartmentDto> findDepartmentManageByNodeId(String nodeId,String procDefId);

    Boolean hasChooseDepHeader(String nodeId,String procDefId);

    Boolean hasChooseSponsor(String nodeId,String procDefId);

    List<String> findFormVariableByNodeId(String nodeId,String procDefId);

}
