package wfm.example.back.activiti.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程业务扩展表
 * @Author: 吴福明
 */
@Data
@TableName("act_z_business")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActBusiness {

    /**
     * 表单路由
     */
    @TableField(exist = false)
    private String routeName;

    @TableField(exist = false)
    private Map dataMap;

    /**
     * 流程名称
     */
    @TableField(exist = false)
    private String processName;

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * createBy
     */
    private String createBy;

    /**
     * createTime
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * delFlag
     */
    private Integer delFlag;

    /**
     * updateBy
     */
    private String updateBy;

    /**
     * updateTime
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 流程定义id
     */
    private String procDefId;

    /**
     * 流程实例id
     */
    private String procInstId;

    /**
     * 结果状态 0未提交默认 1处理中 2通过 3驳回
     */
    private Integer result;

    /**
     * 状态 0草稿默认 1处理中 2结束
     */
    private Integer status;

    /**
     * 关联表的数据id
     */
    private String tableId;

    /**
     * 申请标题
     */
    private String title;

    /**
     * 创建用户id
     */
    private String userId;

    /**
     * 提交申请时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /**
     * 历史标记
     */
    private Boolean isHistory;

    /**
     * 数据表名
     */
    private String tableName;


    /**
     * 审批人(用户名username)，多个,号相连
     */
    @TableField(exist = false)
    private String assignees;

    /**任务优先级 默认0   0普通1重要2紧急*/
    @TableField(exist = false)
    private Integer priority = 0;

    /**
     * 当前任务
     */
    @TableField(exist = false)
    private String currTaskName;

    /**
     * 第一个节点是否为网关
     */
    @TableField(exist = false)
    private Boolean firstGateway = false;

    /**
     * 是否发送站内消息
     */
    @TableField(exist = false)
    private Boolean sendMessage;

    /**
     * 是否发送短信通知
     */
    @TableField(exist = false)
    private Boolean sendSms;

    /**
     * 是否发送邮件通知
     */
    @TableField(exist = false)
    private Boolean sendEmail;

    /**
     * 流程设置参数
     */
    @JsonIgnore
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();

    /**
     * 流程实例状态 0：未审批，1：已审批
     */
    @TableField(exist = false)
    private Integer procInstStatus;
}
