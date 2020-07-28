package com.sven.dfs;

public class Bootstrap {

    public static void main(String[] args) {


        DistributedFileSystem dfs = new DistributedFileSystem();

        dfs.put("/Users/sven/app/out/apache-zookeeper-3.6.1.tar.gz","zookeeper");
    }



}
