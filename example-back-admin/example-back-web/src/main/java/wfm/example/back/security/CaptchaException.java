package wfm.example.back.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 二维码验证异常
 * @author 吴福明
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg) {
        super(msg);
    }

}