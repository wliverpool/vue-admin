package wfm.example.back.wx.constant.dingtalk;


import com.alibaba.fastjson.TypeReference;
import wfm.example.back.wx.model.dingtalk.Department;
import wfm.example.back.wx.model.dingtalk.PageResult;
import wfm.example.back.wx.model.dingtalk.User;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 用于JSON泛型转换，定义各种实际类型
 *
 * @author 吴福明
 */
public class JdtTypes {

    public final static Type PageResult_User = new TypeReference<PageResult<User>>() {
    }.getType();

    public final static Type PageResult_Department = new TypeReference<PageResult<Department>>() {
    }.getType();

    public final static Type List_String = new TypeReference<List<String>>() {
    }.getType();

    public final static Type List_Department = new TypeReference<List<Department>>() {
    }.getType();

}
