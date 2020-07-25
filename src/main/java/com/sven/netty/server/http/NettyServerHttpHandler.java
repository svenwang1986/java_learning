package com.sven.netty.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class NettyServerHttpHandler extends SimpleChannelInboundHandler {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        //        System.out.println("接收到请求：  "+msg);
        if (msg instanceof HttpRequest) {
            HttpRequest msgs = (HttpRequest) msg;

            System.out.println("接收到请求：  " + msgs.method());

            //设置返回内容
            ByteBuf content = Unpooled.copiedBuffer("Hello World 2020 ! \n", CharsetUtil.UTF_8);

            //创建响应
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            ctx.writeAndFlush(response);

        }

    }
}
