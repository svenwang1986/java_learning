package com.sven.dfs;

import com.sven.rpc.common.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 文件块管理
 */
public class BlockManager {


    public List<ServerNode> chooseServer(List<ServerNode> servers) {

        int repliationNum = Integer.parseInt(PropertiesDFSUtil.getProperty("replica_num", "0"));

        if (repliationNum <= 0)
            throw new RuntimeException("[副本个数 replica_num ]参数配置非法");

        if (repliationNum >= servers.size()) {
            return servers;
        }

        List<ServerNode> choosedServers = new ArrayList<>(repliationNum);

        //这里使用随机方法模拟
        for (int i = 0; i < repliationNum; i++) {
            choosedServers.add(servers.remove(new Random().nextInt(servers.size())));
        }

        return choosedServers;
    }
}
