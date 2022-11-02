package wfm.example.back.wx.model.dingtalk;

/**
 * 员工在对应的部门中的排序
 *
 * @author 吴福明
 */
public class DeptOrder {

    /**
     * 部门ID
     */
    private Number dept_id;
    /**
     * 员工在部门中的排序
     */
    private Number order;

    public Number getDept_id() {
        return dept_id;
    }

    public DeptOrder setDept_id(Number dept_id) {
        this.dept_id = dept_id;
        return this;
    }

    public Number getOrder() {
        return order;
    }

    public DeptOrder setOrder(Number order) {
        this.order = order;
        return this;
    }

}
