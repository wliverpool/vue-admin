package wfm.example.common.dto;

import lombok.Data;

/**
 * 包含 SysUser 和 SysDepart 的 DTO
 * @author 吴福明
 */

@Data
public class SysUserSysDepartDTO {

    private String id;
    private String realname;
    private String workNo;
    private String post;
    private String telephone;
    private String email;
    private String phone;
    private String departId;
    private String departName;
    private String avatar;

}
