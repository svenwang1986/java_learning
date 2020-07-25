package com.sven.dfs;

import java.io.*;
import java.util.Properties;

public class PropertiesDFSUtil {

    // 服务名称， 服务实现类
    private static Properties properties = null;
    private static final String DEFAULT_ENCODING = "UTF-8";

    static {
        properties = new Properties();
        InputStream inputStream = null;
        try {
            File file = new File("src/main/resources/dfs/server.properties");
            inputStream = new BufferedInputStream(new FileInputStream(file));
            properties.load(new InputStreamReader(inputStream, DEFAULT_ENCODING));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取实现类
     */
    public static String getProperty(String key, String defaultV) {
        String value = properties.getProperty(key);
        if (null == value) return defaultV;
        return value;
    }
}
