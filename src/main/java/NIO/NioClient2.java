package NIO;

import java.io.IOException;

public class NioClient2 {

    public static void main(String[] args) throws IOException {

        NIOClient client = new NIOClient("0002");

        client.work();
    }
}
