package wfm.example.back.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import wfm.example.back.config.SystemConfig;
import wfm.example.back.redis.RedisDao;
import wfm.example.back.util.JwtUtils;
import wfm.example.back.vo.JwtUser;
import wfm.example.common.constant.CommonConstant;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * jwt验证器,验证JwtAuthenticationToken
 * @author 吴福明
 */
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    /**
     * 刷新间隔5分钟
     */
    private static final int TOKEN_REFRESH_INTERVAL = 300;

    private UserDetailsService userDetailsService;

    public JwtAuthenticationProvider(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((JwtAuthenticationToken)authentication).getToken();
        String username = JwtUtils.getUsername(token);
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (null == jwtUser || !jwtUser.isAccountNonExpired() || !jwtUser.isEnabled()){
            throw new NonceExpiredException("token is expires");
        }
        if (!jwtUser.isAccountNonLocked()){
            throw new NonceExpiredException("user is locked");
        }
        RedisDao redisDao = SystemConfig.getBean(RedisDao.class);
        String cacheToken = (String)redisDao.get(CommonConstant.PREFIX_USER_TOKEN + token);
        Boolean refresh = false;
        if (StringUtils.isNotEmpty(cacheToken) && cacheToken.equals(token) && JwtUtils.verify(token,username,jwtUser.getPassword())){
            log.info("===============token:" + token + "验证通过==========================");
            if (shouldTokenRefresh(JwtUtils.getExpiresAt(token))) {
                String newToken = JwtUtils.sign(username, jwtUser.getPassword());
                // 设置超时时间
                redisDao.set(CommonConstant.PREFIX_USER_TOKEN + token, newToken);
                redisDao.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtils.EXPIRE_TIME * 2 / 1000);
                log.info("===============刷新旧token:" + token + ",新token:" + newToken + "成功==========================");
                token = newToken;
                refresh = true;
            }
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwtUser,token,jwtUser.getAuthorities(),refresh);
            return jwtAuthenticationToken;
        }
        throw new BadCredentialsException("jwt token verify failed");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(JwtAuthenticationToken.class);
    }

    protected boolean shouldTokenRefresh(Date expiresAt){
        LocalDateTime issueTime = LocalDateTime.ofInstant(expiresAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(TOKEN_REFRESH_INTERVAL).isAfter(issueTime);
    }

}
