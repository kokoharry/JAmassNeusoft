package com.neusoft.soc.nli.jamass.source;

import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import com.neusoft.soc.nli.jamass.channel.receive.IReceiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.UnsupportedEncodingException;

/**
 * Created by luyb on 2017/6/5.
 */
public abstract class ICollectSource implements Runnable {
    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(ICollectSource.class);
    /**
     * 接受线程名字
     */
    public String threadName = "SyslogUdpAmass";
    /**
     * 监控端口号
     */
    public int port = 514;
    /**
     * 接收消息缓冲区大小 B
     */
    public static final int RECEIVE_BUFF_SIZE = 1024 * 1024 * 4;
    /**
     * 日志接收初步处理方法
      */
    public IReceiver receiver;
    /**
     * 日志接收初步处理方法
     */
    public ProtocolType protocolType;
    /**
     * 接收到信息处理方法
     *
     * @param message
     */
    public void messageRecevieHandler(String message, String ip, int port) {}

    /**
     * 收集自动转码处理类
     * @param message
     * @param ip
     * @param port
     */
    public void autoDecodeHandler(byte[] message, String ip, int port) {
        String messageStr = "";
        try {
            messageStr = new String(message,encodingHandleData(message));
        } catch (UnsupportedEncodingException e) {
            logger.error("自动转换编码异常",e);
            return;
        }
        messageRecevieHandler(messageStr,ip,port);
    }

    /**
     * 根据字节信息获取当前字节信息的编码
     * @param bytes 字节信息
     * @return
     */
    public static String encodingHandleData(byte[] bytes) {
        //设置默认编码，通过获取系统编码
        String DEFAULT_ENCODING = System.getProperty("file.encoding");
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }

}
