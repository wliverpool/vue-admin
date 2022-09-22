package wfm.example.back.sys.rule.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import wfm.example.back.common.config.SystemConfig;
import wfm.example.back.sys.mapper.SysCategoryMapper;
import wfm.example.back.sys.model.SysCategory;
import wfm.example.back.sys.rule.IFillRuleHandler;
import wfm.example.back.common.util.CodeGenerateUtils;
import wfm.example.back.common.util.ObjectConvertUtils;

import java.util.List;

/**
 * 分类字典编码生成规则
 * @author 吴福明
 */
public class CategoryCodeRule implements IFillRuleHandler {

    public static final String ROOT_PID_VALUE = "0";

    @Override
    public Object execute(JSONObject params, JSONObject formData) {

        String categoryPid = ROOT_PID_VALUE;
        String categoryCode = null;

        if (formData != null && formData.size() > 0) {
            Object obj = formData.get("pid");
            if (ObjectConvertUtils.isNotEmpty(obj)) {
                categoryPid = obj.toString();
            }
        } else {
            if (params != null) {
                Object obj = params.get("pid");
                if (ObjectConvertUtils.isNotEmpty(obj)) {
                    categoryPid = obj.toString();
                }
            }
        }

        /*
         * 分成三种情况
         * 1.数据库无数据 调用CodeGenerateUtils.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 CodeGenerateUtils.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 CodeGenerateUtils.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getPid, categoryPid).isNotNull(SysCategory::getCode).orderByDesc(SysCategory::getCode);
        SysCategoryMapper baseMapper = (SysCategoryMapper) SystemConfig.getBean("sysCategoryMapper");
        List<SysCategory> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (ROOT_PID_VALUE.equals(categoryPid)) {
                //情况1
                categoryCode = CodeGenerateUtils.getNextYouBianCode(null);
            } else {
                //情况2
                SysCategory parent = (SysCategory) baseMapper.selectById(categoryPid);
                categoryCode = CodeGenerateUtils.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = CodeGenerateUtils.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
