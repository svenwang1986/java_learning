package com.sven.rpc.common;

import com.sven.rpc.protocol.RpcResponse;

public class RpcFuture {

    private RpcResponse response;

    private volatile boolean got;

    private final Object lock = new Object();

    public RpcResponse getResponse(int timeout) {

        synchronized (lock) {
            while (!got) {
                try {
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }


    public void setResponse(RpcResponse response){
        if (got) return ;

        synchronized (lock){
            this.response = response;
            this.got = true;
            lock.notify();
        }


    }
}
