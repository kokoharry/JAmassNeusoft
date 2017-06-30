package com.neusoft.soc.nli.jamass.source;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by luyb on 2017/6/6.
 */
public class UdpSocketSource extends ICollectSource{

    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(UdpSocketSource.class);

    public UdpSocketSource (String threadName,int port){
        this.threadName = threadName;
        this.port = port;
    }

    @Override
    public void messageRecevieHandler(String message,String ip , int port) {
            System.out.println(ip+";"+port+message);
    }

    @Override
    public void run() {
        // 构造接收Socket和packet
        try {
            Thread.currentThread().setName(this.threadName);
            DatagramSocket socket = new DatagramSocket(this.port, InetAddress.getByName("10.2.1.129"));
            socket.setReceiveBufferSize(RECEIVE_BUFF_SIZE);
            DatagramPacket packet = new DatagramPacket(new byte[8192], 8192);
            while(true){
                socket.receive(packet);
                String address = packet.getAddress().getHostAddress();
                int sport = packet.getPort();
                byte[] message = packet.getData();
                autoDecodeHandler(message,address,sport);
                packet.setLength(8192);
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
