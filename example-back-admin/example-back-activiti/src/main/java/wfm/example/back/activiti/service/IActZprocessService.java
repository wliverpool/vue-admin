package wfm.example.back.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.activiti.model.ActBusiness;
import wfm.example.back.activiti.model.ActZprocess;
import wfm.example.common.dto.LoginUserDto;
import wfm.example.common.vo.ProcessNodeVo;

import java.util.List;

/**
 * 流程定义扩展表
 * @Author: 吴福明
 */
public interface IActZprocessService extends IService<ActZprocess> {

    List<ActZprocess> queryNewestProcess(String processKey);

    String startProcess(ActBusiness actBusiness, LoginUserDto loginUser);

    /**
     * 通过key设置所有版本为旧
     * @param processKey
     */
    void setAllOldByProcessKey(String processKey);

    /**
     * 更新最新版本的流程
     * @param processKey
     */
    void setLatestByProcessKey(String processKey);

    ProcessNodeVo getNextNode(String procDefId, String currActId, String procInsId);

    ProcessNodeVo getNode(String nodeId, String tableName, String tableId);

    ProcessNodeVo getFirstNode(String procDefId,String tableName,String tableId);

    /**
     * 发消息
     * @param actBusId 流程业务id
     * @param fromUser 发送人
     * @param toUser 接收人
     * @param title 标题
     * @param msgText 信息内容
     * @param sendMessage 系统消息
     * @param sendSms 短信
     * @param sendEmail 邮件
     */
    void sendMessage(String actBusId,LoginUserDto fromUser, LoginUserDto toUser,String title,String msgText,  Boolean sendMessage, Boolean sendSms, Boolean sendEmail);

    /**
     * 发送流程信息
     * @param fromUser 发送人
     * @param toUser 接收人
     * @param act 流程
     * @param taskName
     * @param sendMessage 系统消息
     * @param sendSms 短信消息
     * @param sendEmail 邮件消息
     */
    void sendActMessage(LoginUserDto fromUser, LoginUserDto toUser, ActBusiness act, String taskName, Boolean sendMessage, Boolean sendSms, Boolean sendEmail);
}
