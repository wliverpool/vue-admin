package wfm.example.back.common.util;

import org.springframework.util.StringUtils;
import wfm.example.back.common.config.SystemConfig;
import wfm.example.back.common.dto.SysPermissionDataRuleDTO;
import wfm.example.back.common.vo.SysUserCacheVo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存中数据权限查询规则容器工具类
 * @author: 吴福明
 */

public class DataAuthorUtils {

    public static final String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";

    public static final String MENU_DATA_AUTHOR_RULE_SQL = "MENU_DATA_AUTHOR_RULE_SQL";

    public static final String SYS_USER_INFO = "SYS_USER_INFO";

    /**
     * 往链接请求里面，传入数据查询条件
     * @param request
     * @param dataRules
     */
    public static synchronized void installDataSearchConditon(HttpServletRequest request, List<SysPermissionDataRuleDTO> dataRules) {
        @SuppressWarnings("unchecked")
        // 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
        List<SysPermissionDataRuleDTO> list = (List<SysPermissionDataRuleDTO>)loadDataSearchConditon();
        if (list==null) {
            // 2.如果不存在，则new一个list
            list = new ArrayList<SysPermissionDataRuleDTO>();
        }
        for (SysPermissionDataRuleDTO tsDataRule : dataRules) {
            list.add(tsDataRule);
        }
        // 3.往list里面增量存指
        request.setAttribute(MENU_DATA_AUTHOR_RULES, list);
    }

    /**
     * 获取请求对应的数据权限规则
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized List<SysPermissionDataRuleDTO> loadDataSearchConditon() {
        return (List<SysPermissionDataRuleDTO>) SystemConfig.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULES);

    }

    /**
     * 获取请求对应的数据权限SQL
     * @return
     */
    public static synchronized String loadDataSearchConditonSQLString() {
        return (String) SystemConfig.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULE_SQL);
    }

    /**
     * 往链接请求里面，传入数据查询条件
     * @param request
     * @param sql
     */
    public static synchronized void installDataSearchConditon(HttpServletRequest request, String sql) {
        String ruleSql = (String)loadDataSearchConditonSQLString();
        if (!StringUtils.hasText(ruleSql)) {
            request.setAttribute(MENU_DATA_AUTHOR_RULE_SQL,sql);
        }
    }

    public static synchronized void installUserInfo(HttpServletRequest request, SysUserCacheVo userCacheVo) {
        request.setAttribute(SYS_USER_INFO, userCacheVo);
    }

    public static synchronized SysUserCacheVo loadUserInfo() {
        return (SysUserCacheVo) SystemConfig.getHttpServletRequest().getAttribute(SYS_USER_INFO);

    }

}
