package wfm.example.back.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import wfm.example.back.common.aspect.Dict;
import wfm.example.back.common.model.BaseStringIDModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 职务表
 * @author: 吴福明
 */
@Data
@TableName("sys_position")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysPosition extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职务编码
     */
    private String code;

    /**
     * 职务名称
     */
    private String name;

    /**
     * 职级
     */
    @Dict(dicCode = "position_rank")
    private String postRank;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 组织机构编码
     */
    private String sysOrgCode;
}
