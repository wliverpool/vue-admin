package wfm.example.back.wx.model.wxuser;

/**
 * 微信服务号关注用户union信息
 * @author 吴福明
 *
 */
public class Wxuser {

    /**用户是否订阅*/
    private java.lang.Integer subscribe;
    /**用户的标识*/
    private java.lang.String openid;
    /**用户的昵称*/
    private java.lang.String nickname;
    /**性别*/
    private java.lang.String sex;
    /**用户所在城市*/
    private java.lang.String city;
    /**用户所在国家*/
    private java.lang.String country;
    /**用户所在省份*/
    private java.lang.String province;
    /**用户的语言zh_CN*/
    private java.lang.String language;
    /**用户头像*/
    private java.lang.String headimgurl;
    /**用户关注时间*/
    private java.lang.String subscribe_time;
    /**用户粉丝唯一ID*/
    private java.lang.String unionid;
    /**用户被打上的标签ID列表*/
    private String[] tagid_list;
    /**粉丝备注*/
    private String remark;
    /**用户所在的分组ID（兼容旧的用户分组接口）*/
    private Integer groupid;

    public Integer getGroupid() {
        return groupid;
    }
    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }
    public String[] getTagid_list() {
        return tagid_list;
    }
    public void setTagid_list(String[] tagid_list) {
        this.tagid_list = tagid_list;
    }
    public java.lang.Integer getSubscribe() {
        return subscribe;
    }
    public void setSubscribe(java.lang.Integer subscribe) {
        this.subscribe = subscribe;
    }
    public java.lang.String getOpenid() {
        return openid;
    }
    public void setOpenid(java.lang.String openid) {
        this.openid = openid;
    }
    public java.lang.String getNickname() {
        return nickname;
    }
    public void setNickname(java.lang.String nickname) {
        this.nickname = nickname;
    }
    public java.lang.String getSex() {
        return sex;
    }
    public void setSex(java.lang.String sex) {
        this.sex = sex;
    }
    public java.lang.String getCity() {
        return city;
    }
    public void setCity(java.lang.String city) {
        this.city = city;
    }
    public java.lang.String getCountry() {
        return country;
    }
    public void setCountry(java.lang.String country) {
        this.country = country;
    }
    public java.lang.String getProvince() {
        return province;
    }
    public void setProvince(java.lang.String province) {
        this.province = province;
    }
    public java.lang.String getLanguage() {
        return language;
    }
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }
    public java.lang.String getHeadimgurl() {
        return headimgurl;
    }
    public void setHeadimgurl(java.lang.String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public java.lang.String getSubscribe_time() {
        return subscribe_time;
    }
    public void setSubscribe_time(java.lang.String subscribe_time) {
        this.subscribe_time = subscribe_time;
    }
    public java.lang.String getUnionid() {
        return unionid;
    }
    public void setUnionid(java.lang.String unionid) {
        this.unionid = unionid;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
