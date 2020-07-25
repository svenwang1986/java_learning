package com.sven.netty.client;

import com.sven.netty.service.DateTimeService;

public class NettyClientBootstrap {

    public static void main(String[] args) {

        NettyClient client = new NettyClient("localhost",10010);

        Class serviceClass = DateTimeService.class;

        DateTimeService service = (DateTimeService) client.getProxy(serviceClass);


        System.out.println("send hello via the proxy ");

        String res = service.hello("aaa");

        System.out.println("get result via the proxy : "+res);

    }
}
