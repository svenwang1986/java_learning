package com.sven.dfs;

import com.sven.rpc.common.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;

public class ServerLoader {

    public static List<ServerNode> loadServer() {
        List<ServerNode> servers = null;
        // 解析服务器
        servers = new ArrayList<ServerNode>();
        String serverStr = PropertiesDFSUtil.getProperty("servers",null);
        String[] ss = serverStr.split(",");
        for (String server : ss) {
            servers.add(new ServerNode(server));
        }
        return servers;
    }
}
