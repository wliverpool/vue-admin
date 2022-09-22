package wfm.example.back.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户角色vo
 * @author 吴福明
 */

@Data
public class SysUserRoleVo implements Serializable {

    /**部门id*/
    private String roleId;
    /**对应的用户id集合*/
    private List<String> userIdList;

    public SysUserRoleVo() {
        super();
    }

    public SysUserRoleVo(String roleId, List<String> userIdList) {
        super();
        this.roleId = roleId;
        this.userIdList = userIdList;
    }

}
