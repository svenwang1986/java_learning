package com.sven.rpc.protocol;

public class RpcResponse {

    private String requestId;

    private Throwable throwable;

    private Object result;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RCPResponse{" +
                "requestId='" + requestId + '\'' +
                ", throwable=" + throwable +
                ", result=" + result +
                '}';
    }
}
