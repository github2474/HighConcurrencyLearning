package com.he.javanio.demo02Discard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * 服务端，SeverSocket监听连接，轮询selectedKeys，ServerSocket接受连接，把连接的通道和IO事件注册到selector
 * @author HeXingYun
 * @date 2024/04/01 20:30
 **/
public class NioDiscardServer {

    public static void main(String[] args) throws IOException {
        startServer();
    }
    public static void startServer() throws IOException {
        //1.获得选择器
        Selector selector = Selector.open();
        //2.创建监听通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //3.设置非阻塞
        serverSocketChannel.configureBlocking(false);
        //4.绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8080));
        Logger.getGlobal().info("服务器启动成功");
        //5.将通道注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6.轮询感兴趣的事件
        while (selector.select() > 0) {
            //7.获取选择键集合
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                //8.获取单个选择键
                SelectionKey selectionKey = selectedKeys.next();
                //9.判断事件类型
                if (selectionKey.isAcceptable()) {
                    //10.如果是连接就绪事件，获取客户端连接通道
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //11.设置非阻塞
                    socketChannel.configureBlocking(false);
                    //12.注册连接通道到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    //13.如果是可读事件，获取通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //14.读取数据,然后丢弃
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int length = 0;
                    while ((length = socketChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        Logger.getGlobal().info("收到客户端数据：" + new String(byteBuffer.array(), 0, length));
                        byteBuffer.clear();
                    }
                    //关闭连接通道
                    socketChannel.close();
                }
                //15.移除选择键
                selectedKeys.remove();
            }
            //16.关闭连接
            serverSocketChannel.close();
        }
    }
}
