package com.sven.rpc.handler;

import com.sven.rpc.common.RpcFuture;
import com.sven.rpc.protocol.RpcRequest;
import com.sven.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyRpcClientHandler extends ChannelDuplexHandler {

    private Map<String, RpcFuture> futureMap = new ConcurrentHashMap<>();


    /**
     * 发请求前，将requestId和一个future绑定起来
     * @param ctx
     * @param msg
     * @param promise
     * @throws Exception
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequest){
            RpcRequest request = (RpcRequest) msg;
            futureMap.putIfAbsent(request.getRequestId(),new RpcFuture());
        }
        super.write(ctx, msg, promise);
    }

    /**
     * 数据返回时，将requestId与返回的响应内容对象进行关联
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof RpcResponse){
            RpcResponse response = (RpcResponse) msg;
            RpcFuture future = futureMap.get(response.getRequestId());
            future.setResponse(response);

        }

        super.channelRead(ctx, msg);
    }

    public RpcResponse getResponse(String requestId , int timeout){

        try {

            RpcFuture future = futureMap.get(requestId);

            RpcResponse response = future.getResponse(timeout);

            return response;

        } finally {
            futureMap.remove(requestId);
        }
    }
}
