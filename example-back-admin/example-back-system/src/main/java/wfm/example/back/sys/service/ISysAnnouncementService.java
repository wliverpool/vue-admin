package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.SysAnnouncement;

/**
 * 系统通告表服务接口
 * @Author: 吴福明
 */
public interface ISysAnnouncementService extends IService<SysAnnouncement> {

    public void saveAnnouncement(SysAnnouncement sysAnnouncement);

    public boolean updateAnnouncement(SysAnnouncement sysAnnouncement);

    public void saveSysAnnouncement(String title, String msgContent);

    public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId, String msgCategory);

}
