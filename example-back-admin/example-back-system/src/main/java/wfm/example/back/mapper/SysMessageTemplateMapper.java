package wfm.example.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import wfm.example.back.model.SysMessageTemplate;

import java.util.List;

/**
 * 消息模板
 * @author: 吴福明
 */
public interface SysMessageTemplateMapper extends BaseMapper<SysMessageTemplate> {

    @Select("SELECT * FROM SYS_SMS_TEMPLATE WHERE TEMPLATE_CODE = #{code}")
    List<SysMessageTemplate> selectByCode(String code);

}
