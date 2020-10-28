package wfm.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.model.SysDepart;
import wfm.example.common.dto.DepartIdDTO;
import wfm.example.common.dto.SysDepartTreeDTO;

import java.util.List;

/**
 * 部门表服务
 * @author:吴福明
 */
public interface ISysDepartService extends IService<SysDepart>{

    /**
     * 查询我的部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeDTO> queryMyDeptTreeList(String departIds);

    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeDTO> queryTreeList();

    /**
     * 查询所有部门DepartId信息,并分节点进行显示
     * @return
     */
    public List<DepartIdDTO> queryDepartIdTreeList();

    /**
     * 保存部门数据
     * @param sysDepart
     */
    void saveDepartData(SysDepart sysDepart,String username);

    /**
     * 更新depart数据
     * @param sysDepart
     * @return
     */
    Boolean updateDepartDataById(SysDepart sysDepart,String username);

    /**
     * 删除depart数据
     * @param id
     * @return
     */
    /* boolean removeDepartDataById(String id); */

    /**
     * 根据关键字搜索相关的部门数据
     * @param keyWord
     * @return
     */
    List<SysDepartTreeDTO> searchBy(String keyWord,String myDeptSearch,String departIds);

    /**
     * 根据部门id删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 查询SysDepart集合
     * @param userId
     * @return
     */
    public List<SysDepart> queryUserDeparts(String userId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    List<SysDepart> queryDepartsByUsername(String username);

    /**
     * 根据部门id批量删除并删除其可能存在的子级部门
     * @param ids
     * @return
     */
    void deleteBatchWithChildren(List<String> ids);

    /**
     *  根据部门Id查询,当前和下级所有部门IDS
     * @param departId
     * @return
     */
    List<String> getSubDepIdsByDepId(String departId);

    /**
     * 获取我的部门下级所有部门IDS
     * @return
     */
    List<String> getMySubDepIdsByDepId(String departIds);
    /**
     * 根据关键字获取部门信息（通讯录）
     * @return
     */
    List<SysDepartTreeDTO> queryTreeByKeyWord(String keyWord);
}
