package com.sven.dfs;

import java.io.*;
import java.util.List;

public class DFSFileTrans {


    /**
     * 拷贝文件到DFS系统中
     */
    public static void copyFileToDFS(String sourceFile, String distFileDir) {


        //1、选择将文件存储在哪些节点，
        BlockManager blockManager = new BlockManager();

        List<ServerNode> servers = ServerLoader.loadServer();

        List<ServerNode> choosedServers = blockManager.chooseServer(servers);

        //2、将文件按块大小 分别写到对应的节点上

        //计算一共需要分几块，默认4M一个块

        int blockSize = Integer.parseInt(PropertiesDFSUtil.getProperty("block_size", "4194304"));
        long length = new File(sourceFile).length();
        double blockNumTemp = length *1D / blockSize;
        int blockNum = (int) Math.ceil(blockNumTemp);

        if (blockNum == 0)
            blockNum = 1;

        // 公共输入流
        InputStream fin = null;
        try {
            fin = new FileInputStream(sourceFile);  //输入流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //按块写，写所有节点
        for (int i = 0; i < blockNum; i++) {

            String blockID = "block_" + System.currentTimeMillis();

            ServerNode node = choosedServers.get(0);

            String dfsPath = node.getServerPath()
                    + File.separator
                    + distFileDir;
            //判断路径是否存在
            File dir = new File(dfsPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String finalPath = dfsPath + File.separator + blockID;

            System.out.println("最终文件路径为：" + finalPath);
            //将文件上传到对应的节点位置下
            transforFile(fin, finalPath, blockSize);

            //同步副本
            //应该是交给不同的线程去做，将节点和BlockID
            copyReplications(choosedServers, distFileDir, dfsPath, blockID);

        }

        try {
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void copyReplications(List<ServerNode> choosedServers, String distFileDir, String dfsPath, String blockID) {

        for (int i = 1; i < choosedServers.size(); i++) {
            String outPath = choosedServers.get(i).getServerPath()
                    + File.separator
                    + distFileDir;

            File dir = new File(outPath);

            if (!dir.exists()){
                dir.mkdirs();
            }

            String outFile = outPath + File.separator + blockID;



            try {
                FileInputStream fin = new FileInputStream(dfsPath + File.separator + blockID);

                FileOutputStream fout = new FileOutputStream(outFile);


                int bufferSize = 4096;

                byte[] buffer = new byte[bufferSize];

                int read = 0;

                while ((read = fin.read(buffer)) > 0) {
                    fout.write(buffer, 0, read);
                }

                fin.close();
                fout.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static void transforFile(InputStream fin, String finalPath, int blockSize) {

        try {
            FileOutputStream outputStream = new FileOutputStream(finalPath);
            int buffer = 4096;
            int total = blockSize / buffer;
            byte[] byteArray = new byte[buffer];
            int read = 0;
            int count = 0;
            while ((read = fin.read(byteArray)) > 0) {
                //有内容写到outputstream
                outputStream.write(byteArray, 0, read);
                count++;
                //控制每个块只读取固定大小的内容
                if (count == total) break;
            }

            outputStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("指定的输入/输出文件不存在");
        } catch (IOException e) {
            System.out.println("读写文件异常！");
        }
    }
}
