package com.neusoft.soc.nli.jamass.source;

import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import com.neusoft.soc.nli.jamass.channel.receive.ReceiverFactory;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by luyb on 2017/6/14.
 */
public class TcpNettySource extends ICollectSource{
    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(TcpNettySource.class);

    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private  String IP = "127.0.0.1";

    public TcpNettySource(String threadName,int port,ProtocolType protocolType){
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
    public void run(){
        Thread.currentThread().setName(threadName);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast(new TcpServerHandler());
            }
        });
        try {
            b.bind(IP, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class TcpServerHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//            logger.debug("SERVER接收到消息:"+msg+ ctx.channel().remoteAddress());
            autoDecodeHandler(msg.toString().getBytes(),ctx.channel().remoteAddress().toString().split(":")[0],
                    Integer.parseInt(ctx.channel().remoteAddress().toString().split(":")[1]));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.warn("Unexpected exception from downstream.", cause);
            ctx.close();
        }
    }
}
