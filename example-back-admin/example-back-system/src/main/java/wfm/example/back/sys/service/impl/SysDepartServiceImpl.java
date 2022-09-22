package wfm.example.back.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wfm.example.back.sys.mapper.*;
import wfm.example.back.sys.model.*;
import wfm.example.back.sys.service.ISysDepartService;
import wfm.example.back.util.FillRuleUtils;
import wfm.example.back.util.FindsDepartsChildrenUtils;
import wfm.example.back.common.constant.CacheConstant;
import wfm.example.back.common.constant.CommonConstant;
import wfm.example.back.common.constant.FillRuleConstant;
import wfm.example.back.common.dto.DepartIdDTO;
import wfm.example.back.common.dto.SysDepartTreeDTO;

import java.util.*;

/**
 * 部门表 服务实现类
 * @author 吴福明
 */

@Service
@Slf4j
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {

    @Autowired
    private SysUserDepartMapper userDepartMapper;
    @Autowired
    private SysDepartRoleMapper sysDepartRoleMapper;
    @Autowired
    private SysDepartPermissionMapper departPermissionMapper;
    @Autowired
    private SysDepartRolePermissionMapper departRolePermissionMapper;
    @Autowired
    private SysDepartRoleUserMapper departRoleUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysDepartTreeDTO> queryMyDeptTreeList(String departIds) {
        //根据部门id获取所负责部门
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        String[] codeArr = this.getMyDeptParentOrgCode(departIds);
        for(int i=0;i<codeArr.length;i++){
            query.or().likeRight(SysDepart::getOrgCode,codeArr[i]);
        }
        query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.orderByAsc(SysDepart::getDepartOrder);
        //将父节点ParentId设为null
        List<SysDepart> listDepts = this.list(query);
        for(int i=0;i<codeArr.length;i++){
            for(SysDepart dept : listDepts){
                if(dept.getOrgCode().equals(codeArr[i])){
                    dept.setParentId(null);
                }
            }
        }
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeDTO> listResult = FindsDepartsChildrenUtils.wrapTreeDataToTreeList(listDepts);
        return listResult;
    }

    /**
     * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
     */
    @Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE)
    @Override
    public List<SysDepartTreeDTO> queryTreeList() {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeDTO> listResult = FindsDepartsChildrenUtils.wrapTreeDataToTreeList(list);
        return listResult;
    }

    @Cacheable(value = CacheConstant.SYS_DEPART_IDS_CACHE)
    @Override
    public List<DepartIdDTO> queryDepartIdTreeList() {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<DepartIdDTO> listResult = FindsDepartsChildrenUtils.wrapTreeDataToDepartIdTreeList(list);
        return listResult;
    }

    /**
     * saveDepartData 对应 add 保存用户在页面添加的新的部门对象数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDepartData(SysDepart sysDepart, String username) {
        if (sysDepart != null && username != null) {
            if (sysDepart.getParentId() == null) {
                sysDepart.setParentId("");
            }
            String s = UUID.randomUUID().toString().replace("-", "");
            sysDepart.setId(s);
            // 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
            // 获取父级ID
            String parentId = sysDepart.getParentId();
            //根据配置规则生成部门编码
            JSONObject formData = new JSONObject();
            formData.put("parentId",parentId);
            String[] codeArray = (String[]) FillRuleUtils.executeRule(FillRuleConstant.DEPART,formData);
            sysDepart.setOrgCode(codeArray[0]);
            String orgType = codeArray[1];
            sysDepart.setOrgType(String.valueOf(orgType));
            sysDepart.setCreateTime(new Date());
            sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            this.save(sysDepart);
        }
    }

    @Override
    @Transactional
    public Boolean updateDepartDataById(SysDepart sysDepart, String username) {
        if (sysDepart != null && username != null) {
            sysDepart.setUpdateTime(new Date());
            sysDepart.setUpdateBy(username);
            this.updateById(sysDepart);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<SysDepartTreeDTO> searchBy(String keyWord, String myDeptSearch, String departIds) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        List<SysDepartTreeDTO> newList = new ArrayList<>();
        //myDeptSearch不为空时为我的部门搜索，只搜索所负责部门
        if(!StringUtils.isEmpty(myDeptSearch)){
            //departIds 为空普通用户或没有管理部门
            if(StringUtils.isEmpty(departIds)){
                return newList;
            }
            //根据部门id获取所负责部门
            String[] codeArr = this.getMyDeptParentOrgCode(departIds);
            for(int i=0;i<codeArr.length;i++){
                query.or().likeRight(SysDepart::getOrgCode,codeArr[i]);
            }
            query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        }
        query.like(SysDepart::getDepartName, keyWord);
        //update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索回显优化--------------------
        SysDepartTreeDTO model = new SysDepartTreeDTO();
        List<SysDepart> departList = this.list(query);
        if(departList.size() > 0) {
            for(SysDepart depart : departList) {
                model = depart.transferToDTO();
                model.setChildren(null);
                //update-end--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索功回显优化----------------------
                newList.add(model);
            }
            return newList;
        }
        return null;
    }

    /**
     * 根据部门id删除并且删除其可能存在的子级任何部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        this.checkChildrenExists(id, idList);
        //清空部门树内存
        boolean ok = this.removeByIds(idList);
        //根据部门id获取部门角色id
        List<String> roleIdList = new ArrayList<>();
        LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
        query.select(SysDepartRole::getId).in(SysDepartRole::getDepartId, idList);
        List<SysDepartRole> depRoleList = sysDepartRoleMapper.selectList(query);
        for(SysDepartRole deptRole : depRoleList){
            roleIdList.add(deptRole.getId());
        }
        //根据部门id删除用户与部门关系
        userDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getDepId,idList));
        //根据部门id删除部门授权
        departPermissionMapper.delete(new LambdaQueryWrapper<SysDepartPermission>().in(SysDepartPermission::getDepartId,idList));
        //根据部门id删除部门角色
        sysDepartRoleMapper.delete(new LambdaQueryWrapper<SysDepartRole>().in(SysDepartRole::getDepartId,idList));
        if(roleIdList != null && roleIdList.size()>0){
            //根据角色id删除部门角色授权
            departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().in(SysDepartRolePermission::getRoleId,roleIdList));
            //根据角色id删除部门角色用户信息
            departRoleUserMapper.delete(new LambdaQueryWrapper<SysDepartRoleUser>().in(SysDepartRoleUser::getDroleId,roleIdList));
        }
        return ok;
    }

    @Override
    public List<SysDepart> queryUserDeparts(String userId) {
        return baseMapper.queryUserDeparts(userId);
    }

    @Override
    public List<SysDepart> queryDepartsByUsername(String username) {
        return baseMapper.queryDepartsByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchWithChildren(List<String> ids) {
        List<String> idList = new ArrayList<String>();
        for(String id: ids) {
            idList.add(id);
            this.checkChildrenExists(id, idList);
        }
        this.removeByIds(idList);
        //根据部门id获取部门角色id
        List<String> roleIdList = new ArrayList<>();
        LambdaQueryWrapper<SysDepartRole> query = new LambdaQueryWrapper<>();
        query.select(SysDepartRole::getId).in(SysDepartRole::getDepartId, idList);
        List<SysDepartRole> depRoleList = sysDepartRoleMapper.selectList(query);
        for(SysDepartRole deptRole : depRoleList){
            roleIdList.add(deptRole.getId());
        }
        //根据部门id删除用户与部门关系
        userDepartMapper.delete(new LambdaQueryWrapper<SysUserDepart>().in(SysUserDepart::getDepId,idList));
        //根据部门id删除部门授权
        departPermissionMapper.delete(new LambdaQueryWrapper<SysDepartPermission>().in(SysDepartPermission::getDepartId,idList));
        //根据部门id删除部门角色
        sysDepartRoleMapper.delete(new LambdaQueryWrapper<SysDepartRole>().in(SysDepartRole::getDepartId,idList));
        if(roleIdList != null && roleIdList.size()>0){
            //根据角色id删除部门角色授权
            departRolePermissionMapper.delete(new LambdaQueryWrapper<SysDepartRolePermission>().in(SysDepartRolePermission::getRoleId,roleIdList));
            //根据角色id删除部门角色用户信息
            departRoleUserMapper.delete(new LambdaQueryWrapper<SysDepartRoleUser>().in(SysDepartRoleUser::getDroleId,roleIdList));
        }
    }

    @Override
    public List<String> getSubDepIdsByDepId(String departId) {
        return this.baseMapper.getSubDepIdsByDepId(departId);
    }

    @Override
    public List<String> getMySubDepIdsByDepId(String departIds) {
        //根据部门id获取所负责部门
        String[] codeArr = this.getMyDeptParentOrgCode(departIds);
        return this.baseMapper.getSubDepIdsByOrgCodes(codeArr);
    }

    /**
     * 获取部门树信息根据关键字
     * @param keyWord
     * @return
     */
    @Override
    public List<SysDepartTreeDTO> queryTreeByKeyWord(String keyWord) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<SysDepartTreeDTO> listResult = FindsDepartsChildrenUtils.wrapTreeDataToTreeList(list);
        List<SysDepartTreeDTO> treelist =new ArrayList<>();
        if(StringUtils.isNotBlank(keyWord)){
            this.getTreeByKeyWord(keyWord,listResult,treelist);
        }else{
            return listResult;
        }
        return treelist;
    }

    /**
     * 根据用户所负责部门ids获取父级部门编码
     * @param departIds
     * @return
     */
    private String[] getMyDeptParentOrgCode(String departIds){
        //根据部门id查询所负责部门
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        query.in(SysDepart::getId, Arrays.asList(departIds.split(",")));
        query.orderByAsc(SysDepart::getOrgCode);
        List<SysDepart> list = this.list(query);
        //查找根部门
        if(list == null || list.size()==0){
            return null;
        }
        String orgCode = this.getMyDeptParentNode(list);
        String[] codeArr = orgCode.split(",");
        return codeArr;
    }

    /**
     * 获取负责部门父节点
     * @param list
     * @return
     */
    private String getMyDeptParentNode(List<SysDepart> list){
        Map<String,String> map = new HashMap<>();
        //1.先将同一公司归类
        for(SysDepart dept : list){
            String code = dept.getOrgCode().substring(0,3);
            if(map.containsKey(code)){
                String mapCode = map.get(code)+","+dept.getOrgCode();
                map.put(code,mapCode);
            }else{
                map.put(code,dept.getOrgCode());
            }
        }
        StringBuffer parentOrgCode = new StringBuffer();
        //2.获取同一公司的根节点
        for(String str : map.values()){
            String[] arrStr = str.split(",");
            parentOrgCode.append(",").append(this.getMinLengthNode(arrStr));
        }
        return parentOrgCode.substring(1);
    }

    /**
     * 获取同一公司中部门编码长度最小的部门
     * @param str
     * @return
     */
    private String getMinLengthNode(String[] str){
        int min =str[0].length();
        String orgCode = str[0];
        for(int i =1;i<str.length;i++){
            if(str[i].length()<=min){
                min = str[i].length();
                orgCode = orgCode+","+str[i];
            }
        }
        return orgCode;
    }

    /**
     * delete 方法调用
     * @param id
     * @param idList
     */
    private void checkChildrenExists(String id, List<String> idList) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getParentId,id);
        List<SysDepart> departList = this.list(query);
        if(departList != null && departList.size() > 0) {
            for(SysDepart depart : departList) {
                idList.add(depart.getId());
                this.checkChildrenExists(depart.getId(), idList);
            }
        }
    }

    /**
     * 根据关键字筛选部门信息
     * @param keyWord
     * @return
     */
    public void getTreeByKeyWord(String keyWord,List<SysDepartTreeDTO> allResult,List<SysDepartTreeDTO>  newResult){
        for (SysDepartTreeDTO model:allResult) {
            if (model.getDepartName().contains(keyWord)){
                newResult.add(model);
                continue;
            }else if(model.getChildren()!=null){
                getTreeByKeyWord(keyWord,model.getChildren(),newResult);
            }
        }
    }
}
