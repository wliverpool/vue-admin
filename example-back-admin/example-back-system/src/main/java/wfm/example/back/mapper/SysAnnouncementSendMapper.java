package wfm.example.back.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wfm.example.back.model.SysAnnouncementSend;
import wfm.example.common.dto.SysAnnouncementSendDTO;

/**
 * @Description: 用户通告阅读标记表
 * @Author: 吴福明
 */
public interface SysAnnouncementSendMapper extends BaseMapper<SysAnnouncementSend> {

    public List<String> queryByUserId(@Param("userId") String userId);

    /**
     * @功能：获取我的消息
     * @param page
     * @param announcementSendDTO
     * @return
     */
    List<SysAnnouncementSendDTO> getMyAnnouncementSendList(Page<SysAnnouncementSendDTO> page, @Param("announcementSendDTO") SysAnnouncementSendDTO announcementSendDTO);

}
