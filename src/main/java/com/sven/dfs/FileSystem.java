package com.sven.dfs;

import java.util.List;

public interface FileSystem {

    // 上传文件 到指定目录
    void put(String inputFile, String outputDir);

    // 下载文件 到指定文件夹
    void get(String inputFile, String outputDir);

    void mkdir(String mkdir);

    // 展示某个文件夹的所有子内容
    List<String> list(String inputPath);
}
