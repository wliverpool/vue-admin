package wfm.example.back.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 部门角色权限
 * @author: 吴福明
 */
@Data
@TableName("sys_depart_role_permission")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDepartRolePermission {

    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**部门id*/
    private String departId;

    /**角色id*/
    private String roleId;

    /**权限id*/
    private String permissionId;

    /**dataRuleIds*/
    private String dataRuleIds;

    /** 操作时间 */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operateDate;

    /** 操作ip */
    private String operateIp;

    public SysDepartRolePermission() {
    }

    public SysDepartRolePermission(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}