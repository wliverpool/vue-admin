package wfm.example.back.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 封装树结构的部门的名称的实体类
 * @author 吴福明
 *
 */
public class DepartIdDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键ID
    private String key;

    // 主键ID
    private String value;

    // 部门名称
    private String title;

    List<DepartIdDTO> children = new ArrayList<>();

    /**
     * 将SysDepartTreeModel的部分数据放在该对象当中
     * @param treeModel
     * @return
     */
    public DepartIdDTO convert(SysDepartTreeDTO treeModel) {
        this.key = treeModel.getId();
        this.value = treeModel.getId();
        this.title = treeModel.getDepartName();
        return this;
    }

    public List<DepartIdDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DepartIdDTO> children) {
        this.children = children;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
