package com.sven.rpc.bootstrap;

import com.sven.rpc.common.ProxyFactory;
import com.sven.rpc.service.HelloService;
import com.sven.rpc.service.impl.HelloServiceImpl;

public class NettyRpcClientBootstrap {

    public static void main(String[] args) {


        HelloService helloService = ProxyFactory.create(
                NettyServerProperties.host,
                NettyServerProperties.port,
                HelloService.class);

        String result = helloService.hello("sven");

        System.out.println("客户端收到的响应：" + result);
    }
}
