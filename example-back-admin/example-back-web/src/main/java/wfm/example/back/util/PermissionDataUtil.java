package wfm.example.back.util;

import wfm.example.back.sys.model.SysPermission;
import wfm.example.back.common.util.ObjectConvertUtils;

import java.util.List;

/**
 * 权限帮助类
 * @author: 吴福明
 */
public class PermissionDataUtil {

    /**
     * 智能处理错误数据，简化用户失误操作
     *
     * @param permission
     */
    public static SysPermission intelligentProcessData(SysPermission permission) {
        if (permission == null) {
            return null;
        }

        // 组件
        if (ObjectConvertUtils.isNotEmpty(permission.getComponent())) {
            String component = permission.getComponent();
            if (component.startsWith("/")) {
                component = component.substring(1);
            }
            if (component.startsWith("views/")) {
                component = component.replaceFirst("views/", "");
            }
            if (component.startsWith("src/views/")) {
                component = component.replaceFirst("src/views/", "");
            }
            if (component.endsWith(".vue")) {
                component = component.replace(".vue", "");
            }
            permission.setComponent(component);
        }

        // 请求URL
        if (ObjectConvertUtils.isNotEmpty(permission.getUrl())) {
            String url = permission.getUrl();
            if (url.endsWith(".vue")) {
                url = url.replace(".vue", "");
            }
            if (!url.startsWith("http") && !url.startsWith("/")&&!url.trim().startsWith("{{")) {
                url = "/" + url;
            }
            permission.setUrl(url);
        }

        // 一级菜单默认组件
        if (0 == permission.getMenuType() && ObjectConvertUtils.isEmpty(permission.getComponent())) {
            // 一级菜单默认组件
            permission.setComponent("layouts/RouteView");
        }
        return permission;
    }

    /**
     * 如果没有index页面 需要new 一个放到list中
     * @param metaList
     */
    public static void addIndexPage(List<SysPermission> metaList) {
        boolean hasIndexMenu = false;
        for (SysPermission sysPermission : metaList) {
            if("首页".equals(sysPermission.getName())) {
                hasIndexMenu = true;
                break;
            }
        }
        if(!hasIndexMenu) {
            metaList.add(0,new SysPermission(true));
        }
    }

    /**
     * 判断是否授权首页
     * @param metaList
     * @return
     */
    public static boolean hasIndexPage(List<SysPermission> metaList){
        boolean hasIndexMenu = false;
        for (SysPermission sysPermission : metaList) {
            if("首页".equals(sysPermission.getName())) {
                hasIndexMenu = true;
                break;
            }
        }
        return hasIndexMenu;
    }

}
