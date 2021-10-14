package wfm.example.back.activiti.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wfm.example.back.activiti.mapper.ActZprocessMapper;
import wfm.example.back.activiti.model.ActBusiness;
import wfm.example.back.activiti.model.ActNode;
import wfm.example.back.activiti.model.ActZprocess;
import wfm.example.back.activiti.service.IActZprocessService;
import wfm.example.common.constant.ActivitiConstant;
import wfm.example.common.dto.BusMessageDTO;
import wfm.example.common.dto.DepartmentDto;
import wfm.example.common.dto.LoginUserDto;
import wfm.example.common.dto.RoleDto;
import wfm.example.common.enums.DySmsEnum;
import wfm.example.common.enums.SendMsgTypeEnum;
import wfm.example.common.exception.BizException;
import wfm.example.common.message.ISendMsgHandle;
import wfm.example.common.service.ISysBaseAPI;
import wfm.example.common.util.DateUtils;
import wfm.example.common.util.DySmsHelperUtils;
import wfm.example.common.vo.ProcessNodeVo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 流程定义扩展表
 * @Author: pmc
 * @Date:   2020-03-22
 * @Version: V1.0
 */
@Service
public class ActZprocessServiceImpl extends ServiceImpl<ActZprocessMapper, ActZprocess> implements IActZprocessService {

    @Value("${system.sms.server}")
    private String smsServer;
    @Value("${system.sms.port}")
    private Integer smsPort;
    @Value("${system.sms.private-key}")
    private String cxPrivateKey;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private ActNodeServiceImpl actNodeService;
    @Autowired
    private ActBusinessServiceImpl actBusinessService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void setAllOldByProcessKey(String processKey) {
        List<ActZprocess> list = this.list(new LambdaQueryWrapper<ActZprocess>().eq(ActZprocess::getProcessKey,processKey));
        if(list==null||list.size()==0){
            return;
        }
        list.forEach(item -> {
            item.setLatest(false);
        });
        this.updateBatchById(list);
    }

    @Override
    public void setLatestByProcessKey(String processKey) {
        ActZprocess actProcess = this.findTopByProcessKeyOrderByVersionDesc(processKey);
        if(actProcess==null){
            return;
        }
        actProcess.setLatest(true);
        this.updateById(actProcess);
    }

