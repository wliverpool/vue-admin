package wfm.example.back.wx.model.qywx;

import com.alibaba.fastjson.JSONObject;

/**
 * 企业微信--user
 * @author 吴福明
 *
 */
public class User {

    /**
     * 成员UserID。对应管理端的帐号，企业内必须唯一。长度为1~64个字节
     */
    private String userid;

    /**
     * 成员名称。长度为1~64个字节
     */
    private String name;

    /**
     * 成员所属部门id列表
     */
    private Integer[] department;

    /**
     * 职位信息。长度为0~64个字节
     */
    private String position;

    /**
     * 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空  创建时必须要
     */
    private String mobile;

    /**
     * 性别。1表示男性，2表示女性
     */
    private String gender;

    /**
     * 邮箱。长度为0~64个字节。企业内必须唯一
     */
    private String email;

    /**
     * 微信号。企业内必须唯一。（注意：是微信号，不是微信的名字）
     */
    private String weixinid;

    /**
     * 启用/禁用成员。1表示启用成员，0表示禁用成员
     */
    private Integer enable;

    /**
     * 成员头像的mediaid，通过多媒体接口上传图片获得的mediaid
     */
    private String avatar_mediaid;

    /**
     * 头像url。注：如果要获取小图将url最后的"/0"改成"/64"即可   创建的时候不需要这个字段 也可以使用transient来取消序列化
     */
    private String avatar;

    /**
     * 关注状态 1=已关注，2=已禁用，4=未关注   创建的时候不需要这个字段 也可以使用transient来取消序列化
     */
    private Integer status;

    /**
     * 座机号
     */
    private String telephone;

    /**
     * 个数必须和参数department的个数一致，表示在所在的部门内是否为上级。1表示为上级，0表示非上级。在审批等应用里可以用来标识上级审批人
     */
    private Integer[] is_leader_in_dept;

    /**
     * 扩展属性。扩展属性需要在WEB管理端创建后才生效，否则忽略未知属性的赋值
     */
    private JSONObject extattr;

    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer[] getDepartment() {
        return department;
    }
    public void setDepartment(Integer[] department) {
        this.department = department;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getWeixinid() {
        return weixinid;
    }
    public void setWeixinid(String weixinid) {
        this.weixinid = weixinid;
    }
    public Integer getEnable() {
        return enable;
    }
    public void setEnable(Integer enable) {
        this.enable = enable;
    }
    public String getAvatar_mediaid() {
        return avatar_mediaid;
    }
    public void setAvatar_mediaid(String avatar_mediaid) {
        this.avatar_mediaid = avatar_mediaid;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public JSONObject getExtattr() {
        return extattr;
    }
    public void setExtattr(JSONObject extattr) {
        this.extattr = extattr;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public Integer[] getIs_leader_in_dept() {
        return is_leader_in_dept;
    }
    public void setIs_leader_in_dept(Integer[] is_leader_in_dept) {
        this.is_leader_in_dept = is_leader_in_dept;
    }

}
