package wfm.example.back.common.vo;

import lombok.Data;

/**
 * 字典分类vo
 * @author 吴福明
 */

@Data
public class SysCategoryVo {

    /**主键*/
    private java.lang.String id;
    /**父级节点*/
    private java.lang.String pid;
    /**类型名称*/
    private java.lang.String name;
    /**类型编码*/
    private java.lang.String code;

}
