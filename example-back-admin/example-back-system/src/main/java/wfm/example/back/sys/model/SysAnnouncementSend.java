package wfm.example.back.sys.model;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import wfm.example.back.common.model.BaseStringIDModel;

/**
 * 用户通告阅读标记表
 * @author: 吴福明
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_announcement_send")
public class SysAnnouncementSend extends BaseStringIDModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**通告id*/
    private String anntId;

    /**用户id*/
    private String userId;

    /**阅读状态（0未读，1已读）*/
    private String readFlag;

    /**阅读时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date readTime;

}
