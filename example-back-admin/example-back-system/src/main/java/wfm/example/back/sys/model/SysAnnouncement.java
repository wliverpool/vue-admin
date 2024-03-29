package wfm.example.back.sys.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import wfm.example.back.common.aspect.Dict;
import wfm.example.back.common.model.BaseStringIDModel;

/**
 * 系统通告表
 * @author: 吴福明
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_announcement")
public class SysAnnouncement extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 标题
     */
    private String titile;

    /**
     * 内容
     */
    private String msgContent;

    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 发布人
     */
    private String sender;

    /**
     * 优先级（L低，M中，H高）
     */
    @Dict(dicCode = "priority")
    private String priority;

    /**
     * 消息类型1:通知公告2:系统消息
     */
    @Dict(dicCode = "msg_category")
    private String msgCategory;

    /**
     * 通告对象类型（USER:指定用户，ALL:全体用户）
     */
    @Dict(dicCode = "msg_type")
    private String msgType;

    /**
     * 发布状态（0未发布，1已发布，2已撤销）
     */
    @Dict(dicCode = "send_status")
    private String sendStatus;

    /**
     * 发布时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date sendTime;

    /**
     * 撤销时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date cancelTime;

    /**
     * 删除状态（0，正常，1已删除）
     */
    private String delFlag;

    /**
     * 指定用户
     **/
    private String userIds;

    /**
     * 业务类型(email:邮件 bpm:流程)
     */
    private String busType;

    /**
     * 业务id
     */
    private String busId;

    /**
     * 打开方式 组件：component 路由：url
     */
    private String openType;

    /**
     * 组件/路由 地址
     */
    private String openPage;

    /**
     * 摘要
     */
    private String msgAbstract;
}
