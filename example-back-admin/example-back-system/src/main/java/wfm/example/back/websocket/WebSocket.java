package wfm.example.back.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import wfm.example.common.constant.WebsocketConstant;

/**
 * WebSocket服务
 * @Author 吴福明
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {

    //1.增加app端标识
    private String APP_SESSION_SUFFIX = "_app";

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        try {
            this.session = session;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】有新的连接，总数为:"+webSockets.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            log.info("【websocket消息】连接断开，总数为:"+webSockets.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        //todo 现在有个定时任务刷，应该去掉
        log.debug("【websocket消息】收到客户端消息:"+message);
        JSONObject obj = new JSONObject();
        // 业务类型
        obj.put(WebsocketConstant.MSG_CMD, WebsocketConstant.CMD_CHECK);
        // 消息内容
        obj.put(WebsocketConstant.MSG_TXT, "心跳响应");
        session.getAsyncRemote().sendText(obj.toJSONString());
    }

    /**
     * 此为广播消息
     * @param message
     */
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:"+message);
        for(WebSocket webSocket : webSockets) {
            try {
                if(webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 此为单点消息
     * @param userId
     * @param message
     */
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null&&session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        //--------增加APP端消息推送--------
        Session session_app = sessionPool.get(userId+APP_SESSION_SUFFIX );
        if (session_app != null&&session_app .isOpen()) {
            try {
                log.info("【websocket移动端消息】 单点消息:"+message);
                session_app .getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 此为单点消息(多人)
     * @param userIds
     * @param message
     */
    public void sendMoreMessage(String[] userIds, String message) {
        for(String userId:userIds) {
            Session session = sessionPool.get(userId);
            if (session != null&&session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:"+message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            //--------增加APP端消息推送--------
            Session session_app = sessionPool.get(userId+APP_SESSION_SUFFIX );
            if (session_app != null&&session_app .isOpen()) {
                try {
                    log.info("【websocket移动端消息】 单点消息:"+message);
                    session_app .getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }

}