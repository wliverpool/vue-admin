package wfm.example.back.activiti.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfm.example.back.activiti.mapper.ActBusinessMapper;
import wfm.example.back.activiti.model.ActBusiness;
import wfm.example.back.activiti.model.ActZprocess;
import wfm.example.back.activiti.service.IActBusinessService;
import wfm.example.back.activiti.vo.HistoricTaskVo;
import wfm.example.common.constant.ActivitiConstant;
import wfm.example.common.dto.LoginUserDto;
import wfm.example.common.service.ISysBaseAPI;
import wfm.example.common.util.DateUtils;
import wfm.example.common.util.ObjectConvertUtils;
import wfm.example.common.vo.ComboVo;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程业务扩展表
 * @Author: 吴福明
 */
@Service
public class ActBusinessServiceImpl extends ServiceImpl<ActBusinessMapper, ActBusiness> implements IActBusinessService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActZprocessServiceImpl actZprocessService;

    @Autowired
    ISysBaseAPI sysBaseAPI;

    @Autowired
    private HistoryService historyService;

    @Override
    public List<ActBusiness> approveList(HttpServletRequest request,ActBusiness param,LoginUserDto loginUser){
        // 按时间排序
        LambdaQueryWrapper<ActBusiness> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(ActBusiness::getCreateTime);
        if (StrUtil.isNotBlank(param.getTitle())) {
            queryWrapper.like(ActBusiness::getTitle,param.getTitle());
        }
        if (param.getStatus()!=null) {
            queryWrapper.eq(ActBusiness::getStatus,param.getStatus());
        }
        // 流程定义key
        String procDefKey = request.getParameter("procDefKey");
        if (StrUtil.isNotBlank(procDefKey)) {
            queryWrapper.in(ActBusiness::getProcDefId,procDefKey);
        }

        if (param.getResult()!=null) {
            queryWrapper.eq(ActBusiness::getResult,param.getResult());
        }
        String createTime_begin = request.getParameter("createTime_begin");
        if (StrUtil.isNotBlank(createTime_begin)) {
            queryWrapper.ge(ActBusiness::getCreateTime,createTime_begin);
        }
        String createTime_end = request.getParameter("createTime_end");
        if (StrUtil.isNotBlank(createTime_end)) {
            queryWrapper.le(ActBusiness::getCreateTime,createTime_end);
        }
        queryWrapper.eq(ActBusiness::getUserId,loginUser.getUsername());

        //流程类型
        String type = request.getParameter("type");
        if (StrUtil.isNotBlank(type)){
            List<String> actBusinessIdsByType = this.listByTypeApp(type);
            // 没有符合的 目的是上下面的查询条件也查不到
            if (actBusinessIdsByType.size()==0){
                queryWrapper.in(ActBusiness::getId, Lists.newArrayList(""));
            }else {
                queryWrapper.in(ActBusiness::getId,actBusinessIdsByType);
            }
        }
        List<ActBusiness> actBusinessList = this.list(queryWrapper);

        // 是否需要业务数据
        String needData = request.getParameter("needData");
        actBusinessList.forEach(e -> {
            if(StrUtil.isNotBlank(e.getProcDefId())){
                // 获取流程定义表中 路由名称和流程名称
                ActZprocess actProcess = actZprocessService.getById(e.getProcDefId());
                e.setRouteName(actProcess.getRouteName());
                e.setProcessName(actProcess.getName());
            }
            // 流程正在处理中时
            if(ActivitiConstant.STATUS_DEALING.equals(e.getStatus())){
                // 关联当前任务 查询当前待办
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(e.getProcInstId()).list();
                if(taskList!=null&&taskList.size()==1){
                    e.setCurrTaskName(taskList.get(0).getName());
                }else if(taskList!=null&&taskList.size()>1){
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<taskList.size()-1;i++){
                        sb.append(taskList.get(i).getName()+"、");
                    }
                    sb.append(taskList.get(taskList.size()-1).getName());
                    e.setCurrTaskName(sb.toString());
                }
                // 查询审批历史，如果有的话，禁止撤回操作
                List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(e.getProcInstId()).finished().list();
                if(list.size()>0){
                    e.setProcInstStatus(ActivitiConstant.PROC_INST_APPROVE);
                }else{
                    e.setProcInstStatus(ActivitiConstant.PROC_INST_NOT_APPROVE);
                }
            }
            if (StrUtil.equals(needData,"true")){ // 需要业务数据
                Map<String, Object> applyForm = this.getApplyForm(e.getTableId(), e.getTableName());
                e.setDataMap(applyForm);
            }
        });
        return actBusinessList;

    }

    @Override
    public List<ActBusiness> findByProcDefId(String id) {
       return this.list(new LambdaQueryWrapper<ActBusiness>().eq(ActBusiness::getProcDefId,id));
    }

    @Override
    public boolean saveApplyForm(String tableId, HttpServletRequest request, LoginUserDto loginUser) {
        String tableName = request.getParameter("tableName");
        String filedNames = request.getParameter("filedNames");
        Map<String, Object> busiData = this.baseMapper.getBusiData(tableId, tableName);
        String[] fileds = filedNames.split(",");
        //没有，新增逻辑
        if (MapUtil.isEmpty(busiData)){
            StringBuilder filedsB = new StringBuilder("id");
            StringBuilder filedsVB = new StringBuilder("'"+tableId+"'");
            for (String filed : fileds) {
                String dbFiled = ObjectConvertUtils.camelToUnderline(filed);
                if(filed != null && !filed.equals("undefined")){
                    if(request.getParameter(filed) != null){
                        filedsB.append(","+dbFiled);
                        filedsVB.append(",'"+request.getParameter(filed)+"'");
                    }else{
                        filedsB.append(","+dbFiled);
                        filedsVB.append(","+request.getParameter(filed));
                    }
                }
            }
            String userName = loginUser.getUsername();
            filedsB.append(","+"create_by");
            filedsVB.append(",'"+userName+"'");
            filedsB.append(","+"create_time");
            filedsVB.append(",'"+ DateUtils.formatDate(new Date(),"yyyy-MM-dd") +"'");
            this.baseMapper.insertBusiData(String.format("INSERT INTO %s (%s) VALUES (%s)",tableName,filedsB.toString(),filedsVB.toString()));
        }else { //有，修改
            StringBuilder setSql = new StringBuilder();
            for (String filed : fileds) {
                if(filed != null && !filed.equals("undefined")){
                    String parameter = request.getParameter(filed);
                    String dbFiled = ObjectConvertUtils.camelToUnderline(filed);
                    if (parameter==null){
                        setSql.append(String.format("%s = null,",dbFiled));
                    }else {
                        setSql.append(String.format("%s = '%s',",dbFiled, parameter));
                    }
                }
            }
            //去掉最后一个,号
            String substring = setSql.substring(0, setSql.length()-1);
            String userName = loginUser.getUsername();
            substring += (",update_by = " +    "'"+userName+"'");
            substring += (",update_time = " + "'" +DateUtils.formatDate(new Date(),"yyyy-MM-dd")+"'");
            this.baseMapper.updateBusiData(String.format("update %s set %s where id = '%s'",tableName,substring,tableId));
        }
        return MapUtil.isEmpty(busiData);
    }

    @Override
    public Map<String, Object> getApplyForm(String tableId, String tableName) {
        Map<String, Object> busiData = this.getBusiData(tableId, tableName);
        Object createBy = busiData.get("createBy");
        if (createBy != null){
            String depName = sysBaseAPI.getDepartNamesByUsername(createBy.toString()).get(0);
            busiData.put("createByDept",depName);
            LoginUserDto userByName = sysBaseAPI.getUserByName(createBy.toString());
            busiData.put("createByName",userByName.getRealname());
            busiData.put("createByAvatar",userByName.getAvatar());
        }
        return busiData;
    }

    @Override
    public void deleteBusiness(String tableName, String tableId) {
        this.baseMapper.deleteBusiData(tableId,tableName);
    }

    @Override
    public List<String> findUserIdByTypeAndTaskId(String type, String taskId) {
        return baseMapper.findUserIdByTypeAndTaskId(type, taskId);
    }

    @Override
    public void insertHI_IDENTITYLINK(String id, String type, String userId, String taskId, String procInstId) {
        this.baseMapper.insertHI_IDENTITYLINK(id, type, userId, taskId, procInstId);
    }

    @Override
    public List<String> selectIRunIdentity(String taskId, String type) {
       return baseMapper.selectIRunIdentity(taskId,type);
    }

    @Override
    public void updateBusinessStatus(String tableName, String tableId, String actStatus) {
        try {
            baseMapper.updateBusinessStatus(tableName,tableId,actStatus);
        } catch (Exception e) {
             // 业务表需要有 act_status字段，没有会报错，不管他
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public Map<String, Object> getBusiData(String tableId, String tableName) {
        Map<String, Object> busiData = this.baseMapper.getBusiData(tableId, tableName);
        if (busiData==null){
            return null;
        }
        HashMap<String, Object> map = Maps.newHashMap();
        for (String key : busiData.keySet()) {
            String camelName = ObjectConvertUtils.camelName(key);
            map.put(camelName,busiData.get(key));
        }
        return map;
    }

    public List<String> listByTypeApp(String type) {
        return this.baseMapper.listByTypeApp(type);
    }

    @Override
    public List<HistoricTaskVo> getHistoricTaskVos(HttpServletRequest req, String name, String categoryId, Integer priority,LoginUserDto loginUser) {
        List<HistoricTaskVo> list = new ArrayList<>();
        String userId = loginUser.getUsername();
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().or().taskCandidateUser(userId).
                taskAssignee(userId).endOr().finished();

        // 多条件搜索
        query.orderByTaskCreateTime().desc();
        if(StrUtil.isNotBlank(name)){
            query.taskNameLike("%"+name+"%");
        }
        if(StrUtil.isNotBlank(categoryId)){
            query.taskCategory(categoryId);
        }
        if(priority!=null){
            query.taskPriority(priority);
        }
        String searchVal = req.getParameter("searchVal");
        if (StrUtil.isNotBlank(searchVal)){
            //搜索标题、申请人
            List<LoginUserDto> usersByName = getBaseMapper().getUsersByName(searchVal);
            List<String> uNames = null;
            if (usersByName.size()==0){
                uNames = Lists.newArrayList("");
            }else {
                uNames = usersByName.stream().map(u->u.getUsername()).collect(Collectors.toList());
            }
            //标题查询
            List<ActBusiness> businessList = this.list(new LambdaQueryWrapper<ActBusiness>()
                    .like(ActBusiness::getTitle, searchVal)
                    .or().in(ActBusiness::getUserId,uNames)
            );
            if (businessList.size()>0){
                // 定义id
                List<String> pids = businessList.stream().filter(act -> act.getProcInstId()!=null).map(act -> act.getProcInstId()).collect(Collectors.toList());
                query.processInstanceIdIn(pids);
            }else {
                query.processInstanceIdIn(Lists.newArrayList(""));
            }
        }
        String type = req.getParameter("type");
        if (StrUtil.isNotBlank(type)){
            List<String> deployment_idList = this.getBaseMapper().deployment_idListByType(type);
            if (deployment_idList.size()==0){
                query.deploymentIdIn(Lists.newArrayList(""));
            }else {
                query.deploymentIdIn(deployment_idList);
            }
        }
        String createTime_end = req.getParameter("createTime_end");
        if(StrUtil.isNotBlank(createTime_end)){
            Date end = DateUtil.parse(createTime_end);
            query.taskCreatedBefore(DateUtil.endOfDay(end));
        }
        // 流程定义key
        String procDefKey = req.getParameter("procDefKey");
        if (StrUtil.isNotBlank(procDefKey)) {
            query.processDefinitionId(procDefKey);
        }


        List<HistoricTaskInstance> taskList = query.list();
        // 是否需要业务数据
        String needData = req.getParameter("needData");
        // 转换vo
        List<ComboVo> allUser = sysBaseAPI.queryAllUser();
        Map<String, String> userMap = allUser.stream().collect(Collectors.toMap(ComboVo::getUsername, ComboVo::getTitle));
        taskList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            // 关联委托人
            if(StrUtil.isNotBlank(htv.getOwner())){
                htv.setOwner(userMap.get(htv.getOwner()));
            }
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForProcessInstance(htv.getProcInstId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联发起人
                if("starter".equals(hik.getType())&&StrUtil.isNotBlank(hik.getUserId())){
                    htv.setApplyer(userMap.get(hik.getUserId()));
                }
            }
            // 关联审批意见
            List<Comment> comments = taskService.getTaskComments(htv.getId(), "comment");
            if(comments!=null&&comments.size()>0){
                htv.setComment(comments.get(0).getFullMessage());
            }
            // 关联流程信息
            ActZprocess actProcess = actZprocessService.getById(htv.getProcDefId());
            if(actProcess!=null){
                htv.setProcessName(actProcess.getName());
                htv.setRouteName(actProcess.getRouteName());
            }
            // 关联业务key
            HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(htv.getProcInstId()).singleResult();
            htv.setBusinessKey(hpi.getBusinessKey());
            ActBusiness actBusiness = this.getById(hpi.getBusinessKey());
            if(actBusiness!=null){
                htv.setTableId(actBusiness.getTableId());
                htv.setTableName(actBusiness.getTableName());
                htv.setTitle(actBusiness.getTitle());
                htv.setStatus(actBusiness.getStatus());
                htv.setResult(actBusiness.getResult());
                htv.setApplyTime(actBusiness.getApplyTime());
                if (StrUtil.equals(needData,"true")){ // 需要业务数据
                    Map<String, Object> applyForm = this.getApplyForm(actBusiness.getTableId(), actBusiness.getTableName());
                    htv.setDataMap(applyForm);
                }
            }

            list.add(htv);
        });
        return list;
    }
}
