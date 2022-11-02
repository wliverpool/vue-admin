package wfm.example.back.wx.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 微信请求处理类型
 * @author 吴福明
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ReqType {

    public String value() default "";
}