package wfm.example.back.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 部门角色人员信息
 * @author: 吴福明
 */
@Data
@TableName("sys_depart_role_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDepartRoleUser {

    /**主键id*/
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**用户id*/
    private String userId;

    /**角色id*/
    private String droleId;

    public SysDepartRoleUser() {

    }

    public SysDepartRoleUser(String userId, String droleId) {
        this.userId = userId;
        this.droleId = droleId;
    }
}
