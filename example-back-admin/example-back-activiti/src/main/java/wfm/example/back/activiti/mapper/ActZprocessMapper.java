package wfm.example.back.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import wfm.example.back.activiti.model.ActZprocess;

import java.util.List;

/**
 * @Description: 流程定义扩展表
 * @Author: 吴福明
 */
public interface ActZprocessMapper extends BaseMapper<ActZprocess> {

    List<ActZprocess> selectNewestProcess(@Param("processKey") String processKey);
}
