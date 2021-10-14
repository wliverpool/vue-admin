package wfm.example.back.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import wfm.example.back.redis.RedisDao;
import wfm.example.back.vo.JwtUser;
import wfm.example.common.constant.CacheConstant;
import wfm.example.common.constant.CommonConstant;
import wfm.example.common.exception.BizException;
import wfm.example.common.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt退出token
 * @author 吴福明
 */

@Slf4j
public class TokenClearLogoutHandler implements LogoutHandler {

    private RedisDao redisDao;

    public TokenClearLogoutHandler(RedisDao redisDao) {
        this.redisDao = redisDao;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Result<JSONObject> result = new Result<JSONObject>();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        if(authentication == null) {
            return;
        }
        try {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            String token = jwtAuthenticationToken.getToken();
            String cacheToken = (String) redisDao.get(CommonConstant.PREFIX_USER_TOKEN + token);
            if (StringUtils.isEmpty(token) || !token.equals(cacheToken)){
                result = result.error500("退出登录失败！");
                response.getWriter().write(JSON.toJSONString(result));
            }
            JwtUser jwtUser = (JwtUser) jwtAuthenticationToken.getPrincipal();
            log.info(" 用户名:" + jwtUser.getRealname()+",退出成功！ ");
            //清空用户登录Token缓存
            redisDao.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录权限缓存
            //redisDao.del(CommonConstant.PREFIX_USER_AUTH_CACHE + jwtUser.getId());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisDao.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, jwtUser.getUsername()));
            result = result.success("退出登录成功！");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e){
            log.error(e.getMessage(), e);
            throw new BizException(e.getMessage());
        }

    }

}
