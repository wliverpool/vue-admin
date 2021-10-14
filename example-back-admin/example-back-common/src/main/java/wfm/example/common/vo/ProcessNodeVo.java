package wfm.example.common.vo;

import lombok.Data;
import wfm.example.common.dto.DepartmentDto;
import wfm.example.common.dto.LoginUserDto;
import wfm.example.common.dto.RoleDto;

import java.util.List;

@Data
public class ProcessNodeVo {

    /**节点id*/
    private String id;

    /**流程定义id*/
    private String procDefId;

    /**节点名*/
    private String title;

    /**节点类型 0开始 1用户任务 2结束 3排他网关 4并行网关*/
    private Integer type;

    /**关联角色*/
    private List<RoleDto> roles;

    /**关联用户*/
    private List<LoginUserDto> users;

    /**关联部门*/
    private List<DepartmentDto> departments;

    /**关联部门负责人**/
    private List<DepartmentDto> departmentManages;

    /**表单变量**/
    private String formVariables;

    /**选操作人的部门负责人*/
    private Boolean chooseDepHeader = false;

    /**选操作人*/
    private Boolean chooseSponsor = false;

    /**节点展开 前端所需*/
    private Boolean expand = true;
}
