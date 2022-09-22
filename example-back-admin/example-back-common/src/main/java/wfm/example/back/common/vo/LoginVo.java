package wfm.example.back.common.vo;

import lombok.Data;

/**
 * 登录表单vo
 * @author 吴福明
 */

@Data
public class LoginVo {

    private String username;
    private String password;
    private String captcha;
    private String checkKey;

}
