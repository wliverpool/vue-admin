package wfm.example.back.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import wfm.example.back.sys.model.SysPermissionDataRule;

/**
 * 权限规则 Mapper 接口
 * @author 吴福明
 */
public interface SysPermissionDataRuleMapper extends BaseMapper<SysPermissionDataRule> {

    /**
     * 根据用户名和权限id查询
     * @param username
     * @param permissionId
     * @return
     */
    public List<String> queryDataRuleIds(@Param("username") String username,@Param("permissionId") String permissionId);

}
