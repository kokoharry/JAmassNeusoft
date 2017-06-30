package com.neusoft.soc.nli.jamass.channel.receive;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import com.neusoft.soc.nli.jamass.util.NetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * ������־ �ӿ�
 * Created by luyb on 2017/6/5.
 */
public abstract class IReceiver implements Runnable{

    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(IReceiver.class);
    /**
     * ����ԴIP
     */
    private String ip;
    /**
     * ����Դ�˿�
     */
    private int port;
    /**
     * ԭʼ��־��Ϣ
     */
    private String message;
    /**
     * ������־,��װ��ͬЭ����շ���
     *
     * @return ���յ�����־��Ϣ
     */
    public void receive(){
    }

    public void cleanReceiver(){
        this.setIp("");
        this.setPort(0);
        this.setMessage("");
    }

    /**
     * ���ݽ�����־ԭ������ AmassEvent ����
     * @param message
     * @param ip
     * @param port
     * @return
     */
    public AmassEvent createAmassEvent(String message , String ip , int port){
        try {
            // �����ռ������¼�
            AmassEvent amassEvent = new AmassEvent();
            //��־����ʱ���
            amassEvent.setReceiveTime(System.currentTimeMillis());
            //�ж�IP����
            if (NetUtil.isIPv6LiteralAddress(ip)) {
                //IPV6
                ip = NetUtil.normalizeIPV6(NetUtil.textToNumericFormatV6(ip));
                amassEvent.setIpv4Flag(false);
                amassEvent.setAddr0(9999999991l);
            } else {
                //IPV4
                amassEvent.setAddr0(NetUtil.getLongValuefromIPStr(ip));
            }
            amassEvent.setIp0(ip);//����ԴIp
            amassEvent.setSport(port);//����Դ�˿�
            amassEvent.setRaw(message);//��־ԭ��
            amassEvent.setId(UUID.randomUUID().toString());//id uuid �����
            return amassEvent;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void run() {
        receive();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}