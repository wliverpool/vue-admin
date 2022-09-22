package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.Demo;

/**
 * demo服务接口
 * @author 吴福明
 */
public interface IDemoService extends IService<Demo> {

    public void testTran();

    public Demo getByIdCacheable(String id);

    /**
     * 查询列表数据 在service中获取数据权限sql信息
     * @param pageSize
     * @param pageNo
     * @return
     */
    IPage<Demo> queryListWithPermission(int pageSize, int pageNo);
}
