package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class NIOServer {

    public static int TIME_OUT = 1000;

    private final int SOCKET = 10010;

    private final int BUFFER_SIZE = 4096;

    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    private ByteBuffer sendBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    Selector selector;

    public NIOServer() throws IOException {


        ServerSocketChannel channel = ServerSocketChannel.open();

        channel.configureBlocking(false);

        channel.bind(new InetSocketAddress(SOCKET));

        selector = Selector.open();

        channel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server init successfully.");

    }

    public void listen() throws IOException {

        while (true){
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            if (selectionKeys.isEmpty()){
                continue;
            }

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                handle(selectionKey);
            }

            try {
                Thread.sleep(100);
                //System.out.println("looping ...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void handle(SelectionKey selectionKey) throws IOException {

        if (selectionKey.isAcceptable()){

            //接受连接时 channel()方法返回的是ServerSocketChannel
            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
            SocketChannel client = server.accept();

            client.configureBlocking(false);

            client.register(selector, SelectionKey.OP_READ);

        }else if(selectionKey.isReadable()){

            //可读或写的时候，channel()方法返回的是 SocketChannel()
            SocketChannel client = (SocketChannel) selectionKey.channel();

            receiveBuffer.clear();
            int read = client.read(receiveBuffer);
            if(read > 0){
                String msg = new String(receiveBuffer.array(),0,read);
                System.out.println("Server received: "+msg);
                //todo 为什么读到内容才注册写呀？
                client.register(selector, SelectionKey.OP_WRITE);
            }
        }else if (selectionKey.isWritable()){

            String msg = "[response] a="+ new Random().nextInt(1000);

            sendBuffer.clear();
            sendBuffer.put(msg.getBytes());
            sendBuffer.flip();

            SocketChannel client = (SocketChannel) selectionKey.channel();
            client.write(sendBuffer);

            System.out.println("Server said:" + msg);

            client.register(selector, SelectionKey.OP_READ);

        }

    }


    public static void main(String[] args) throws IOException {



        NIOServer server = new NIOServer();
        server.listen();

    }
}
