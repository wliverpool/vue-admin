package wfm.example.back.common.config;

import org.csource.fastdfs.ClientGlobal;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

/**
 * 系统配置
 * @author 吴福明
 */

@Configuration
@Component
public class SystemConfig implements ApplicationContextAware {

    /**
     * SprngContext上下文对象实例
     */
    private static ApplicationContext applicationContext;

    @Value("${system.upload.fast-dfs.connect-timeout}")
    private Integer fastDFSConnectTimeout;

    @Value("${system.upload.fast-dfs.network-timeout}")
    private Integer fastDFSNetworkTimeout;

    @Value("${system.upload.fast-dfs.charset}")
    private String fastDFSCharset;

    @Value("${system.upload.fast-dfs.http.tracker-http-port}")
    private Integer fastDFSHttpTrackerPort;

    @Value("${system.upload.fast-dfs.http.anti-steal-token}")
    private String fastDFSHttpAntiStealToken;

    @Value("${system.upload.fast-dfs.http.secret-key}")
    private String fastDFSHttpSecretKey;

    @Value("${system.upload.fast-dfs.http.browse-base-url}")
    private String fastDFSHttpBrowseBaseUrl;

    @Value("${system.upload.fast-dfs.tracker-server}")
    private String fastDFSTrackerServer;

    @PostConstruct
    public void init() throws Exception{
        // 初始化fastdfs
        Properties props = new Properties();
        props.setProperty("fastdfs.tracker_servers",fastDFSTrackerServer);
        props.setProperty("fastdfs.connect_timeout_in_seconds",String.valueOf(fastDFSConnectTimeout));
        props.setProperty("fastdfs.network_timeout_in_seconds",String.valueOf(fastDFSNetworkTimeout));
        props.setProperty("fastdfs.charset",fastDFSCharset);
        props.setProperty("fastdfs.http_anti_steal_token",fastDFSHttpAntiStealToken);
        props.setProperty("fastdfs.http_secret_key",fastDFSHttpSecretKey);
        props.setProperty("fastdfs.http_tracker_http_port",String.valueOf(fastDFSHttpTrackerPort));
        ClientGlobal.initByProperties(props);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SystemConfig.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String getDomain(){
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    public static String getOrigin(){
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader("Origin");
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param       <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param       <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
