package com.neusoft.soc.nli.jamass.channel.receive;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import com.neusoft.soc.nli.jamass.util.NetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * 接收日志 接口
 * Created by luyb on 2017/6/5.
 */
public abstract class IReceiver implements Runnable{

    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(IReceiver.class);
    /**
     * 发生源IP
     */
    private String ip;
    /**
     * 发生源端口
     */
    private int port;
    /**
     * 原始日志信息
     */
    private String message;
    /**
     * 接收日志,封装不同协议接收方法
     *
     * @return 接收到的日志信息
     */
    public void receive(){
    }

    public void cleanReceiver(){
        this.setIp("");
        this.setPort(0);
        this.setMessage("");
    }

    /**
     * 根据接收日志原文生成 AmassEvent 对象
     * @param message
     * @param ip
     * @param port
     * @return
     */
    public AmassEvent createAmassEvent(String message , String ip , int port){
        try {
            // 构造收集代理事件
            AmassEvent amassEvent = new AmassEvent();
            //日志接受时间戳
            amassEvent.setReceiveTime(System.currentTimeMillis());
            //判断IP类型
            if (NetUtil.isIPv6LiteralAddress(ip)) {
                //IPV6
                ip = NetUtil.normalizeIPV6(NetUtil.textToNumericFormatV6(ip));
                amassEvent.setIpv4Flag(false);
                amassEvent.setAddr0(9999999991l);
            } else {
                //IPV4
                amassEvent.setAddr0(NetUtil.getLongValuefromIPStr(ip));
            }
            amassEvent.setIp0(ip);//发生源Ip
            amassEvent.setSport(port);//发生源端口
            amassEvent.setRaw(message);//日志原文
            amassEvent.setId(UUID.randomUUID().toString());//id uuid 带横杠
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