package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wfm.example.back.mapper.SysDepartRoleMapper;
import wfm.example.back.model.SysDepartRole;
import wfm.example.back.service.ISysDepartRoleService;

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