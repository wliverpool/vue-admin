package wfm.example.back.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.activiti.mapper.ActBusinessMapper;
import wfm.example.back.activiti.model.ActBusiness;
import wfm.example.back.activiti.vo.HistoricTaskVo;
import wfm.example.common.dto.LoginUserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 流程业务扩展表
 * @Author: 吴福明
 */
public interface IActBusinessService extends IService<ActBusiness> {

    ActBusinessMapper getBaseMapper();

    /**保存业务表单数据到数据库表
     * 该方法相对通用，复杂业务单独定制，套路类似
     * @param tableId 业务表中的数据id
     * @return  如果之前数据库没有 返回 true
     * */
    boolean saveApplyForm(String tableId, HttpServletRequest request, LoginUserDto loginUser);

    Map<String, Object> getApplyForm(String tableId, String tableName);

    void deleteBusiness(String tableName, String tableId);

    /**
     * 获取业务表单数据并驼峰转换
     * @param tableId
     * @param tableName
     * */
    Map<String, Object> getBusiData(String tableId, String tableName);

    /**
     * 修改业务表的流程字段
     * @param tableId
     * @param tableName
     * @param actStatus
     */
    void updateBusinessStatus(String tableName, String tableId, String actStatus);

    /**
     * 查询我的流程列表
     * @param param
     * @param request
     * @param loginUser
     * @return
     */
    List<ActBusiness> approveList(HttpServletRequest request, ActBusiness param, LoginUserDto loginUser);

    /**
     *  获取登陆人的已办
     *
     * @param req
     * @param name 流程名
     * @param categoryId 流程类型
     * @param priority 优先级别
     * @param loginUser 登录用户
     * @return
     */
    List<HistoricTaskVo> getHistoricTaskVos(HttpServletRequest req, String name, String categoryId, Integer priority, LoginUserDto loginUser);

    List<ActBusiness> findByProcDefId(String id);

    void insertHI_IDENTITYLINK(String id, String type, String userId, String taskId, String procInstId);

    /**
     *通过类型和任务id查找用户id
     * @param taskId  任务id
     * @param type   类型
     * @return
     */
    List<String> findUserIdByTypeAndTaskId(String type, String taskId);

    List<String> selectIRunIdentity(String taskId, String type);

}
