package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wfm.example.back.config.SystemConfig;
import wfm.example.back.mapper.SysRolePermissionMapper;
import wfm.example.back.model.SysRolePermission;
import wfm.example.back.service.ISysRolePermissionService;
import wfm.example.common.util.IPUtils;
import wfm.example.common.util.ObjectConvertUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 角色权限表 服务实现类
 * @author 吴福明
 */
@Service
@Slf4j
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Override
    public void saveRolePermission(String roleId, String permissionIds) {
        String ip = "";
        try {
            //获取request
            HttpServletRequest request = SystemConfig.getHttpServletRequest();
            //获取IP地址
            ip = IPUtils.getIpAddr(request);
        } catch (Exception e) {
            ip = "127.0.0.1";
        }
        LambdaQueryWrapper<SysRolePermission> query = new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId);
        this.remove(query);
        List<SysRolePermission> list = new ArrayList<SysRolePermission>();
        String[] arr = permissionIds.split(",");
        for (String p : arr) {
            if(ObjectConvertUtils.isNotEmpty(p)) {
                SysRolePermission rolepms = new SysRolePermission(roleId, p);
                rolepms.setOperateDate(new Date());
                rolepms.setOperateIp(ip);
                list.add(rolepms);
            }
        }
        this.saveBatch(list);
    }

    @Override
    public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds) {
        String ip = "";
        try {
            //获取request
            HttpServletRequest request = SystemConfig.getHttpServletRequest();
            //获取IP地址
            ip = IPUtils.getIpAddr(request);
        } catch (Exception e) {
            ip = "127.0.0.1";
        }
        List<String> add = getDiff(lastPermissionIds,permissionIds);
        if(add!=null && add.size()>0) {
            List<SysRolePermission> list = new ArrayList<SysRolePermission>();
            for (String p : add) {
                if(ObjectConvertUtils.isNotEmpty(p)) {
                    SysRolePermission rolepms = new SysRolePermission(roleId, p);
                    rolepms.setOperateDate(new Date());
                    rolepms.setOperateIp(ip);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }

        List<String> delete = getDiff(permissionIds,lastPermissionIds);
        if(delete!=null && delete.size()>0) {
            for (String permissionId : delete) {
                this.remove(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId).eq(SysRolePermission::getPermissionId, permissionId));
            }
        }
    }

    /**
     * 从diff中找出main中没有的元素
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main,String diff){
        if(ObjectConvertUtils.isEmpty(diff)) {
            return null;
        }
        if(ObjectConvertUtils.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }

        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diffArr) {
            if(ObjectConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }

}
