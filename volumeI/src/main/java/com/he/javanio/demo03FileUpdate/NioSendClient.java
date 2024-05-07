package com.he.javanio.demo03FileUpdate;



import lombok.extern.slf4j.Slf4j;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 使用FileChannel通道读取本地文件，SocketChannel套接字管道向服务器发送文件
 * @author HeXingYun
 * @date 2024/04/01 21:49
 **/
@Slf4j
public class NioSendClient {
    public static void main(String[] args) {

        new NioSendClient().sendFile();

    }


    private final Charset charset = StandardCharsets.UTF_8;
    /*
    * 向服务端传输文件
    * */
    public void sendFile() {
        String sourceFile = "cn\\highconcurrency\\volumeI\\javanio\\demo03FileIO\\ClientFile\\aaa.txt";
        log.debug("srcPath=" + sourceFile);
        log.info("文件");
        log.info("发送文件");

    }


}
