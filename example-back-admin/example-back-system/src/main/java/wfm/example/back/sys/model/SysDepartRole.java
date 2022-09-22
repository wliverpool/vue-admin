package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import wfm.example.back.common.aspect.Dict;
import wfm.example.back.common.model.BaseStringIDModel;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门角色
 * @author: 吴福明
 */
@Data
@TableName("sys_depart_role")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysDepartRole extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**部门id*/
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String departId;

    /**部门角色名称*/
    private String roleName;

    /**部门角色编码*/
    private String roleCode;

    /**描述*/
    private String description;

}
