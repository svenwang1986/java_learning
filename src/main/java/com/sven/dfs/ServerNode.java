package com.sven.dfs;

public class ServerNode {
    //对应真实的分布式应用，此处应该是IP+端口+文件路径
    private String serverPath;

    public ServerNode(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }


    @Override
    public String toString() {
        return "ServerNode{" +
                "serverPath='" + serverPath + '\'' +
                '}';
    }
}
