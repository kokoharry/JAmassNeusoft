package com.neusoft.soc.nli.jamass.channel.receive;

import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReceiverFactory {
    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(ReceiverFactory.class);
    /**
     * 
     * @param protocol 协议
     * @return IReiceiver 协议接收类
     */
    public static IReceiver createReceiver(ProtocolType protocol) {
        switch (protocol) {
        case SysLog:
            return new SysLogReceiver();
        case SnmpTrap:
           return new SysLogReceiver();
        case FileTrace:
            return new SysLogReceiver();
        default:
            return null;
        }
    }
}
