package wfm.example.back.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Joiner;
import org.springframework.security.core.context.SecurityContextHolder;
import wfm.example.back.config.SystemConfig;
import wfm.example.back.vo.JwtUser;
import wfm.example.common.constant.DataBaseConstant;
import wfm.example.common.exception.BizException;
import wfm.example.common.util.DateUtils;
import wfm.example.common.util.ObjectConvertUtils;
import wfm.example.common.vo.SysUserCacheVo;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * JWT工具类
 * @author 吴福明
 **/
public class JwtUtils {

    /** Token过期时间30分钟（用户登录过期时间是此时间的两倍，以token在reids缓存时间为准） **/
    public static final long EXPIRE_TIME = 30 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的expiresAt信息
     *
     * @return token中包含的expiresAt
     */
    public static Date getExpiresAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);

    }

    /**
     * 根据request中的token获取用户账号
     *
     * @param request
     * @return
     * @throws BizException
     */
    public static String getUserNameByToken(HttpServletRequest request) throws BizException {
        String accessToken = request.getHeader("X-Access-Token");
        String username = getUsername(accessToken);
        if (ObjectConvertUtils.isEmpty(username)) {
            throw new BizException("未获取到用户");
        }
        return username;
    }

    /**
     * 从当前用户中获取变量
     * @param key
     * @param user
     * @return
     */
    public static String getUserSystemData(String key, SysUserCacheVo user) {
        if(user==null) {
            user = DataAuthorUtils.loadUserInfo();
        }
        //#{sys_user_code}%

        // 获取登录用户信息
        JwtUser sysUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();;

        String moshi = "";
        if(key.indexOf("}")!=-1){
            moshi = key.substring(key.indexOf("}")+1);
        }
        String returnValue = null;
        //针对特殊标示处理#{sysOrgCode}，判断替换
        if (key.contains("#{")) {
            key = key.substring(2,key.indexOf("}"));
        } else {
            key = key;
        }
        //替换为系统登录用户帐号
        if (key.equals(DataBaseConstant.SYS_USER_CODE)|| key.toLowerCase().equals(DataBaseConstant.SYS_USER_CODE_TABLE)) {
            if(user==null) {
                returnValue = sysUser.getUsername();
            }else {
                returnValue = user.getSysUserCode();
            }
        }
        //替换为系统登录用户真实名字
        else if (key.equals(DataBaseConstant.SYS_USER_NAME)|| key.toLowerCase().equals(DataBaseConstant.SYS_USER_NAME_TABLE)) {
            if(user==null) {
                returnValue = sysUser.getRealname();
            }else {
                returnValue = user.getSysUserName();
            }
        }

        //替换为系统用户登录所使用的机构编码
        else if (key.equals(DataBaseConstant.SYS_ORG_CODE)|| key.toLowerCase().equals(DataBaseConstant.SYS_ORG_CODE_TABLE)) {
            if(user==null) {
                returnValue = sysUser.getOrgCode();
            }else {
                returnValue = user.getSysOrgCode();
            }
        }
        //替换为系统用户所拥有的所有机构编码
        else if (key.equals(DataBaseConstant.SYS_MULTI_ORG_CODE)|| key.toLowerCase().equals(DataBaseConstant.SYS_MULTI_ORG_CODE_TABLE)) {
            if(user.isOneDepart()) {
                returnValue = user.getSysMultiOrgCode().get(0);
            }else {
                returnValue = Joiner.on(",").join(user.getSysMultiOrgCode());
            }
        }
        //替换为当前系统时间(年月日)
        else if (key.equals(DataBaseConstant.SYS_DATE)|| key.toLowerCase().equals(DataBaseConstant.SYS_DATE_TABLE)) {
            returnValue = DateUtils.formatDate();
        }
        //替换为当前系统时间（年月日时分秒）
        else if (key.equals(DataBaseConstant.SYS_TIME)|| key.toLowerCase().equals(DataBaseConstant.SYS_TIME_TABLE)) {
            returnValue = DateUtils.now();
        }
        //流程状态默认值（默认未发起）
        else if (key.equals(DataBaseConstant.BPM_STATUS)|| key.toLowerCase().equals(DataBaseConstant.BPM_STATUS_TABLE)) {
            returnValue = "1";
        }
        if(returnValue!=null){returnValue = returnValue + moshi;}
        return returnValue;
    }

    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjUzMzY1MTMsInVzZXJuYW1lIjoiYWRtaW4ifQ.xjhud_tWCNYBOg_aRlMgOdlZoWFFKB_givNElHNw3X0";
        System.out.println(JwtUtils.getUsername(token));
    }

}
