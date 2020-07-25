package com.sven.netty.server;

import com.sven.netty.service.DateTimeServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);

        String name = (String) msg;

        System.out.println("received client message: " + name);

        String helloResult = new DateTimeServiceImpl().hello(name);

        ctx.writeAndFlush(helloResult);


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("异常发生了 >>>> ");
        cause.printStackTrace();
        ctx.close();
    }
}
