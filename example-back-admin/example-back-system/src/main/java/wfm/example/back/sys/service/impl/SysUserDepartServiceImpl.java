package wfm.example.back.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wfm.example.back.sys.mapper.SysDepartMapper;
import wfm.example.back.sys.mapper.SysUserDepartMapper;
import wfm.example.back.sys.mapper.SysUserMapper;
import wfm.example.back.sys.model.SysDepart;
import wfm.example.back.sys.model.SysUser;
import wfm.example.back.sys.model.SysUserDepart;
import wfm.example.back.sys.service.ISysUserDepartService;
import wfm.example.back.common.dto.DepartIdDTO;
import wfm.example.back.common.util.ObjectConvertUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户部门表实现类
 * @author 吴福明
 */
@Service
@Slf4j
public class SysUserDepartServiceImpl extends ServiceImpl<SysUserDepartMapper, SysUserDepart> implements ISysUserDepartService {

    @Resource
    private SysDepartMapper sysDepartMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 根据用户id查询部门信息
     */
    @Override
    public List<DepartIdDTO> queryDepartIdsOfUser(String userId) {
        LambdaQueryWrapper<SysUserDepart> queryUDep = new LambdaQueryWrapper<SysUserDepart>();
        LambdaQueryWrapper<SysDepart> queryDep = new LambdaQueryWrapper<SysDepart>();
        try {
            queryUDep.eq(SysUserDepart::getUserId, userId);
            List<String> depIdList = new ArrayList<>();
            List<DepartIdDTO> depIdModelList = new ArrayList<>();
            List<SysUserDepart> userDepList = this.list(queryUDep);
            if(userDepList != null && userDepList.size() > 0) {
                for(SysUserDepart userDepart : userDepList) {
                    depIdList.add(userDepart.getDepId());
                }
                queryDep.in(SysDepart::getId, depIdList);
                List<SysDepart> depList = sysDepartMapper.selectList(queryDep);
                if(depList != null || depList.size() > 0) {
                    for(SysDepart depart : depList) {
                        depIdModelList.add(depart.convertByUserDepart());
                    }
                }
                return depIdModelList;
            }
        }catch(Exception e) {
            e.fillInStackTrace();
        }
        return null;


    }


    /**
     * 根据部门id查询用户信息
     */
    @Override
    public List<SysUser> queryUserByDepId(String depId) {
        LambdaQueryWrapper<SysUserDepart> queryUDep = new LambdaQueryWrapper<SysUserDepart>();
        queryUDep.eq(SysUserDepart::getDepId, depId);
        List<String> userIdList = new ArrayList<>();
        List<SysUserDepart> uDepList = this.list(queryUDep);
        if(uDepList != null && uDepList.size() > 0) {
            for(SysUserDepart uDep : uDepList) {
                userIdList.add(uDep.getUserId());
            }
            List<SysUser> userList = (List<SysUser>) sysUserMapper.selectBatchIds(userIdList);
            //update-begin-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
            for (SysUser sysUser : userList) {
                sysUser.setSalt("");
                sysUser.setPassword("");
            }
            //update-end-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
            return userList;
        }
        return new ArrayList<SysUser>();
    }

    /**
     * 根据部门code，查询当前部门和下级部门的 用户信息
     */
    @Override
    public List<SysUser> queryUserByDepCode(String depCode,String realname) {
        LambdaQueryWrapper<SysDepart> queryByDepCode = new LambdaQueryWrapper<SysDepart>();
        queryByDepCode.likeRight(SysDepart::getOrgCode,depCode);
        List<SysDepart> sysDepartList = sysDepartMapper.selectList(queryByDepCode);
        List<String> depIds = sysDepartList.stream().map(SysDepart::getId).collect(Collectors.toList());

        LambdaQueryWrapper<SysUserDepart> queryUDep = new LambdaQueryWrapper<SysUserDepart>();
        queryUDep.in(SysUserDepart::getDepId, depIds);
        List<String> userIdList = new ArrayList<>();
        List<SysUserDepart> uDepList = this.list(queryUDep);
        if(uDepList != null && uDepList.size() > 0) {
            for(SysUserDepart uDep : uDepList) {
                userIdList.add(uDep.getUserId());
            }
            LambdaQueryWrapper<SysUser> queryUser = new LambdaQueryWrapper<SysUser>();
            queryUser.in(SysUser::getId,userIdList);
            if(ObjectConvertUtils.isNotEmpty(realname)){
                queryUser.like(SysUser::getRealname,realname.trim());
            }
            List<SysUser> userList = (List<SysUser>) sysUserMapper.selectList(queryUser);
            //update-begin-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
            for (SysUser sysUser : userList) {
                sysUser.setSalt("");
                sysUser.setPassword("");
            }
            //update-end-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
            return userList;
        }
        return new ArrayList<SysUser>();
    }

}
