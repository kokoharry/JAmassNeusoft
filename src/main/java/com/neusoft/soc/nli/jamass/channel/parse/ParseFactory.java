package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 解析格式化的工厂类
 */
public class ParseFactory {
    /**
     * 日志类
     */
    public static Logger logger = LogManager.getLogger(ParseFunction.class);
    /**
     * 初始化UdpSysLog日志格式化类并返回,目前支持SnmpTrap日志格式化类
     * 
     * @return 日志格式化对象，若失败则返回null
     */
    public static IParser createParser(AmassEvent amassEvent) {
        switch (amassEvent.getReceiveProtocol()) {
        case SysLog:
            return new SyslogParser(amassEvent);
        case SnmpTrap:
            return  new SyslogParser(amassEvent);
        default:
            return null;
        }
    }
}
