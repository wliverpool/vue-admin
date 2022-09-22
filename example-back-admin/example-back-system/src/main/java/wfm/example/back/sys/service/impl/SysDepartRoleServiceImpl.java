package wfm.example.back.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wfm.example.back.sys.mapper.SysDepartRoleMapper;
import wfm.example.back.sys.model.SysDepartRole;
import wfm.example.back.sys.service.ISysDepartRoleService;

import java.util.List;

/**
 * 部门角色
 * @author: 吴福明
 */

@Service
public class SysDepartRoleServiceImpl extends ServiceImpl<SysDepartRoleMapper, SysDepartRole> implements ISysDepartRoleService {

    @Override
    public List<SysDepartRole> queryDeptRoleByDeptAndUser(String orgCode, String userId) {
        return this.baseMapper.queryDeptRoleByDeptAndUser(orgCode,userId);
    }
}