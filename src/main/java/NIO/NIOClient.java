package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOClient {

    public NIOClient(String clientId) {
        this.clinentId = clientId;
    }

    public String clinentId = "default";

    private int bufferSize = 4096;

    private ByteBuffer recieveBuffer = ByteBuffer.allocate(bufferSize);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(bufferSize);

    private final InetSocketAddress server = new InetSocketAddress("localhost",10010);

    public void work() {

        try {
            //打开socket通道
            SocketChannel channel = SocketChannel.open();
            //设置为非阻塞
            channel.configureBlocking(false);
            //打开选择器
            Selector selector = Selector.open();
            //注册连接事件
            channel.register(selector, SelectionKey.OP_CONNECT);

            //开始连接服务端
            channel.connect(server);

            //开始一个死循环处理事件
            //todo 如何正常的退出呢？
            while (true) {

                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();
                    SocketChannel client = (SocketChannel) key.channel();
                    if (key.isConnectable()) {
                        System.out.println("client:" + clinentId + " has been connected");
                        if (client.isConnectionPending()) {
                            client.finishConnect();

                            sendBuffer.clear();
                            sendBuffer.put(("[client ack] client:" + clinentId + " finished connection.").getBytes());
                            sendBuffer.flip();

                            //todo 这里已经可以写入了吗？
                            client.write(sendBuffer);
                            client.register(selector, SelectionKey.OP_READ);

                        }

                    } else if (key.isReadable()) {

                        //可以读的情况
                        recieveBuffer.clear();
                        int read = channel.read(recieveBuffer);

                        if (read > 0) {
                            String msg = new String(recieveBuffer.array(), 0, read);
                            System.out.println("[client received] " + msg);

                            //注册写事件
                            client.register(selector, SelectionKey.OP_WRITE);

                        }
                    } else if (key.isWritable()) {

                        System.out.println("Client now can speak:");
                        Scanner scanner = new Scanner(System.in);
                        String msg = scanner.nextLine();
                        msg = "[client " + clinentId+" ]" + msg;
                        sendBuffer.clear();
                        sendBuffer.put(msg.getBytes());
                        sendBuffer.flip();
                        client.write(sendBuffer);

                        System.out.println("client " + clinentId + " said]" + msg);

                        client.register(selector,SelectionKey.OP_READ);

                    }


                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) throws IOException {

        NIOClient client = new NIOClient("0001");

        client.work();
    }
}
