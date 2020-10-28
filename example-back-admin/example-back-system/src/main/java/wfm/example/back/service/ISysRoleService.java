package wfm.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import wfm.example.back.model.SysRole;
import wfm.example.common.vo.Result;

/**
 * 角色表 服务类
 * @author 吴福明
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 导入 excel ，检查 roleCode 的唯一性
     *
     * @param file
     * @param params
     * @return
     * @throws Exception
     */
    /*Result importExcelCheckRoleCode(MultipartFile file, ImportParams params) throws Exception;*/

    /**
     * 删除角色
     * @param roleid
     * @return
     */
    public boolean deleteRole(String roleid);

    /**
     * 批量删除角色
     * @param roleids
     * @return
     */
    public boolean deleteBatchRole(String[] roleids);

}
