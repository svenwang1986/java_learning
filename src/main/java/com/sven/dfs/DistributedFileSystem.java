package com.sven.dfs;

import java.util.List;

public class DistributedFileSystem implements FileSystem {
    @Override
    public void put(String inputFile, String outputDir) {
        //拷贝文件
        DFSFileTrans.copyFileToDFS(inputFile, outputDir);
        //记录日志、元数据


    }

    @Override
    public void get(String inputFile, String outputDir) {

    }

    @Override
    public void mkdir(String mkdir) {

    }

    @Override
    public List<String> list(String inputPath) {
        return null;
    }
}