    private ActZprocess findTopByProcessKeyOrderByVersionDesc(String processKey) {
        List<ActZprocess> list = this.list(new LambdaQueryWrapper<ActZprocess>().eq(ActZprocess::getProcessKey, processKey)
                .orderByDesc(ActZprocess::getVersion)
        );
        if (CollUtil.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public String startProcess(ActBusiness actBusiness, LoginUserDto loginUser) {
        // 启动流程用户
        identityService.setAuthenticatedUserId(loginUser.getUsername());
        // 启动流程 需传入业务表id变量
        Map<String, Object> params = actBusiness.getParams();
        params.put("tableId", actBusiness.getTableId());
        ActBusiness act = actBusinessService.getById(actBusiness.getId());
        String tableName = act.getTableName();
        String tableId = act.getTableId();
        if (StrUtil.isBlank(tableId)||StrUtil.isBlank(tableName)){
            throw new BizException("没有业务表单数据");
        }
        /*表单数据写入*/
        Map<String, Object> busiData = actBusinessService.getBusiData(tableId, tableName);
        for (String key : busiData.keySet()) {
            params.put(key,busiData.get(key));
        }
        ProcessInstance pi = runtimeService.startProcessInstanceById(actBusiness.getProcDefId(), actBusiness.getId(), params);
        // 设置流程实例名称
        runtimeService.setProcessInstanceName(pi.getId(), actBusiness.getTitle());
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).list();
        for(Task task : tasks){
            if(actBusiness.getFirstGateway()){
                // 网关类型
                List<LoginUserDto> users = getNode(task.getTaskDefinitionKey(),tableName,tableId).getUsers();
                // 如果下个节点未分配审批人为空 取消结束流程
                if(users==null||users.size()==0){
                    throw new RuntimeException("任务节点未分配任何候选审批人，发起流程失败");
                }else{
                    // 分配了节点负责人分发给全部
                    for(LoginUserDto user : users){
                        taskService.addCandidateUser(task.getId(), user.getUsername());
                        // 异步发消息
                        sendActMessage(loginUser,user,actBusiness,task.getName(), actBusiness.getSendMessage(),
                                actBusiness.getSendSms(), actBusiness.getSendEmail());
                    }
                }
            }else {
                // 分配第一个任务用户
                String assignees = actBusiness.getAssignees();
                for (String assignee : assignees.split(",")) {
                    taskService.addCandidateUser(task.getId(), assignee);
                    // 异步发消息
                    LoginUserDto user = sysBaseAPI.getUserByName(assignee);
                    sendActMessage(loginUser,user,actBusiness,task.getName(), actBusiness.getSendMessage(),
                            actBusiness.getSendSms(), actBusiness.getSendEmail());
                }
            }
            // 设置任务优先级
            taskService.setPriority(task.getId(), actBusiness.getPriority());
        }
        return pi.getId();
    }

    @Override
    public void sendActMessage(LoginUserDto fromUser, LoginUserDto toUser, ActBusiness act, String taskName, Boolean sendMessage, Boolean sendSms, Boolean sendEmail) {
        String title = String.format("您有一个新的审批任务："+act.getTitle());
        Map<String, String> msgMap = Maps.newHashMap();
        /*
          流程名称：  ${bpm_name}
          催办任务：  ${bpm_task}
          催办时间 :    ${datetime}
          催办内容 :    ${remark}
          */
        msgMap.put("bpm_name",act.getTitle());
        msgMap.put("bpm_task",taskName);
        msgMap.put("datetime", DateUtils.now());
        msgMap.put("remark", "请进入待办栏，尽快处理！");
        /*流程催办模板*/
        //String msgText = sysBaseAPI.parseTemplateByCode("bpm_cuiban", msgMap);
        String msgText = String.format("您有一个新的审批任务："+act.getTitle());
        this.sendMessage(act.getId(),fromUser,toUser,title,msgText,sendMessage,sendSms,sendEmail);
    }

    @Override
    public void sendMessage(String actBusId,LoginUserDto fromUser, LoginUserDto toUser,String title,String msgText,  Boolean sendMessage, Boolean sendSms, Boolean sendEmail) {
        if (sendMessage!=null&&sendMessage){
            BusMessageDTO messageDTO = new BusMessageDTO(fromUser.getUsername(),toUser.getUsername(),title,msgText,"2","bpm",actBusId);
            sysBaseAPI.sendBusAnnouncement(messageDTO);
        }
        if (sendSms!=null&&sendSms&& StrUtil.isNotBlank(toUser.getPhone())){
            DySmsHelperUtils.sendSms(smsServer, smsPort, msgText, toUser.getPhone(), DySmsEnum.REGISTER_TEMPLATE_CODE.getTemplateCode(),cxPrivateKey);
        }
        if (sendEmail!=null&&sendEmail&& StrUtil.isNotBlank(toUser.getEmail())){
            try {
                ISendMsgHandle sendMsgHandle  = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.EMAIL.getImplClass()).newInstance();
                sendMsgHandle.sendMsg(toUser.getEmail(),title,msgText);
            } catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }
    }

