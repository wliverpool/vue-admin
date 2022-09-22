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
 * 流程定义扩展表
 * @Author: 吴福明
 */
@Data
@TableName("act_z_process")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActZprocess extends BaseStringIDModel implements Serializable {

	/**delFlag*/
	private Integer delFlag;

	/**所属分类*/
	private String categoryId;

	/**流程类目*/
	private String typeId;

	/**排序*/
	private Integer sort;

	/**部署id*/
	private String deploymentId;

	/**描述/备注*/
	private String description;

	/**流程图片名*/
	private String diagramName;

	/**最新版本*/
	private Boolean latest;

	/**流程名称*/
	private String name;

	/**流程标识名称*/
	private String processKey;

	/**流程状态 部署后默认1激活*/
	private Integer status;

	/**版本*/
	private Integer version;

	/**关联业务表名*/
	private String businessTable;

	/**关联前端表单路由名*/
	private String routeName;

	/**授权的角色*/
	private String roles;

	/**流程表单报表ID*/
	private String reportModelId;
}
