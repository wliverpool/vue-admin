package wfm.example.back.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户代理人设置
 * @author 吴福明
 */
@Data
@TableName("sys_user_agent")
public class SysUserAgent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**序号*/
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**用户名*/
    private String userName;

    /**代理人用户名*/
    private String agentUserName;

    /**代理开始时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**代理结束时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**状态0无效1有效*/
    private String status;

    /**创建人名称*/
    private String createName;

    /**创建人登录名称*/
    private String createBy;

    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**更新人名称*/
    private String updateName;

    /**更新人登录名称*/
    private String updateBy;

    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**所属部门*/
    private String sysOrgCode;

    /**所属公司*/
    private String sysCompanyCode;
}
