package wfm.example.back.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import wfm.example.back.config.SystemConfig;
import wfm.example.back.model.SysDepart;
import wfm.example.back.redis.RedisDao;
import wfm.example.back.service.ISysDepartService;
import wfm.example.back.service.ISysDictService;
import wfm.example.back.service.ISysUserService;
import wfm.example.back.util.JwtUtils;
import wfm.example.back.vo.JwtUser;
import wfm.example.common.constant.CommonConstant;
import wfm.example.common.exception.BizException;
import wfm.example.common.util.MD5Utils;
import wfm.example.common.vo.LoginVo;
import wfm.example.common.vo.Result;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * jwt认证filter
 * @author 吴福明
 */

@Slf4j
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private RedisDao redisDao;

    public LoginAuthenticationFilter(RedisDao redisDao){
        super(new AntPathRequestMatcher("/sys/login","POST"));
        this.redisDao = redisDao;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {
        try {
            String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
            if (StringUtils.isEmpty(body)){
                throw new NonceExpiredException("登录信息异常!");
            }
            JSONObject jsonObj = null;
            try {
                jsonObj = JSON.parseObject(body);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                throw new NonceExpiredException("登录信息异常!");
            }
            if (StringUtils.isEmpty(jsonObj.getString("username")) ){
                throw new NonceExpiredException("缺少用户名信息!");
            }
            String username = jsonObj.getString("username");
            if (StringUtils.isEmpty(jsonObj.getString("password")) ){
                throw new NonceExpiredException("缺少密码信息!");
            }
            String password = jsonObj.getString("password");
            if (StringUtils.isEmpty(jsonObj.getString("captcha")) ){
                throw new CaptchaException("缺少验证码信息!");
            }
            if (StringUtils.isEmpty(jsonObj.getString("checkKey"))){
                throw new CaptchaException("缺少验证码信息!");
            }
            String checkKey = jsonObj.getString("checkKey");
            String captcha = jsonObj.getString("captcha");
            String lowerCaseCaptcha = captcha.toLowerCase();
            String realKey = MD5Utils.MD5Encode(lowerCaseCaptcha + checkKey, "utf-8");
            Object checkCode = redisDao.get(realKey);
            if (null == checkCode || !checkCode.equals(lowerCaseCaptcha)){
                throw new CaptchaException("验证码验证错误!");
            }
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username,password));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            Result<JSONObject> result = new Result<JSONObject>();
            result = result.error500(e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            try {
                response.getWriter().write(JSON.toJSONString(result));
            } catch (IOException ee){
                logger.error(ee.getMessage(), e);
            }
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException{
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        log.info("jwt user info:" + JSON.toJSONString(jwtUser));
        String token = JwtUtils.sign(jwtUser.getUsername(), jwtUser.getPassword());
        redisDao.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisDao.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtils.EXPIRE_TIME * 2 / 1000);
        JSONObject obj = new JSONObject();
        Result<JSONObject> result = new Result<JSONObject>();
        ISysDepartService sysDepartService = SystemConfig.getBean(ISysDepartService.class);
        ISysUserService sysUserService = SystemConfig.getBean(ISysUserService.class);
        ISysDictService sysDictService = SystemConfig.getBean(ISysDictService.class);
        List<SysDepart> departs = sysDepartService.queryUserDeparts(jwtUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(jwtUser.getUsername(), departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            obj.put("multi_depart", 2);
        }
        obj.put("token", token);
        obj.put("userInfo", jwtUser);
        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        result.success("登录成功");
        setResponse(response);
        response.getWriter().write(JSON.toJSONString(result));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Result<JSONObject> result = new Result<JSONObject>();
        result.error500(failed.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        setResponse(response);
        response.getWriter().write(JSON.toJSONString(result));
    }

    private void setResponse(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");
    }

}
