package com.sven.rpc.service.impl;

import com.sven.rpc.service.HelloService;

public class HelloServiceImpl implements HelloService {

    public HelloServiceImpl() {
        super();
    }

    @Override
    public String hello(String name) {

        System.out.println(" service impl executed");

        return  " 2020 , hello ," + name;
    }
}
