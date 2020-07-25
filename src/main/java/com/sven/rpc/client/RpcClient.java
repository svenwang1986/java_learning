package com.sven.rpc.client;

import com.sven.rpc.protocol.RpcRequest;
import com.sven.rpc.protocol.RpcResponse;

public interface RpcClient {

    //  发送请求
    RpcResponse send(RpcRequest request, int timeout);

    //  链接服务器
    void connect();

    //  关闭客户端
    void close();
}