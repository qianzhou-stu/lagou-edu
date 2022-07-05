package com.lagou.message.server;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.corundumstudio.socketio.store.StoreFactory;
import com.lagou.common.jwt.JwtUtil;
import com.lagou.common.string.GsonUtil;
import com.lagou.message.api.dto.Message;
import com.lagou.message.consts.Constants;
import com.lagou.message.server.store.StoreFactoryProvider;
import com.lagou.message.util.PushUtils;
import com.lagou.message.util.ServerConfigUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import com.corundumstudio.socketio.namespace.Namespace;
import org.apache.commons.lang.StringUtils;

import java.net.SocketAddress;
import java.util.*;

@Slf4j
public class PushServer {

    public static final PushServer pushServer = new PushServer();

    private Namespace pushNamespace;

    private SocketIOServer server;

    private PushServer(){
        final Configuration configuration = new Configuration();
        configuration.setStoreFactory(StoreFactoryProvider.getRedissonStoreFactory()); // 设置存储为redisson
        configuration.setAuthorizationListener(new UserAuthorizationListener());
        configuration.setPort(ServerConfigUtils.instance.getWebSocketPort());
        configuration.setContext(ServerConfigUtils.instance.getWebSocketContext());
        configuration.setOrigin(ServerConfigUtils.instance.getWebSocketOrigin());
        // 可重用地址，防止处于TIME_WAIT的socket影响服务启动
        final SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        configuration.setSocketConfig(socketConfig);

        server = new SocketIOServer(configuration);
        pushNamespace = (Namespace) server.addNamespace(ServerConfigUtils.instance.getWebSocketContext());

        // 异常监听器
        configuration.setExceptionListener(new ExceptionListener() {
            @Override
            public void onEventException(Exception e, List<Object> list, SocketIOClient socketIOClient) {
                UUID sessionId = socketIOClient.getSessionId();
                log.error("onEventException, sessionId:{}, roomList:{}", sessionId, socketIOClient.get(Constants.ROOM), e);
            }

            @Override
            public void onDisconnectException(Exception e, SocketIOClient socketIOClient) {
                UUID sessionId = socketIOClient.getSessionId();
                log.error("onDisconnectException, sessionId:{}, roomList:{}", sessionId, socketIOClient.get(Constants.ROOM), e);
            }

            @Override
            public void onConnectException(Exception e, SocketIOClient socketIOClient) {
                UUID sessionId = socketIOClient.getSessionId();
                log.error("onConnectException, sessionId:{}, roomList:{}", sessionId, socketIOClient.get(Constants.ROOM), e);
            }

            @Override
            public void onPingException(Exception e, SocketIOClient socketIOClient) {
                try{
                    boolean channelOpen = socketIOClient.isChannelOpen();
                    SocketAddress remoteAddress = socketIOClient.getRemoteAddress();
                    UUID sessionId = socketIOClient.getSessionId();
                    log.error("onPingException, channelOpen:{}, sessionId:{}, remoteAddress:{}, roomList:{}",channelOpen, sessionId, remoteAddress,socketIOClient.get(Constants.ROOM), e);
                }catch (Exception e1){
                    log.error("onPingException", e1);
                }
            }

            @Override
            public boolean exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                try{
                    Channel channel = channelHandlerContext.channel();
                    if (null != channel){
                        log.error("exceptionCaught, channel:{}, isOpen:{}, remoteAddress:{}",channel, channel.isOpen(), channel.remoteAddress(), throwable);
                    }
                }catch (Exception e){
                    log.error("exceptionCaught", e);
                }
                return false;
            }
        });

