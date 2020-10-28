package wfm.example.back.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import wfm.example.back.config.SystemConfig;
import wfm.example.back.service.ISysBaseAPI;
import wfm.example.back.service.ISysUserService;
import wfm.example.back.util.JwtUtils;
import wfm.example.back.vo.JwtUser;
import wfm.example.common.constant.CommonConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 登录成功之后鉴权filter
 * @author 吴福明
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private RequestMatcher requiresAuthenticationRequestMatcher;
    private List<RequestMatcher> permissiveRequestMatchers;
    private AuthenticationManager authenticationManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager){
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(CommonConstant.X_ACCESS_TOKEN);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
    }

    protected String getJwtToken(HttpServletRequest request){
        return request.getHeader(CommonConstant.X_ACCESS_TOKEN);
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    protected AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    protected boolean permissiveRequest(HttpServletRequest request) {
        if(permissiveRequestMatchers == null) {
            return false;
        }
        for(RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if(permissiveMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public void setPermissiveUrl(String... urls) {
        if(permissiveRequestMatchers == null) {
            permissiveRequestMatchers = new ArrayList<>();
        }
        for(String url : urls) {
            permissiveRequestMatchers .add(new AntPathRequestMatcher(url));
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        /*
        header没带token的，直接放过，因为部分url匿名用户也可以访问.
        如果需要不支持匿名用户的请求没带token，这里放过也没问题，因为SecurityContext中没有认证信息，后面会被权限控制模块拦截
         */
        if (!requiresAuthentication(request, response)){
            chain.doFilter(request, response);
            return;
        }
        Authentication authResult = null;
        AuthenticationException failed = null;
        try {
            String token = getJwtToken(request);
            if (StringUtils.isEmpty(token)) {
                failed = new InsufficientAuthenticationException(("JWT is Empty"));
            } else  {
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(token);
                authResult = this.getAuthenticationManager().authenticate(authToken);
            }
        } catch (InternalAuthenticationServiceException e){
            failed = e;
            logger.error(
                    "An internal error occurred while trying to authenticate the user.",
                    failed);
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        if (null != authResult){
            SecurityContextHolder.getContext().setAuthentication(authResult);
            JwtAuthenticationToken authToken = (JwtAuthenticationToken) authResult;
            if (authToken.getRefresh()) {
                response.setHeader(CommonConstant.X_ACCESS_TOKEN_NEW,authToken.getToken());
            }
        } else  if (!permissiveRequest(request)) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        chain.doFilter(request, response);
    }

}
