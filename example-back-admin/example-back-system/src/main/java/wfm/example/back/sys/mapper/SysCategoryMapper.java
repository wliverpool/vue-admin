package wfm.example.back.sys.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import wfm.example.back.sys.model.SysCategory;
import wfm.example.back.common.dto.TreeSelectDTO;

/**
 * 分类字典
 * @Author: 吴福明
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {

    /**
     *  根据父级ID查询树节点数据
     * @param pid
     * @return
     */
    public List<TreeSelectDTO> queryListByPid(@Param("pid")  String pid, @Param("query") Map<String, String> query);

    @Select("SELECT ID FROM sys_category WHERE CODE = #{code,jdbcType=VARCHAR}")
    public String queryIdByCode(@Param("code")  String code);


}
