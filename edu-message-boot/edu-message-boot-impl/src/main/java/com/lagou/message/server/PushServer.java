package com.lagou.message.server;

import com.corundumstudio.socketio.SocketIOServer;
import com.lagou.message.api.dto.Message;
import lombok.extern.slf4j.Slf4j;
import com.corundumstudio.socketio.namespace.Namespace;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PushServer {

    public static final PushServer pushServer = new PushServer();

    private Namespace pushNamespace;

    private SocketIOServer server;

    private PushServer(){

    }

    /**
     * 推送消息
     * @param message
     */
    public void push(Message message){

    }

    /**
     * 同步启动服务；
     */
    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            log.error("Push server start failed!", e);
            System.exit(-1);
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        server.stop();
    }

    public Map<String, Object> getStatus() {
        HashMap<String, Object> status = new HashMap<>();
        status.put("namespace", pushNamespace.getName());   // namespace
        status.put("rooms", pushNamespace.getRooms());
        status.put("clients", pushNamespace.getAllClients().size());
        return status;
    }

}
