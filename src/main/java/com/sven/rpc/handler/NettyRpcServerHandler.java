package com.sven.rpc.handler;

import com.sven.rpc.common.PropertiesUtil;
import com.sven.rpc.protocol.RpcRequest;
import com.sven.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;

public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RpcRequest request = (RpcRequest) msg;

        System.out.println("收到request" + request);

        /**
         * 根据服务对象，解析得到服务实现类
         */
        String className = request.getClassName();
        String name = className.substring(className.lastIndexOf(".") + 1);
        String subClass = PropertiesUtil.getSubClass(name);
        request.setClassName(subClass);

        System.out.println("服务端接收到的请求对象：" + request.toString());

        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());

        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Throwable t) {
            response.setThrowable(t);
            t.printStackTrace();
        }
        System.out.println("将返回内容写回" + response);
        // 操作完以后写入 netty 的上下文中。netty 自己处理返回值。
        ctx.writeAndFlush(response);
    }

    private Object handle(RpcRequest request) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, InvocationTargetException {

        // 通过反射创建 服务类 实例
        // 这里已经被替换成了实现类的class
        Class<?> clz = Class.forName(request.getClassName());
        Object serviceBean = clz.newInstance();

        // 通过反射创建 服务类的 方法实例
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();

        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        // 根本思路还是获取类名和方法名，利用反射实现调用
        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);

        // 实际调用发生的地方
        return fastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.err.println(" exception caught in NettyRpcServerHandler ");
        ctx.close();
    }
}
