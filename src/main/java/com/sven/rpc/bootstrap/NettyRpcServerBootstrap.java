package com.sven.rpc.bootstrap;


import com.sven.rpc.server.NettyRpcServer;

/**
 * 一个简单的服务端测试案例
 **/
public class NettyRpcServerBootstrap {

    public static void main(String[] args) {

        NettyRpcServer server = new NettyRpcServer(NettyServerProperties.port);

        server.startServer();
    }
}
