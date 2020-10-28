package wfm.example.back.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 部门权限表
 * @author: 吴福明
 */
@Data
@TableName("sys_depart_permission")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDepartPermission {

    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**部门id*/
    private String departId;

    /**权限id*/
    private String permissionId;

    /**数据规则id*/
    private String dataRuleIds;

    public SysDepartPermission() {

    }

    public SysDepartPermission(String departId, String permissionId) {
        this.departId = departId;
        this.permissionId = permissionId;
    }
}
