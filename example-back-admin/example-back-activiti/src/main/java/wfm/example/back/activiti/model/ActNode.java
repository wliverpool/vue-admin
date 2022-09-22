package wfm.example.back.activiti.model;

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
 * 流程节点扩展表
 * @Author: 吴福明
 */
@Data
@TableName("act_z_node")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActNode extends BaseStringIDModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**流程定义id*/
	private String procDefId;

	/**delFlag*/
	private Integer delFlag;

	/**节点id*/
	private String nodeId;

	/**节点关联类型 0角色 1用户 2部门 3发起人 4发起人的部门负责人*/
	private Integer type;

	/**关联其他表id*/
	private String relateId;
}
