package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import wfm.example.back.common.model.BaseModel;
import wfm.example.back.common.model.BaseStringIDModel;

import java.io.Serializable;

/**
 * 消息模板
 * @author: 吴福明
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_sms_template")
public class SysMessageTemplate extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**模板CODE*/
    private String templateCode;

    /**模板标题*/
    private String templateName;

    /**模板内容*/
    private String templateContent;

    /**模板测试json*/
    private String templateTestJson;

    /**模板类型*/
    private String templateType;

}