    @Override
    public ProcessNodeVo getNode(String nodeId, String tableName, String tableId) {
        ProcessNodeVo node = new ProcessNodeVo();
        // 设置关联用户
        List<LoginUserDto> users = getNodetUsers(nodeId,tableName,tableId);
        node.setUsers(removeDuplicate(users));
        return node;
    }
    /**
     * 设置节点审批人
     * @param nodeId
     */
    public List<LoginUserDto> getNodetUsers(String nodeId,String tableName,String tableId){
        ActBusiness business = actBusinessService.getOne(new LambdaQueryWrapper<ActBusiness>().eq(ActBusiness::getTableId, tableId).eq(ActBusiness::getTableName, tableName));
        String procDefId = business.getProcDefId();
        // 直接选择人员
        List<LoginUserDto> users = actNodeService.findUserByNodeId(nodeId,procDefId);
        // 根据角色选择
        List<RoleDto> roles = actNodeService.findRoleByNodeId(nodeId,procDefId);
        for(RoleDto r : roles){
            List<LoginUserDto> userList = actNodeService.findUserByRoleId(r.getId());
            users.addAll(userList);
        }
        // 部门
        List<DepartmentDto> departments = actNodeService.findDepartmentByNodeId(nodeId,procDefId);
        for (DepartmentDto d : departments){
            List<LoginUserDto> userList = actNodeService.findUserDepartmentId(d.getId());
            users.addAll(userList);
        }
        // 部门负责人
        List<DepartmentDto> departmentManages = actNodeService.findDepartmentManageByNodeId(nodeId,procDefId);
        for (DepartmentDto d : departmentManages){
            List<LoginUserDto> userList = actNodeService.findUserDepartmentManageId(d.getId());
            users.addAll(userList);
        }
        // 发起人部门负责人
        if(actNodeService.hasChooseDepHeader(nodeId,procDefId)){
            List<LoginUserDto> allUser = actNodeService.queryAllUser();
            //申请人的部门负责人
            String createBy = getCreateBy(tableName,tableId);
            List<String> departIds = sysBaseAPI.getDepartIdsByUsername(createBy);

            for (String departId : departIds) {
                List<LoginUserDto> collect = allUser.stream().filter(u -> u.getDepartIds() != null && u.getDepartIds().indexOf(departId) > -1).collect(Collectors.toList());
                users.addAll(collect);
            }
        }
        // 发起人
        if(actNodeService.hasChooseSponsor(nodeId,procDefId)){
            String createBy = getCreateBy(tableName,tableId);
            LoginUserDto userByName = sysBaseAPI.getUserByName(createBy);
            users.add(userByName);
        }
        // 表单变量
        if(actNodeService.hasFormVariable(nodeId,procDefId)){
            List<String> variableNames = actNodeService.findFormVariableByNodeId(nodeId,procDefId);
            if(!variableNames.isEmpty()){
                Map<String, Object> applyForm = actBusinessService.getApplyForm(tableId, tableName);
                for(String variable : variableNames){
                    // 变量类型
                    String type = "user";
                    String paramName = variable;

                    int i = variable.indexOf(":");
                    if(i>0){
                        type = variable.substring(i + 1);
                        paramName = variable.substring(0,i);
                    }
                    // 获取表单变量的值
                    String paramVal = (String)applyForm.get(paramName);

                    if(StringUtils.isNotEmpty(paramVal)){
                        for(String val : StringUtils.split(paramVal,',')) {
                            if(StringUtils.equalsIgnoreCase(type,"role")){
                                List<LoginUserDto> roleUsers = actNodeService.findUserByRoleId(val);
                                users.addAll(roleUsers);
                            }else if(StringUtils.equalsIgnoreCase(type,"user")){
                                LoginUserDto user = sysBaseAPI.getUserByName(val);
                                if(user!=null){
                                    users.add(user);
                                }
                            }else if(StringUtils.equalsIgnoreCase(type,"dept")){
                                List<LoginUserDto> depUsers = actNodeService.findUserDepartmentId(val);
                                users.addAll(depUsers);
                            }else if(StringUtils.equalsIgnoreCase(type,"deptManage")){
                                List<LoginUserDto> depManageUsers = actNodeService.findUserDepartmentManageId(val);
                                users.addAll(depManageUsers);
                            }
                        }

                    }


                }
            }
        }

        // 过滤掉删除用户
        users = users.stream().filter(u->StrUtil.equals("0",u.getDelFlag()+"")).collect(Collectors.toList());
        return users;
    }

    /**
     * 根据节点id获取申请人
     * @param nodeId
     * @return
     */
    public String getUserByNodeid(String nodeId) {
        ActNode actNode = actNodeService.getOne(new LambdaQueryWrapper<ActNode>().eq(ActNode::getNodeId, nodeId).last("limit 1"));
        String procDefId = actNode.getProcDefId();
        ActBusiness actBusiness = actBusinessService.getOne(new LambdaQueryWrapper<ActBusiness>().eq(ActBusiness::getProcDefId, procDefId).last("limit 1"));
        return actBusiness.getCreateBy();
    }

    /**
     * 获取表单的创建人
     * @param tableName
     * @param tableId
     * @return
     */
    public String getCreateBy(String tableName,String tableId) {
        Map<String, Object> applyForm = actBusinessService.getApplyForm(tableId, tableName);
        return applyForm.get("createBy").toString();
    }

    /**
     * 去重
     * @param list
     * @return
     */
    private List<LoginUserDto> removeDuplicate(List<LoginUserDto> list) {

        LinkedHashSet<LoginUserDto> set = new LinkedHashSet<>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
        entityManager.clear();
        list.forEach(u -> {
            u.setPassword(null);
        });
        return list;
    }

