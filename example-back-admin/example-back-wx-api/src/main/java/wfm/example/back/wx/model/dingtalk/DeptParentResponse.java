package wfm.example.back.wx.model.dingtalk;

/**
 * 【返回对象】获取指定用户的所有父部门列表
 *
 * @author 吴福明
 */
public class DeptParentResponse {

    /**
     * 父部门列表。
     */
    private Integer[] parent_dept_id_list;

    public Integer[] getParent_dept_id_list() {
        return parent_dept_id_list;
    }

    public DeptParentResponse setParent_dept_id_list(Integer[] parent_dept_id_list) {
        this.parent_dept_id_list = parent_dept_id_list;
        return this;
    }

}
