package wfm.example.back.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import wfm.example.back.model.SysPermission;
import wfm.example.back.vo.TreeVo;

/**
 * 菜单权限表 Mapper 接口
 * @author 吴福明
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 通过父菜单ID查询子菜单
     * @param parentId
     * @return
     */
    public List<TreeVo> queryListByParentId(@Param("parentId") String parentId);

    /**
     *   根据用户查询用户权限
     */
    public List<SysPermission> queryByUser(@Param("username") String username);

    /**
     *   修改菜单状态字段： 是否子节点
     */
    @Update("update sys_permission set is_leaf=#{leaf} where id = #{id}")
    public int setMenuLeaf(@Param("id") String id,@Param("leaf") int leaf);

    /**
     *   获取模糊匹配规则的数据权限URL
     */
    @Select("SELECT url FROM sys_permission WHERE del_flag = 0 and menu_type = 2 and url like '%*%'")
    public List<String> queryPermissionUrlWithStar();


    /**
     * 根据用户账号查询菜单权限
     * @param sysPermission
     * @param username
     * @return
     */
    public int queryCountByUsername(@Param("username") String username, @Param("permission") SysPermission sysPermission);



}