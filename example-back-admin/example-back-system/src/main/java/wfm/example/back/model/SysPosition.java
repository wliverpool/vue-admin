package wfm.example.back.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import wfm.example.common.aspect.Dict;

import java.util.Date;

/**
 * @Description: 职务表
 * @author: 吴福明
 */
@Data
@TableName("sys_position")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPosition {

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

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
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 组织机构编码
     */
    private String sysOrgCode;
}
