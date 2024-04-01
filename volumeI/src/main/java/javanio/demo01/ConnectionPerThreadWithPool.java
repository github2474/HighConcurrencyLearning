package javanio.demo01;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务器端 同步阻塞I/O处理（也就是BIO，Blocking I/O）的参考代码
 * @author HeXingYun
 * @date 2024/03/30 10:16
 **/
public class ConnectionPerThreadWithPool implements Runnable{
    @Override
    public void run() {
        //线程池
        //注意，生产环境不能这么用
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        try {
        //服务器监听 socket
            ServerSocket serverSocket = new ServerSocket(8080);
            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                //接收一个连接后，为 socket 连接，新建一个专属的
                Handler handler = new Handler(socket);
                //创建新线程来 handle
                new Thread(handler).start();
                //或者，使用线程池来处理
                //executorService.execute(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class Handler implements Runnable {
        final Socket socket;
        Handler(Socket s)
        {
            socket = s;
        }
        Boolean ioCompleted = false;
        @Override
        public void run() {
            //死循环处理读写事件
            while (!ioCompleted) {
                try {
                    //处理读写事件
                    byte[] input = new byte[1024];
                    socket.getInputStream().read(input);
                    ioCompleted = true;
                    //处理业务逻辑，获得处理结果
                    byte[] output = new String("Hello!").getBytes();
                    socket.getOutputStream().write(output);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(output);
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

