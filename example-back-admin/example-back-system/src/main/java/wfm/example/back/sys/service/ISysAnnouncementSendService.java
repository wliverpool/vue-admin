package wfm.example.back.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import wfm.example.back.sys.model.SysAnnouncementSend;
import wfm.example.back.common.dto.SysAnnouncementSendDTO;

import java.util.List;

/**
 * 用户通告阅读标记服务接口
 * @Author: 吴福明
 */
public interface ISysAnnouncementSendService extends IService<SysAnnouncementSend> {

    public List<String> queryByUserId(String userId);

    /**
     * @功能：获取我的消息
     * @param announcementSendModel
     * @return
     */
    public Page<SysAnnouncementSendDTO> getMyAnnouncementSendPage(Page<SysAnnouncementSendDTO> page, SysAnnouncementSendDTO announcementSendModel);

}
