package com.neusoft.soc.nli.jamass.channel.receive;

import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReceiverFactory {
    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(ReceiverFactory.class);
    /**
     * 
     * @param protocol Э��
     * @return IReiceiver Э�������
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
