package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.SysCategory;
import wfm.example.back.common.dto.TreeSelectDTO;
import wfm.example.back.common.exception.BizException;

import java.util.List;
import java.util.Map;

/**
 * 分类字典
 * @author: 吴福明
 */
public interface ISysCategoryService extends IService<SysCategory> {

    /**根节点父ID的值*/
    public static final String ROOT_PID_VALUE = "0";

    void addSysCategory(SysCategory sysCategory);

    void updateSysCategory(SysCategory sysCategory);

    /**
     * 根据父级编码加载分类字典的数据
     * @param pcode
     * @return
     */
    public List<TreeSelectDTO> queryListByCode(String pcode) throws BizException;

    /**
     * 根据pid查询子节点集合
     * @param pid
     * @return
     */
    public List<TreeSelectDTO> queryListByPid(String pid);

    /**
     * 根据pid查询子节点集合,支持查询条件
     * @param pid
     * @param condition
     * @return
     */
    public List<TreeSelectDTO> queryListByPid(String pid, Map<String,String> condition);

    /**
     * 根据code查询id
     * @param code
     * @return
     */
    public String queryIdByCode(String code);

}
