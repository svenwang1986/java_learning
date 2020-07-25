package com.sven.rpc.server;

import com.sven.rpc.codec.RpcDecoder;
import com.sven.rpc.codec.RpcEncoder;
import com.sven.rpc.handler.NettyRpcServerHandler;
import com.sven.rpc.protocol.RpcRequest;
import com.sven.rpc.protocol.RpcResponse;
import com.sven.rpc.serde.JsonSerialization;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyRpcServer {

    private int port;


    public NettyRpcServer(int port) {
        this.port = port;
    }

    public void startServer() {
        ServerBootstrap server = initServerBootstrap();

        try {
            System.out.println("开始启动服务端");
            server.bind(this.port).sync().channel().closeFuture().channel();
        } catch (InterruptedException e) {
            System.err.println(" 服务启动异常");
            e.printStackTrace();
        }
    }

    private ServerBootstrap initServerBootstrap() {

        ServerBootstrap server = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);

        ChannelInitializer channelInitializer = initChannelInitializer();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(channelInitializer);

        return server;
    }

    private ChannelInitializer initChannelInitializer() {
        ChannelInitializer serverInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4))
                        .addLast(new RpcDecoder(RpcRequest.class, new JsonSerialization()))
                        .addLast(new RpcEncoder(RpcResponse.class, new JsonSerialization()))
                        .addLast(new NettyRpcServerHandler());
            }
        };

        return serverInitializer;
    }


}
