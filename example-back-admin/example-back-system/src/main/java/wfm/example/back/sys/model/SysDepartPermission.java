package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import wfm.example.back.common.model.BaseStringIDModel;

import java.io.Serializable;

/**
 * 部门权限表
 * @author: 吴福明
 */
@Data
@TableName("sys_depart_permission")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysDepartPermission extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

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
