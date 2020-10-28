package wfm.example.back.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wfm.example.back.model.SysAnnouncement;

/**
 * 系统通告表
 * @author: 吴福明
 */
public interface SysAnnouncementMapper extends BaseMapper<SysAnnouncement> {


    List<SysAnnouncement> querySysCementListByUserId(Page<SysAnnouncement> page, @Param("userId")String userId,@Param("msgCategory")String msgCategory);

}
