package wfm.example.common.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * 重复校验VO
 * @author 吴福明
 */
@Data
public class DuplicateCheckVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段值
     */
    private String fieldVal;

    /**
     * 数据ID
     */
    private String dataId;

}