package wfm.example.back.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Jwt认证token类
 * @author 吴福明
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private UserDetails principal;
    private String credentials;
    private String token;
    private Boolean isRefresh;

    public JwtAuthenticationToken(String token){
        super(Collections.emptyList());
        this.token = token;
        this.isRefresh = false;
    }

    public JwtAuthenticationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities,Boolean isRefresh){
        super(authorities);
        this.principal = principal;
        this.token = token;
        this.setAuthenticated(true);
        this.isRefresh = isRefresh;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setDetails(Object details){
        super.setDetails(details);
        this.setAuthenticated(true);
    }

    public String getToken(){
        return token;
    }

    public Boolean getRefresh() {
        return isRefresh;
    }
}
