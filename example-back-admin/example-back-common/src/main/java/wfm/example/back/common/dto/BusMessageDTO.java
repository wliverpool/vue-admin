package wfm.example.back.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 带业务参数的消息
 */
@Data
public class BusMessageDTO implements Serializable {

    /**
     * 发送人(用户登录账户)
     */
    protected String fromUser;

    /**
     * 发送给(用户登录账户)
     */
    protected String toUser;

    /**
     * 消息主题
     */
    protected String title;

    /**
     * 消息内容
     */
    protected String content;

    /**
     * 消息类型 1:消息  2:系统消息
     */
    protected String category;

    /**
     * 业务类型
     */
    private String busType;

    /**
     * 业务id
     */
    private String busId;

    public BusMessageDTO(){

    }

    /**
     * 构造 带业务参数的消息
     * @param fromUser
     * @param toUser
     * @param title
     * @param msgContent
     * @param msgCategory
     * @param busType
     * @param busId
     */
    public BusMessageDTO(String fromUser, String toUser, String title, String msgContent, String msgCategory, String busType, String busId){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.title = title;
        this.content = msgContent;
        this.category = msgCategory;
        this.busId = busId;
        this.busType = busType;
    }

}
