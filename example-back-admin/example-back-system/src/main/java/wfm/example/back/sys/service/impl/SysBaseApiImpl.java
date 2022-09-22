package wfm.example.back.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import wfm.example.back.common.config.SystemConfig;
import wfm.example.back.sys.mapper.*;
import wfm.example.back.sys.model.*;
import wfm.example.back.common.dto.BusMessageDTO;
import wfm.example.back.common.dto.LoginUserDto;
import wfm.example.back.common.service.ISysBaseAPI;
import wfm.example.back.common.websocket.WebSocket;
import wfm.example.back.common.constant.CacheConstant;
import wfm.example.back.common.constant.CommonConstant;
import wfm.example.back.common.constant.DataBaseConstant;
import wfm.example.back.common.constant.WebsocketConstant;
import wfm.example.back.common.dto.DictDTO;
import wfm.example.back.common.enums.SysAnnmentTypeEnum;
import wfm.example.back.common.exception.BizException;
import wfm.example.back.common.util.ObjectConvertUtils;
import wfm.example.back.common.vo.ComboVo;
import wfm.example.back.common.vo.SysCategoryVo;
import wfm.example.back.common.vo.SysDepartVo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SysBaseAPI实现类
 * @author 吴福明
 */

@Slf4j
@Service
public class SysBaseApiImpl implements ISysBaseAPI {

    /** 当前系统数据库类型 */
    private static String DB_TYPE = "";
    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysDepartMapper sysDepartMapper;
    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysAnnouncementMapper sysAnnouncementMapper;
    @Autowired
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysDepartMapper departMapper;
    @Autowired
    private SysCategoryMapper categoryMapper;


