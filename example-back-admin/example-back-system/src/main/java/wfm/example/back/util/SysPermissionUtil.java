package wfm.example.back.util;

import wfm.example.back.common.vo.TreeVo;
import wfm.example.back.sys.model.SysPermission;

import java.util.ArrayList;

public class SysPermissionUtil {

    public static TreeVo convertTreeVo(SysPermission permission) {
        TreeVo treeVo = new TreeVo();
        treeVo.setKey(permission.getId());
        treeVo.setIcon(permission.getIcon());
        treeVo.setParentId(permission.getParentId());
        treeVo.setTitle(permission.getName());
        treeVo.setSlotTitle(permission.getName());
        treeVo.setValue(permission.getId());
        treeVo.setIsLeaf(permission.isLeaf());
        treeVo.setLabel(permission.getName());
        if(!permission.isLeaf()) {
            treeVo.setChildren(new ArrayList<TreeVo>());
        }
        return treeVo;
    }

}
