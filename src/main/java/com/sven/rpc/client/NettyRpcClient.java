package com.sven.rpc.client;

import com.sven.rpc.codec.RpcDecoder;
import com.sven.rpc.codec.RpcEncoder;
import com.sven.rpc.handler.NettyRpcClientHandler;
import com.sven.rpc.protocol.RpcRequest;
import com.sven.rpc.protocol.RpcResponse;
import com.sven.rpc.serde.JsonSerialization;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyRpcClient implements  RpcClient {

    private String host;
    private int port;

    private NettyRpcClientHandler clientHandler;
    private NioEventLoopGroup eventLoopGroup;
    private Channel channel;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse send(RpcRequest request, int timeout) {
        try {
            channel.writeAndFlush(request).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return clientHandler.getResponse(request.getRequestId(),timeout);
    }

    @Override
    public void connect() {

        eventLoopGroup = new NioEventLoopGroup();

        clientHandler = new NettyRpcClientHandler();

        Bootstrap client = new Bootstrap();

        client.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(initChannelInitializer());

        //start
        try {
            channel = client.connect(host,port).sync().channel();
        } catch (InterruptedException e) {
            System.err.println("客户端启动出错！");
            e.printStackTrace();
        }

    }

    private ChannelInitializer initChannelInitializer() {

        return new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4))
                        .addLast(new RpcEncoder(RpcRequest.class, new JsonSerialization()))
                        .addLast(new RpcDecoder(RpcResponse.class, new JsonSerialization()))
                        .addLast(clientHandler);
            }
        };
    }

    @Override
    public void close() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }
}

