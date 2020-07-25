package com.sven.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable{

    private String requestparam;
    private ChannelHandlerContext context;
    private String result;

    public void setRequestparam(String requestparam) {
        this.requestparam = requestparam;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(" channel actived ");

        this.context = ctx;

    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;

        this.result = "received message from server : " + message;

        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println(" >>>> client exception <<<< ");
    }


    @Override
    public synchronized Object call() throws Exception {
        //调用这个方法的时候说明要发送数据了
        //可以先将参数绑定完毕，然后写出并冲刷，然后等收到了服务端的响应后，将结果返回

        //System.out.println(" method has been called .");

        this.context.writeAndFlush(this.requestparam);
        
        wait();


        return this.result;
    }
}
