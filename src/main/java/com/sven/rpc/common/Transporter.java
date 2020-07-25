package com.sven.rpc.common;

import com.sven.rpc.client.NettyRpcClient;
import com.sven.rpc.protocol.RpcRequest;
import com.sven.rpc.protocol.RpcResponse;

public class Transporter {


    private static int TIME_OUT = 10;

    public static RpcResponse send(String host, int port, RpcRequest request) {
        NettyRpcClient client = new NettyRpcClient(host, port);
        client.connect();
        RpcResponse response = client.send(request, TIME_OUT);

        return response;

    }
}
