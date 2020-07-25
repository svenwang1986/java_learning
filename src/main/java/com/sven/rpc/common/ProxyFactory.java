package com.sven.rpc.common;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static <T> T create(String host, int port, Class<T> interfaceClass) {


        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcInvoker<T>(host, port, interfaceClass));
    }
}
