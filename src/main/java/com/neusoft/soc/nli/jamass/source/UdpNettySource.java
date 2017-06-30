package com.neusoft.soc.nli.jamass.source;

import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import com.neusoft.soc.nli.jamass.channel.receive.ReceiverFactory;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by luyb on 2017/6/5.
 */
public class UdpNettySource extends ICollectSource {

    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(UdpNettySource.class);

    public UdpNettySource (String threadName,int port,ProtocolType protocolType){
        this.threadName = threadName;
        this.port = port;
        this.protocolType = protocolType;
    }

    @Override
    public void messageRecevieHandler(String message,String ip , int port) {
        try {
            receiver = ReceiverFactory.createReceiver(protocolType);
            if(receiver != null){
                receiver.setMessage(message);
                receiver.setIp(ip);
                receiver.setPort(port);
                AmassEngine.getInstance().getReceiverPool().execute(receiver);
            }
        } catch (Exception e) {
            logger.error("创建接收线程异常",e);
        }
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().setName(this.threadName);
            Bootstrap bootstrap = new Bootstrap();
            EventLoopGroup group = new NioEventLoopGroup();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_RCVBUF, RECEIVE_BUFF_SIZE)
                    .handler(new UdpServerHandler());
            Channel channel = bootstrap.bind(this.port).sync().channel();
            channel.closeFuture().await();
        } catch (Exception e) {
            logger.error("syslog udp 接受日志线程异常",e);
        }
    }

    private class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
            ByteBuf buf = msg.copy().content();
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            autoDecodeHandler(req,msg.sender().getAddress().getHostAddress(),msg.sender().getPort());
            buf.release();
        }
    }
}
