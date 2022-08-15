package com.lagou.message.learning;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * netty socketIO 是一个基于Netty的 java版本的 消息及时推送的框架
 * 通过Netty-SocketIO，我们可以轻松的实现服务端主动向客户端推送消息的场景。
 * learning包是对应的学习框架
 */

public class NettySocketService {
    private static List<SocketIOClient> clients = new ArrayList<SocketIOClient>();//用于保存所有客户端

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setHostname("localHost");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);

        // 添加创建连接的监听器
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                clients.add(socketIOClient);
            }
        });

        // 添加断开连接的监听器
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                clients.remove(socketIOClient);
            }
        });

        // 启动服务
        server.start();
        System.out.println("开始推送了。。。。。。");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                Packet packet = new Packet(PacketType.EVENT);
                packet.setData(random.nextInt(100));
                for (SocketIOClient client : clients) {
                    client.sendEvent("hello", new Point(random.nextInt(100), random.nextInt(100)));
                    client.sendEvent("hello", packet);
                }
                //System.out.println(clients.size());
            }
        }, 1000, 1000); // 每隔一秒推送一次
    }
}
