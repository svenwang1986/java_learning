package com.sven.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {


    private String host;
    private int port;
    private NettyClientHandler clientHandler;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;

        start();
    }

    private static ExecutorService threadPool = Executors.newFixedThreadPool(1);

    private void start() {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap client = new Bootstrap();

        clientHandler = new NettyClientHandler();

        client.group(workerGroup)
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(clientHandler);
                    }
                });

        try {

            client.connect(this.host, this.port).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public Object getProxy(Class<?> serviceClass) {

        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        System.out.println("客户端要发送的参数是 ："+ args[0].toString());

                        clientHandler.setRequestparam(args[0].toString());

                        return threadPool.submit(clientHandler).get();
                    }
                });

    }


}
