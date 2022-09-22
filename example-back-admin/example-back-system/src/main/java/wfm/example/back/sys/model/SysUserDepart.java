package wfm.example.back.sys.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户部门表
 * @author 吴福明
 */

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import wfm.example.back.common.model.BaseStringIDModel;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user_depart")
public class SysUserDepart extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**用户id*/
    private String userId;

    /**部门id*/
    private String depId;

    public SysUserDepart(String id, String userId, String depId) {
        super();
        this.setId(id);
        this.userId = userId;
        this.depId = depId;
    }

    public SysUserDepart(String id, String departId) {
        this.userId = id;
        this.depId = departId;
    }
}
