package wfm.example.back.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wfm.example.back.mapper.SysAnnouncementSendMapper;
import wfm.example.back.model.SysAnnouncementSend;
import wfm.example.back.service.ISysAnnouncementSendService;
import wfm.example.common.dto.SysAnnouncementSendDTO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户通告阅读标记服务实现类
 * @author: 吴福明
 */
@Service
public class SysAnnouncementSendServiceImpl extends ServiceImpl<SysAnnouncementSendMapper, SysAnnouncementSend> implements ISysAnnouncementSendService {

    @Resource
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;

    @Override
    public List<String> queryByUserId(String userId) {
        return sysAnnouncementSendMapper.queryByUserId(userId);
    }

    @Override
    public Page<SysAnnouncementSendDTO> getMyAnnouncementSendPage(Page<SysAnnouncementSendDTO> page, SysAnnouncementSendDTO announcementSendModel) {
        return page.setRecords(sysAnnouncementSendMapper.getMyAnnouncementSendList(page, announcementSendModel));
    }

}
