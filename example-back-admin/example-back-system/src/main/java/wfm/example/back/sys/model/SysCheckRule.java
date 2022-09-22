package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import wfm.example.back.common.model.BaseStringIDModel;

import java.io.Serializable;
import java.util.Date;

/**
 * 编码校验规则
 * @author: 吴福明
 */
@Data
@TableName("sys_check_rule")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysCheckRule extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则Code
     */
    private String ruleCode;

    /**
     * 规则JSON
     */
    private String ruleJson;

    /**
     * 规则描述
     */
    private String ruleDescription;

}
