package wfm.example.back.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import wfm.example.common.aspect.Dict;

import java.util.Date;

/**
 * 部门角色
 * @author: 吴福明
 */
@Data
@TableName("sys_depart_role")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDepartRole {

    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**部门id*/
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    private String departId;

    /**部门角色名称*/
    private String roleName;

    /**部门角色编码*/
    private String roleCode;

    /**描述*/
    private String description;

    /**创建人*/
    private String createBy;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**更新人*/
    private String updateBy;

    /**更新时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
