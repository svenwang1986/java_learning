package com.sven.rpc.common;

import com.sven.rpc.protocol.RpcRequest;
import com.sven.rpc.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class RpcInvoker<T> implements InvocationHandler {

    private Class<?> clazz;
    private String host;
    private int port;

    public RpcInvoker(String host, int port, Class<? extends T> clazz) {
        this.clazz = clazz;
        this.host = host;
        this.port = port;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("代理方法开始执行");

        RpcRequest request = new RpcRequest();

        request.setRequestId(UUID.randomUUID().toString());

        request.setClassName(clazz.getName());

        request.setMethodName(method.getName());

        request.setParameterTypes(method.getParameterTypes());

        request.setParameters(args);

        System.out.println("代理方法组装请求对象完毕：" + request);

        RpcResponse response = Transporter.send(host, port, request);

        if (null != response.getThrowable()) {
            System.err.println("代理方法发现返回异常信息");
            response.getThrowable().printStackTrace();
            return null;
        }

        return response.getResult();
    }
}