    @Override
    public ProcessNodeVo getFirstNode(String procDefId,String tableName,String tableId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);

        ProcessNodeVo node = new ProcessNodeVo();

        List<Process> processes = bpmnModel.getProcesses();
        Collection<FlowElement> elements = processes.get(0).getFlowElements();
        // 流程开始节点
        StartEvent startEvent = null;
        for (FlowElement element : elements) {
            if (element instanceof StartEvent) {
                startEvent = (StartEvent) element;
                break;
            }
        }
        FlowElement e = null;
        // 判断开始后的流向节点
        SequenceFlow sequenceFlow = startEvent.getOutgoingFlows().get(0);
        for (FlowElement element : elements) {
            if(element.getId().equals(sequenceFlow.getTargetRef())){
                if(element instanceof UserTask){
                    e = element;
                    node.setType(1);
                    break;
                }else if(element instanceof ExclusiveGateway){
                    e = element;
                    node.setType(3);
                    break;
                }else if(element instanceof ParallelGateway){
                    e = element;
                    node.setType(4);
                    break;
                }else{
                    throw new RuntimeException("流程设计错误，开始节点后只能是用户任务节点、排他网关、并行网关");
                }
            }
        }
        // 排他、平行网关直接返回
        if(e instanceof ExclusiveGateway || e instanceof ParallelGateway){
            return node;
        }
        node.setTitle(e.getName());
        // 设置关联用户
        List<LoginUserDto> users = getNodetUsers(e.getId(),tableName,tableId);
        node.setUsers(removeDuplicate(users));
        return node;
    }

    @Override
    public ProcessNodeVo getNextNode(String procDefId, String currActId,String procInsId) {
        // 根据流程实例id获取业务表单数据
        ActBusiness actBusiness = actBusinessService.getOne(new LambdaQueryWrapper<ActBusiness>().eq(ActBusiness::getProcInstId, procInsId));

        ProcessNodeVo node = new ProcessNodeVo();
        // 当前执行节点id
        ProcessDefinitionEntity dfe = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(procDefId);
        // 获取所有节点
        List<ActivityImpl> activitiList = dfe.getActivities();
        // 判断出当前流程所处节点，根据路径获得下一个节点实例
        for(ActivityImpl activityImpl : activitiList){
            if (activityImpl.getId().equals(currActId)) {
                // 获取下一个节点
                List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();

                PvmActivity pvmActivity = pvmTransitions.get(0).getDestination();

                String type = pvmActivity.getProperty("type").toString();
                if("userTask".equals(type)){
                    // 用户任务节点
                    node.setType(ActivitiConstant.NODE_TYPE_TASK);
                    node.setTitle(pvmActivity.getProperty("name").toString());
                    // 设置关联用户
                    List<LoginUserDto> users = getNodetUsers(pvmActivity.getId(),actBusiness.getTableName(),actBusiness.getTableId());
                    node.setUsers(removeDuplicate(users));
                }else if("exclusiveGateway".equals(type)){
                    // 排他网关
                    node.setType(ActivitiConstant.NODE_TYPE_EG);
                    ActivityImpl pvmActivity1 = (ActivityImpl) pvmActivity;
                    /*定义变量*/
                    Map<String, Object> vals = Maps.newHashMap();

                    LambdaQueryWrapper<ActBusiness> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(ActBusiness::getProcInstId,procInsId);
                    ActBusiness one = actBusinessService.getOne(wrapper);
                    vals = actBusinessService.getApplyForm(one.getTableId(), one.getTableName());

                    TaskDefinition taskDefinition = actNodeService.nextTaskDefinition(pvmActivity1, pvmActivity1.getId(), vals, procInsId);
                    List<LoginUserDto> users = getNodetUsers(taskDefinition.getKey(),actBusiness.getTableName(),actBusiness.getTableId());
                    node.setUsers(removeDuplicate(users));
                }else if("parallelGateway".equals(type)){
                    // 平行网关
                    node.setType(ActivitiConstant.NODE_TYPE_PG);
                }else if("endEvent".equals(type)){
                    // 结束
                    node.setType(ActivitiConstant.NODE_TYPE_END);
                }else{
                    throw new BizException("流程设计错误，包含无法处理的节点");
                }
                break;
            }
        }

        return node;
    }

    @Override
    public List<ActZprocess> queryNewestProcess(String processKey) {
        return baseMapper.selectNewestProcess(processKey);
    }
}
