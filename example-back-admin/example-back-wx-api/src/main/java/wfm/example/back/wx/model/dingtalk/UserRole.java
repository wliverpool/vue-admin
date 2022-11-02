package wfm.example.back.wx.model.dingtalk;

/**
 * 角色
 *
 * @author 吴福明
 */
public class UserRole {

    /**
     * 角色ID
     */
    private Number id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色组名称
     */
    private String group_name;

    public Number getId() {
        return id;
    }

    public UserRole setId(Number id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserRole setName(String name) {
        this.name = name;
        return this;
    }

    public String getGroup_name() {
        return group_name;
    }

    public UserRole setGroup_name(String group_name) {
        this.group_name = group_name;
        return this;
    }

}
