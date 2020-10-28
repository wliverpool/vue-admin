package wfm.example.back.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import wfm.example.back.model.SysUserDepart;


/**
 * 用户部门Mapper
 * @author 吴福明
 */
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart>{

    List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);
}
