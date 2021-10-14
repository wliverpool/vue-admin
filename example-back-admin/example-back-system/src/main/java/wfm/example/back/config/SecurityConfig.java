package wfm.example.back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import wfm.example.back.redis.RedisDao;
import wfm.example.back.security.LoginAuthenticationFilter;
import wfm.example.back.security.JwtAuthenticationProvider;
import wfm.example.back.security.JwtAuthorizationFilter;
import wfm.example.back.security.TokenClearLogoutHandler;
import wfm.example.common.constant.CommonConstant;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("sysUserServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisDao redisDao;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(jwtAuthenticationProvider()).authenticationProvider(daoAuthenticationProvider()).userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean("daoAuthenticationProvider")
    protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
        //这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        return daoProvider;
    }

    @Bean("jwtAuthenticationProvider")
    protected AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager());
        jwtAuthorizationFilter.setPermissiveUrl("/sys/logout");
        jwtAuthorizationFilter.setAuthenticationManager(authenticationManager());

        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(redisDao);
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        loginAuthenticationFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        http.csrf().disable().sessionManagement().disable().formLogin().disable()
                .headers()/*.addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                    new Header("Access-Control-Allow-Origin","http://localhost:3000"),
                    new Header("Access-Control-Allow-Methods","GET,POST,OPTIONS,PUT,DELETE"),
                    new Header("Access-Control-Allow-Headers", "Content-Type, x-requested-with, Authorization, " + CommonConstant.X_ACCESS_TOKEN)
                )))*/.frameOptions().disable().contentTypeOptions().disable().and()
                .authorizeRequests()
                //可以匿名访问url
                .antMatchers("/cas/client/validateLogin","/sys/randomImage/**","/sys/checkCaptcha","/sys/login","/sys/mLogin","/sys/logout","/thirdLogin/**","/sys/getEncryptedString","/sys/sms","/sys/phoneLogin","/sys/user/checkOnlyUser","/sys/user/register","/sys/user/querySysUser","/sys/user/phoneVerification","/sys/user/passwordChange","/auth/2step-code","/sys/common/static/**",
                                "/sys/common/pdf/**","/generic/**","/","/doc.html","/**/*.js","/**/*.css","/**/*.html","/**/*.svg","/**/*.pdf","/**/*.jpg","/**/*.png","/**/*.ico","/**/*.ttf","/**/*.woff","/**/*.woff2","/actuator/**","/websocket/**","/newsWebsocket/**","/category/**","/visual/**","/map/**","/activiti/**","/diagram-viewer/**","/editor-app/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //需要验证了的用户才能访问
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(loginAuthenticationFilter, LogoutFilter.class)
                .addFilterBefore(jwtAuthorizationFilter, LogoutFilter.class)
                .logout().logoutUrl("/sys/logout").addLogoutHandler(new TokenClearLogoutHandler(redisDao)).logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).and()
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    protected CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","HEAD", "OPTIONS","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        //configuration.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

}
