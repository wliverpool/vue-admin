package wfm.example.back.vo;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wfm.example.common.constant.CommonConstant;

/**
 * 在线用户信息
 * @author 吴福明
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JwtUser implements UserDetails {

    /**
     * 登录人id
     */
    private String id;

    /**
     * 登录人账号
     */
    private String username;

    /**
     * 登录人名字
     */
    private String realname;

    /**
     * 登录人密码
     */
    private String password;

    /**
     * 登录验证码
     */
    private String checkCode;

    /**
     * 当前登录部门code
     */
    private String orgCode;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    private Integer sex;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 状态(1：正常 2：冻结 ）
     */
    private Integer status;

    private Integer delFlag;

    /**
     * 同步工作流引擎1同步0不同步
     */
    private Integer activitiSync;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *  身份（1 普通员工 2 上级）
     */
    private Integer userIdentity;

    /**
     * 管理部门ids
     */
    private String departIds;

    /**
     * 职务，关联职务表
     */
    private String post;

    /**
     * 座机号
     */
    private String telephone;

    private Collection<? extends GrantedAuthority> authorities;

    private Set<String> permissions;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return CommonConstant.DEL_FLAG_0.equals(this.delFlag);
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return CommonConstant.USER_UNFREEZE.equals(this.status);
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return CommonConstant.DEL_FLAG_0.equals(this.delFlag);
    }

}