        // 模拟发送消息
        pushNamespace.addEventListener("send-message", Map.class, new DataListener<Map>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Map map, AckRequest ackRequest) throws Exception {
                JwtUtil.JwtResult user = UserAuthorizationListener.getUserInfo(socketIOClient.getHandshakeData());
                Integer toUserId = user.getUserId();
                String content = (String) map.get("content");
                String type = (String) map.get("type");
                Message message = new Message(type, content, toUserId);
                log.info("from {} userId {} type {} content {}", user.getUserId(), toUserId, type, content);
                push(message);
            }
        });

        pushNamespace.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                UUID sid = socketIOClient.getSessionId();
                JwtUtil.JwtResult user = UserAuthorizationListener.getUserInfo(socketIOClient.getHandshakeData());
                if (user != null){
                    log.info("disconnect userId:{} username:{} sid:{}", user.getUserId(), user.getUserName(), sid);
                }
                else {
                    socketIOClient.disconnect();
                    log.info("client get handShakeData is null, disconnect. sid:{}", sid);
                    return;
                }
                String userId = String.valueOf(user.getUserId());
                List<String> roomList = PushUtils.getRoomList(userId, null, null);
                for (String roomStr : roomList) {
                    socketIOClient.leaveRoom(roomStr);
                }
                try{
                    // 离开房间
                    List<String> oldRoomList = socketIOClient.get(Constants.ROOM);
                    if (null != oldRoomList && oldRoomList.size() > 0){
                        for (String room : oldRoomList) {
                            if (StringUtils.isBlank(room)){
                                continue;
                            }
                            socketIOClient.leaveRoom(room);
                        }
                    }
                    socketIOClient.disconnect();
                }catch (Exception e){
                    log.error("leave old room exception, sid:{}", sid, e);
                }finally {
                    try {
                        socketIOClient.del(Constants.ROOM);
                        log.error("sid:{}, del hash success, field:{}", sid, Constants.ROOM);
                    }catch (Exception e){
                        log.error("sid:{}, del hash exception, field:{}", sid, Constants.ROOM, e);
                    }
                }
            }
        });

        pushNamespace.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                try {
                    UUID sid = socketIOClient.getSessionId();
                    JwtUtil.JwtResult user = UserAuthorizationListener.getUserInfo(socketIOClient.getHandshakeData());
                    if (user != null){
                        Set<String> rooms = socketIOClient.getAllRooms();
                        for (String room : rooms) {
                            socketIOClient.leaveRoom(room);
                        }
                        String userId = String.valueOf(user.getUserId());
                        List<String> roomList = PushUtils.getRoomList(userId, null, null);
                        for (String room : roomList) {
                            socketIOClient.joinRoom(room);
                            log.info("userId:{} sid:{} join room {}", userId, sid, room);
                        }
                        socketIOClient.set(Constants.ROOM, roomList);
                    }else {
                        socketIOClient.disconnect();
                        log.warn("sid:{} has no userId", sid);
                    }
                } catch (Exception e) {
                    log.error("addConnectListener - err", e);
                } finally {
                }
            }
        });
    }

    /**
     * 推送消息
     * @param message
     */
    public void push(Message message){
        // 推送消息的方法
        final String type;
        final Integer userId;
        final String json;
        long l11;
        try{
            long l0 = System.currentTimeMillis();
            type = message.getType();
            userId = message.getUserId();
            json = GsonUtil.toJson(message);
            l11 = System.currentTimeMillis();
            if (l11 - l0 > 50l){
                log.info("当前node.push耗时1-1,time={}ms", l11 - l0);
            }
        }finally {

        }
        String room;
        long l12;
        try{
            if (userId == null){
                throw new NullPointerException("userId 不能为空");
            }
            room = PushUtils.getRoom(null, userId, null);
            log.info("send message to {}, type:{}", room, type);
            l12 = System.currentTimeMillis();
            if (l12 - l11 > 50l){
                log.info("当前node.push耗时1-2,time={}ms", l12 - l11);
            }
        }finally {

        }
        Packet packet;
        long l13;
        try{
            // 组装消息
            packet = new Packet(PacketType.MESSAGE);
            packet.setSubType(PacketType.EVENT);
            packet.setName("message");
            ArrayList<Object> data = new ArrayList<>();
            data.add(json);
            packet.setData(data);
            packet.setNsp(pushNamespace.getName());

            l13 = System.currentTimeMillis();
            if (l13 - l12 > 50l){
                log.info("当前node.push耗时1-3, room={}, time={}ms", room, l12 - l11);
            }
        }finally {

        }

        int i1;
        final long l2;

        try{
            // 当前服务推送
            i1 = 0;
            try{
                Iterable<SocketIOClient> clients = pushNamespace.getRoomClients(room);
                for (SocketIOClient socketIOClient : clients) {
                    socketIOClient.send(packet);
                    i1++;
                }
            }catch (Exception e){
                log.error("当前服务直接推送失败", e);
            }

            l2 = System.currentTimeMillis();
            if (l2 - l13 > 50l){
                log.info("当前node.push耗时1-4, room={}, clientCount = {},time={}ms", room, i1, l2 - l13);
            }
        }finally {

        }
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
