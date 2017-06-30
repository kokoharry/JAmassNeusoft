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
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(ICollectSource.class);
    /**
     * �����߳�����
     */
    public String threadName = "SyslogUdpAmass";
    /**
     * ��ض˿ں�
     */
    public int port = 514;
    /**
     * ������Ϣ��������С B
     */
    public static final int RECEIVE_BUFF_SIZE = 1024 * 1024 * 4;
    /**
     * ��־���ճ���������
      */
    public IReceiver receiver;
    /**
     * ��־���ճ���������
     */
    public ProtocolType protocolType;
    /**
     * ���յ���Ϣ������
     *
     * @param message
     */
    public void messageRecevieHandler(String message, String ip, int port) {}

    /**
     * �ռ��Զ�ת�봦����
     * @param message
     * @param ip
     * @param port
     */
    public void autoDecodeHandler(byte[] message, String ip, int port) {
        String messageStr = "";
        try {
            messageStr = new String(message,encodingHandleData(message));
        } catch (UnsupportedEncodingException e) {
            logger.error("�Զ�ת�������쳣",e);
            return;
        }
        messageRecevieHandler(messageStr,ip,port);
    }

    /**
     * �����ֽ���Ϣ��ȡ��ǰ�ֽ���Ϣ�ı���
     * @param bytes �ֽ���Ϣ
     * @return
     */
    public static String encodingHandleData(byte[] bytes) {
        //����Ĭ�ϱ��룬ͨ����ȡϵͳ����
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
