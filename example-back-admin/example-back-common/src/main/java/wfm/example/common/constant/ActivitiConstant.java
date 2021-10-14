package wfm.example.common.constant;

/**
 * 工作流相关常量
 * @author 吴福明
 */
public class ActivitiConstant {

    /**
     * 流程状态 已激活
     */
    public static final Integer PROCESS_STATUS_ACTIVE = 1;

    /**
     * 流程状态 暂停挂起
     */
    public static final Integer PROCESS_STATUS_SUSPEND = 0;

    /**
     * 资源类型 XML
     */
    public static final Integer RESOURCE_TYPE_XML = 0;

    /**
     * 资源类型 图片
     */
    public static final Integer RESOURCE_TYPE_IMAGE = 1;

    /**
     * 状态 待提交申请
     */
    public static final Integer STATUS_TO_APPLY = 0;

    /**
     * 状态 处理中
     */
    public static final Integer STATUS_DEALING = 1;

    /**
     * 状态 处理结束
     */
    public static final Integer STATUS_FINISH = 2;

    /**
     * 状态 已撤回
     */
    public static final Integer STATUS_CANCELED = 3;

    /**
     * 结果 待提交
     */
    public static final Integer RESULT_TO_SUBMIT = 0;

    /**
     * 结果 处理中
     */
    public static final Integer RESULT_DEALING = 1;

    /**
     * 结果 通过
     */
    public static final Integer RESULT_PASS = 2;

    /**
     * 结果 驳回
     */
    public static final Integer RESULT_FAIL = 3;

    /**
     * 结果 撤回
     */
    public static final Integer RESULT_CANCEL = 4;

    /**
     * 结果 删除
     */
    public static final Integer RESULT_DELETED = 5;

    /**
     * 节点类型 开始节点
     */
    public static final Integer NODE_TYPE_START = 0;

    /**
     * 节点类型 用户任务
     */
    public static final Integer NODE_TYPE_TASK = 1;

    /**
     * 节点类型 结束
     */
    public static final Integer NODE_TYPE_END = 2;

    /**
     * 节点类型 排他网关
     */
    public static final Integer NODE_TYPE_EG = 3;

    /**
     * 节点类型 平行网关
     */
    public static final Integer NODE_TYPE_PG = 4;

    /**E
     * 节点关联类型 角色
     */
    public static final Integer NODE_ROLE = 0;

    /**
     * 节点关联类型 用户
     */
    public static final Integer NODE_USER = 1;

    /**
     * 节点关联类型 部门
     */
    public static final Integer NODE_DEPARTMENT = 2;

    /**
     * 节点关联类型 操作人的部门负责人
     */
    public static final Integer NODE_DEP_HEADER = 3;

    /**
     * 待办消息id
     */
    public static final String MESSAGE_TODO_ID = "124717033060306944";

    /**
     * 通过消息id
     */
    public static final String MESSAGE_PASS_ID = "124743474544119808";

    /**
     * 驳回消息id
     */
    public static final String MESSAGE_BACK_ID = "124744392996032512";

    /**
     * 委托消息id
     */
    public static final String MESSAGE_DELEGATE_ID = "124744706050494464";

    /**
     * 待办消息
     */
    public static final String MESSAGE_TODO_CONTENT = "待审批";

    /**
     * 通过消息
     */
    public static final String MESSAGE_PASS_CONTENT = "申请流程已通过";

    /**
     * 驳回消息
     */
    public static final String MESSAGE_BACK_CONTENT = "申请流程已驳回";

    /**
     * 委托消息
     */
    public static final String MESSAGE_DELEGATE_CONTENT = "被委托待审批";

    /**
     * 候选
     */
    public static final String EXECUTOR_candidate = "candidate";
    /**
     * 执行任务用户类型 - 通过
     */
    public static final String EXECUTOR_TYPE_p = "actualExecutor_p";
    /**
     * 执行任务用户类型 - 驳回
     */
    public static final String EXECUTOR_TYPE_b = "actualExecutor_b";

    /**
     * 删除理由前缀
     */
    public static final String DELETE_PRE = "deleted-";

    /**
     * 取消理由前缀
     */
    public static final String CANCEL_PRE = "canceled-";

    /**
     * 驳回标记
     */
    public static final String BACKED_FLAG = "backed";

    /**
     * 通过标记
     */
    public static final String PASSED_FLAG = "completed";
    /**标题关键字*/
    public static final String titleKey = "title";

    /**
     * 流程实例状态
     */
    public static final Integer PROC_INST_APPROVE = 0;

    public static final Integer PROC_INST_NOT_APPROVE = 1;


}
