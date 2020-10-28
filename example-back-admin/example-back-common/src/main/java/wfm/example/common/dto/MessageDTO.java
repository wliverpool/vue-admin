package wfm.example.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 发送消息DTO
 * @author 吴福明
 */

@Data
public class MessageDTO implements Serializable {

    /**
     * 消息类型
     **/
    private String msgType;

    /**
     * 消息接收方
     **/
    private String receiver;

    /**
     * 消息模板码
     **/
    private String templateCode;

    /**
     * 测试数据
     **/
    private String testData;

}
