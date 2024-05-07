package com.he.javanio.demo02Discard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * 建立连接，发送消息给服务端，关闭连接
 * @author HeXingYun
 * @date 2024/04/01 21:01
 **/
public class NioDiscardClient {

    public static void main(String[] args) throws IOException {
        startClient();
    }

    public static void startClient() throws IOException
    {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        //1. 创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open(address);
        //2. 设置非阻塞模式
        socketChannel.configureBlocking(false);
        //自旋等待连接
        while (!socketChannel.finishConnect())
        {
        }
        Logger.getGlobal().info("连接成功");
        //3.设置缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello world".getBytes());
        byteBuffer.flip();
        //4.发送到服务器
        socketChannel.write(byteBuffer);
        socketChannel.shutdownOutput();
        socketChannel.close();
   }
}
