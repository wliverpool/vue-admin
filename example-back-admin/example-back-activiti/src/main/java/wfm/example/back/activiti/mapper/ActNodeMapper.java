package wfm.example.back.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import wfm.example.back.activiti.model.ActNode;
import wfm.example.back.common.dto.DepartmentDto;
import wfm.example.back.common.dto.LoginUserDto;
import wfm.example.back.common.dto.RoleDto;

import java.util.List;

/**
 * 流程节点扩展表
 * @Author: 吴福明
 */
public interface ActNodeMapper extends BaseMapper<ActNode> {

    List<LoginUserDto> findUserByNodeId(@Param("nodeId") String nodeId, @Param("procDefId") String procDefId);

    List<RoleDto> findRoleByNodeId(@Param("nodeId") String nodeId, @Param("procDefId") String procDefId);

    List<DepartmentDto> findDepartmentByNodeId(@Param("nodeId") String nodeId,@Param("procDefId") String procDefId);

    List<DepartmentDto> findDepartmentManageByNodeId(@Param("nodeId") String nodeId, @Param("procDefId") String procDefId);

    List<String> findFormVariableByNodeId(@Param("nodeId") String nodeId,@Param("procDefId") String procDefId);

    @Select("select role_code from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where username=#{username}))")
    List<String> getRoleByUserName(@Param("username") String username);

    @Select("select * from sys_user")
    List<LoginUserDto> queryAllUser();

    @Select("select * from sys_user where id in (select user_id from sys_user_role where role_id = #{id})")
    List<LoginUserDto> findUserByRoleId(@Param("id") String id);

    @Select("select * from sys_user where id in (select user_id from sys_user_depart where dep_id = #{id})")
    List<LoginUserDto> findUserDepartmentId(@Param("id") String id);

    @Select("select * from sys_user where FIND_IN_SET(#{id},depart_ids)")
    List<LoginUserDto> findUserDepartmentManageId(@Param("id") String id);
    @Select("select * from act_z_node where node_id = #{nodeId}")
    List<ActNode> findByNodeId(@Param("nodeId") String nodeId);

}
