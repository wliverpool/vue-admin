package wfm.example.common.dto;

import lombok.Data;

@Data
public class RoleDto {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 备注
     */
    private String description;


}
