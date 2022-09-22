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
 * 部门角色人员信息
 * @author: 吴福明
 */
@Data
@TableName("sys_depart_role_user")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysDepartRoleUser extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

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
