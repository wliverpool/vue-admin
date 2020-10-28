package wfm.example.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import wfm.example.back.security.CorsInterceptor;

import javax.annotation.Resource;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Resource
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");
    }
}
