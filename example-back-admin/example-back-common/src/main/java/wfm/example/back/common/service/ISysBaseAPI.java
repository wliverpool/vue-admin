package wfm.example.back.common.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import wfm.example.back.common.dto.BusMessageDTO;
import wfm.example.back.common.dto.DictDTO;
import wfm.example.back.common.dto.LoginUserDto;
import wfm.example.back.common.vo.ComboVo;
import wfm.example.back.common.vo.SysCategoryVo;
import wfm.example.back.common.vo.SysDepartVo;

/**
 * 底层共通业务API，提供其他独立模块调用
 * @Author: 吴福明
 */
public interface ISysBaseAPI {

    /**
     * 根据用户账号查询用户信息
     * @param username
     * @return
     */
    public LoginUserDto getUserByName(String username);

    /**
     * 通过用户账号查询角色集合
     * @param username
     * @return
     */
    public List<String> getRolesByUsername(String username);

    /**
     * 通过用户账号查询部门集合
     * @param username
     * @return 部门 id
     */
    List<String> getDepartIdsByUsername(String username);

    /**
     * 通过用户账号查询部门 name
     * @param username
     * @return 部门 name
     */
    List<String> getDepartNamesByUsername(String username);

    /**
     * 获取当前数据库类型
     * @return
     * @throws Exception
     */
    public String getDatabaseType() throws SQLException;

    /**
     * 获取数据字典
     * @param code
     * @return
     */
    public List<DictDTO> queryDictItemsByCode(String code);

    /** 查询所有的父级字典，按照create_time排序 */
    public List<DictDTO> queryAllDict();

    /**
     * 查询所有分类字典
     * @return
     */
    public List<SysCategoryVo> queryAllDSysCategory();

    /**
     * 获取表数据字典
     * @param table
     * @param text
     * @param code
     * @return
     */
    List<DictDTO> queryTableDictItemsByCode(String table, String text, String code);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     * @return
     */
    public List<DictDTO> queryAllDepartBackDictModel();

    /**
     * 查询所有部门，拼接查询条件
     * @return
     */
    List<JSONObject> queryAllDepart(Wrapper wrapper);

    /**
     * 发送系统消息
     * @param fromUser 发送人(用户登录账户)
     * @param toUser  发送给(用户登录账户)
     * @param title  消息主题
     * @param msgContent  消息内容
     */
    public void sendSysAnnouncement(String fromUser,String toUser,String title, String msgContent);

    /**
     * 发送系统消息
     * @param fromUser 发送人(用户登录账户)
     * @param toUser   发送给(用户登录账户)
     * @param title    通知标题
     * @param map  	   模板参数
     * @param templateCode  模板编码
     */
    public void sendSysAnnouncement(String fromUser, String toUser,String title, Map<String, String> map, String templateCode);

    /**
     *
     * @param fromUser 发送人(用户登录账户)
     * @param toUser 发送给(用户登录账户)
     * @param title 通知标题
     * @param map 模板参数
     * @param templateCode 模板编码
     * @param busType 业务类型
     * @param busId 业务id
     */
    public void sendSysAnnouncement(String fromUser, String toUser,String title, Map<String, String> map, String templateCode,String busType,String busId);


    /**
     * 2发送消息 附带业务参数
     * @param message 使用构造器赋值参数
     */
    void sendBusAnnouncement(BusMessageDTO message);

    /**
     * 通过消息中心模板，生成推送内容
     *
     * @param templateCode 模板编码
     * @param map          模板参数
     * @return
     */
    public String parseTemplateByCode(String templateCode, Map<String, String> map);


    /**
     * 发送系统消息
     * @param fromUser 发送人(用户登录账户)
     * @param toUser  发送给(用户登录账户)
     * @param title  消息主题
     * @param msgContent  消息内容
     * @param setMsgCategory  消息类型 1:消息2:系统消息
     */
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory);

    /**queryTableDictByKeys
     * 发送系统消息
     * @param fromUser 发送人(用户登录账户)
     * @param toUser  发送给(用户登录账户)
     * @param title  消息主题
     * @param msgContent  消息内容
     * @param setMsgCategory  消息类型 1:消息2:系统消息
     * @param busType  业务类型
     * @param busId  业务id
     */
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory,String busType,String busId);

    /**
     * 根据业务类型及业务id修改消息已读
     * @param busType
     * @param busId
     * @param userId
     */
    public void updateSysAnnounceReadFlag(String busType,String busId,String userId);
    /**
     * 查询表字典 支持过滤数据
     * @param table
     * @param text
     * @param code
     * @param filterSql
     * @return
     */
    public List<DictDTO> queryFilterTableDictInfo(String table, String text, String code, String filterSql);

    /**
     * 查询指定table的 text code 获取字典，包含text和value
     * @param table
     * @param text
     * @param code
     * @param keyArray
     * @return
     */
    @Deprecated
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray);

    /**
     * 获取所有有效用户
     * @return
     */
    public List<ComboVo> queryAllUser();

    /**
     * 获取所有有效用户 带参
     * userIds 默认选中用户
     * @return
     */
    public JSONObject queryAllUser(String[] userIds, int pageNo, int pageSize);

    /**
     * 获取所有有效用户 拼接查询条件
     *
     * @return
     */
    List<JSONObject> queryAllUser(Wrapper wrapper);

    /**
     * 获取所有角色
     * @return
     */
    public List<ComboVo> queryAllRole();

    /**
     * 获取所有角色 带参
     * roleIds 默认选中角色
     * @return
     */
    public List<ComboVo> queryAllRole(String[] roleIds );

    /**
     * 通过用户账号查询角色Id集合
     * @param username
     * @return
     */
    public List<String> getRoleIdsByUsername(String username);

    /**
     * 通过部门编号查询部门id
     * @param orgCode
     * @return
     */
    public String getDepartIdsByOrgCode(String orgCode);

    /**
     * 查询上一级部门
     * @param departId
     * @return
     */
    public DictDTO getParentDepartId(String departId);

    /**
     * 查询所有部门
     * @return
     */
    public List<SysDepartVo> getAllSysDepart();

    /**
     * 根据部门Id获取部门负责人
     * @param deptId
     * @return
     */
    public List<String> getDeptHeadByDepId(String deptId);

    /**
     * 给指定用户发消息
     * @param userIds
     * @param cmd
     */
    public void sendWebSocketMsg(String[] userIds, String cmd);
}