    @Override
    @Cacheable(cacheNames = CacheConstant.SYS_USERS_CACHE, key="#username")
    public LoginUserDto getUserByName(String username) {
        if(ObjectConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUserDto user = new LoginUserDto();
        SysUser sysUser = userMapper.getUserByName(username);
        if(sysUser==null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, user);
        return user;
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    @Override
    public List<String> getDepartIdsByUsername(String username) {
        List<SysDepart> list = sysDepartMapper.queryDepartsByUsername(username);
        List<String> result = new ArrayList<>(list.size());
        for (SysDepart depart : list) {
            result.add(depart.getId());
        }
        return result;
    }

    @Override
    public List<String> getDepartNamesByUsername(String username) {
        List<SysDepart> list = sysDepartMapper.queryDepartsByUsername(username);
        List<String> result = new ArrayList<>(list.size());
        for (SysDepart depart : list) {
            result.add(depart.getDepartName());
        }
        return result;
    }

    @Override
    public String getDatabaseType() throws SQLException {
        if(ObjectConvertUtils.isNotEmpty(DB_TYPE)){
            return DB_TYPE;
        }
        DataSource dataSource = SystemConfig.getApplicationContext().getBean(DataSource.class);
        return getDatabaseTypeByDataSource(dataSource);
    }

    /**
     * 获取数据库类型
     * @param dataSource
     * @return
     * @throws SQLException
     */
    private String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException{
        if("".equals(DB_TYPE)) {
            Connection connection = dataSource.getConnection();
            try {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toLowerCase();
                if(dbType.indexOf("mysql")>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_MYSQL;
                }else if(dbType.indexOf("oracle")>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_ORACLE;
                }else if(dbType.indexOf("sqlserver")>=0||dbType.indexOf("sql server")>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_SQLSERVER;
                }else if(dbType.indexOf("postgresql")>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_POSTGRESQL;
                }else {
                    throw new BizException("数据库类型:["+dbType+"]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }finally {
                connection.close();
            }
        }
        return DB_TYPE;

    }

    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#code")
    public List<DictDTO> queryDictItemsByCode(String code) {
        return sysDictMapper.queryDictItemsByCode(code);
    }

    @Override
    public List<DictDTO> queryAllDict() {
        // 查询并排序
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<SysDict>();
        queryWrapper.orderByAsc("create_time");
        List<SysDict> dicts = sysDictMapper.selectList(queryWrapper);
        // 封装成 model
        List<DictDTO> list = new ArrayList<DictDTO>();
        for (SysDict dict : dicts) {
            list.add(new DictDTO(dict.getDictCode(), dict.getDictName()));
        }

        return list;
    }

    @Override
    public List<SysCategoryVo> queryAllDSysCategory() {
        List<SysCategory> ls = categoryMapper.selectList(null);
        List<SysCategoryVo> res = ObjectConvertUtils.entityListToModelList(ls,SysCategoryVo.class);
        return res;
    }

    @Override
    public List<DictDTO> queryTableDictItemsByCode(String table, String text, String code) {
        return sysDictMapper.queryTableDictItemsByCode(table, text, code);
    }

    @Override
    public List<DictDTO> queryAllDepartBackDictModel() {
        return sysDictMapper.queryAllDepartBackDictModel();
    }

    @Override
    public List<JSONObject> queryAllDepart(Wrapper wrapper) {
        return JSON.parseArray(JSON.toJSONString(sysDepartMapper.selectList(wrapper))).toJavaList(JSONObject.class);
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent) {
        this.sendSysAnnouncement(fromUser, toUser, title, msgContent, CommonConstant.MSG_CATEGORY_2);
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map, String templateCode) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateMapper.selectByCode(templateCode);
        if(sysSmsTemplates==null||sysSmsTemplates.size()==0){
            throw new BizException("消息模板不存在，模板编码："+templateCode);
        }
        SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
        //模板标题
        title = title==null?sysSmsTemplate.getTemplateName():title;
        //模板内容
        String content = sysSmsTemplate.getTemplateContent();
        if(map!=null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String str = "${" + entry.getKey() + "}";
                title = title.replace(str, entry.getValue());
                content = content.replace(str, entry.getValue());
            }
        }

        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(content);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for(int i=0;i<userIds.length;i++) {
            if(ObjectConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if(sysUser==null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConstant.MSG_CMD, WebsocketConstant.CMD_USER);
                obj.put(WebsocketConstant.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConstant.MSG_ID, announcement.getId());
                obj.put(WebsocketConstant.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, Map<String, String> map, String templateCode, String busType, String busId) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateMapper.selectByCode(templateCode);
        if(sysSmsTemplates==null||sysSmsTemplates.size()==0){
            throw new BizException("消息模板不存在，模板编码："+templateCode);
        }
        SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
        //模板标题
        title = title==null?sysSmsTemplate.getTemplateName():title;
        //模板内容
        String content = sysSmsTemplate.getTemplateContent();
        if(map!=null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String str = "${" + entry.getKey() + "}";
                title = title.replace(str, entry.getValue());
                content = content.replace(str, entry.getValue());
            }
        }
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(content);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        announcement.setBusId(busId);
        announcement.setBusType(busType);
        announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
        announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for(int i=0;i<userIds.length;i++) {
            if(ObjectConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if(sysUser==null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConstant.MSG_CMD, WebsocketConstant.CMD_USER);
                obj.put(WebsocketConstant.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConstant.MSG_ID, announcement.getId());
                obj.put(WebsocketConstant.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }

    @Override
    public void sendBusAnnouncement(BusMessageDTO message) {
        sendBusAnnouncement(message.getFromUser(),
                message.getToUser(),
                message.getTitle(),
                message.getContent(),
                message.getCategory(),
                message.getBusType(),
                message.getBusId());
    }

    @Override
    public String parseTemplateByCode(String templateCode, Map<String, String> map) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateMapper.selectByCode(templateCode);
        if(sysSmsTemplates==null||sysSmsTemplates.size()==0){
            throw new BizException("消息模板不存在，模板编码："+templateCode);
        }
        SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
        //模板内容
        String content = sysSmsTemplate.getTemplateContent();
        if(map!=null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String str = "${" + entry.getKey() + "}";
                content = content.replace(str, entry.getValue());
            }
        }
        return content;
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(setMsgCategory);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for(int i=0;i<userIds.length;i++) {
            if(ObjectConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if(sysUser==null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConstant.MSG_CMD, WebsocketConstant.CMD_USER);
                obj.put(WebsocketConstant.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConstant.MSG_ID, announcement.getId());
                obj.put(WebsocketConstant.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory, String busType, String busId) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(setMsgCategory);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        announcement.setBusId(busId);
        announcement.setBusType(busType);
        announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
        announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for(int i=0;i<userIds.length;i++) {
            if(ObjectConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if(sysUser==null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConstant.MSG_CMD, WebsocketConstant.CMD_USER);
                obj.put(WebsocketConstant.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConstant.MSG_ID, announcement.getId());
                obj.put(WebsocketConstant.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }

    @Override
    public void updateSysAnnounceReadFlag(String busType, String busId, String userId) {
        SysAnnouncement announcement = sysAnnouncementMapper.selectOne(new QueryWrapper<SysAnnouncement>().eq("bus_type",busType).eq("bus_id",busId));
        if(announcement != null){
            LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
            updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
            updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
            updateWrapper.last("where annt_id ='"+announcement.getId()+"' and user_id ='"+userId+"'");
            SysAnnouncementSend announcementSend = new SysAnnouncementSend();
            sysAnnouncementSendMapper.update(announcementSend, updateWrapper);
        }
    }

    @Override
    public List<DictDTO> queryFilterTableDictInfo(String table, String text, String code, String filterSql) {
        return sysDictMapper.queryTableDictItemsByCodeAndFilter(table,text,code,filterSql);
    }

    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        if(ArrayUtils.isEmpty(keyArray)){
            return null;
        }
        List<DictDTO> dicts = sysDictMapper.queryTableDictByKeys(table, text, code, keyArray);
        List<String> texts = new ArrayList<>(dicts.size());
        // 查询出来的顺序可能是乱的，需要排个序
        for (String key : keyArray) {
            for (DictDTO dict : dicts) {
                if (key.equals(dict.getValue())) {
                    texts.add(dict.getText());
                    break;
                }
            }
        }
        return texts;
    }

    @Override
    public List<ComboVo> queryAllUser() {
        List<ComboVo> list = new ArrayList<ComboVo>();
        List<SysUser> userList = userMapper.selectList(new QueryWrapper<SysUser>().eq("status",1).eq("del_flag",0));
        for(SysUser user : userList){
            ComboVo model = new ComboVo();
            model.setTitle(user.getRealname());
            model.setId(user.getId());
            model.setUsername(user.getUsername());
            list.add(model);
        }
        return list;
    }

    @Override
    public JSONObject queryAllUser(String[] userIds, int pageNo, int pageSize) {
        JSONObject json = new JSONObject();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status",1).eq("del_flag",0);
        List<ComboVo> list = new ArrayList<ComboVo>();
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        IPage<SysUser> pageList = userMapper.selectPage(page, queryWrapper);
        for(SysUser user : pageList.getRecords()){
            ComboVo model = new ComboVo();
            model.setUsername(user.getUsername());
            model.setTitle(user.getRealname());
            model.setId(user.getId());
            model.setEmail(user.getEmail());
            if(ObjectConvertUtils.isNotEmpty(userIds)){
                for(int i = 0; i<userIds.length;i++){
                    if(userIds[i].equals(user.getId())){
                        model.setChecked(true);
                    }
                }
            }
            list.add(model);
        }
        json.put("list",list);
        json.put("total",pageList.getTotal());
        return json;
    }

    @Override
    public List<JSONObject> queryAllUser(Wrapper wrapper) {
        return JSON.parseArray(JSON.toJSONString(userMapper.selectList(wrapper))).toJavaList(JSONObject.class);
    }

    @Override
    public List<ComboVo> queryAllRole() {
        List<ComboVo> list = new ArrayList<ComboVo>();
        List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<SysRole>());
        for(SysRole role : roleList){
            ComboVo model = new ComboVo();
            model.setTitle(role.getRoleName());
            model.setId(role.getId());
            list.add(model);
        }
        return list;
    }

    @Override
    public List<ComboVo> queryAllRole(String[] roleIds) {
        List<ComboVo> list = new ArrayList<ComboVo>();
        List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<SysRole>());
        for(SysRole role : roleList){
            ComboVo model = new ComboVo();
            model.setTitle(role.getRoleName());
            model.setId(role.getId());
            model.setRoleCode(role.getRoleCode());
            if(ObjectConvertUtils.isNotEmpty(roleIds)) {
                for (int i = 0; i < roleIds.length; i++) {
                    if (roleIds[i].equals(role.getId())) {
                        model.setChecked(true);
                    }
                }
            }
            list.add(model);
        }
        return list;
    }

    @Override
    public List<String> getRoleIdsByUsername(String username) {
        return sysUserRoleMapper.getRoleIdByUserName(username);
    }

    @Override
    public String getDepartIdsByOrgCode(String orgCode) {
        return departMapper.queryDepartIdByOrgCode(orgCode);
    }

    @Override
    public DictDTO getParentDepartId(String departId) {
        SysDepart depart = departMapper.getParentDepartId(departId);
        DictDTO model = new DictDTO(depart.getId(),depart.getParentId());
        return model;
    }

    @Override
    public List<SysDepartVo> getAllSysDepart() {
        List<SysDepartVo> departModelList = new ArrayList<SysDepartVo>();
        List<SysDepart> departList = departMapper.selectList(new QueryWrapper<SysDepart>().eq("del_flag","0"));
        for(SysDepart depart : departList){
            SysDepartVo model = new SysDepartVo();
            BeanUtils.copyProperties(depart,model);
            departModelList.add(model);
        }
        return departModelList;
    }

    @Override
    public List<String> getDeptHeadByDepId(String deptId) {
        List<SysUser> userList = userMapper.selectList(new QueryWrapper<SysUser>().like("depart_ids",deptId).eq("status",1).eq("del_flag",0));
        List<String> list = new ArrayList<>();
        for(SysUser user : userList){
            list.add(user.getUsername());
        }
        return list;
    }

    @Override
    public void sendWebSocketMsg(String[] userIds, String cmd) {
        JSONObject obj = new JSONObject();
        obj.put(WebsocketConstant.MSG_CMD, cmd);
        webSocket.sendMoreMessage(userIds, obj.toJSONString());
    }

    /**
     * 发消息 带业务参数
     * @param fromUser
     * @param toUser
     * @param title
     * @param msgContent
     * @param setMsgCategory
     * @param busType
     * @param busId
     */
    private void sendBusAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory, String busType, String busId) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory(setMsgCategory);
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        announcement.setBusId(busId);
        announcement.setBusType(busType);
        announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
        announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for(int i=0;i<userIds.length;i++) {
            if(ObjectConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if(sysUser==null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConstant.MSG_CMD, WebsocketConstant.CMD_USER);
                obj.put(WebsocketConstant.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConstant.MSG_ID, announcement.getId());
                obj.put(WebsocketConstant.MSG_TXT, announcement.getTitile());
                webSocket.sendOneMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }
}
