package wfm.example.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import wfm.example.back.model.Demo;

import java.util.List;

/**
 * 测试demo mapper
 * @author: 吴福明
 */
public interface DemoMapper extends BaseMapper<Demo> {

    public List<Demo> getDemoByName(@Param("name") String name);

    /**
     * 查询列表数据 直接传数据权限的sql进行数据过滤
     * @param page
     * @param permissionSql
     * @return
     */
    public IPage<Demo> queryListWithPermission(Page<Demo> page, @Param("permissionSql")String permissionSql);

}