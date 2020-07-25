package com.sven.netty.server;

public class NettyServerBootstrap {

    public static void main(String[] args) {
        NettyServer server = new NettyServer("localhost",10010);
        server.startServer();
    }
}
