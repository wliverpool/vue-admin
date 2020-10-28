package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wfm.example.back.mapper.SysUserRoleMapper;
import wfm.example.back.model.SysUserRole;
import wfm.example.back.service.ISysUserRoleService;

/**
 * 用户角色表 服务实现类
 * @author 吴福明
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

}
