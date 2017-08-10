package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ������ʽ���Ĺ�����
 */
public class ParseFactory {
    /**
     * ��־��
     */
    public static Logger logger = LogManager.getLogger(ParseFunction.class);
    /**
     * ��ʼ��UdpSysLog��־��ʽ���ಢ����,Ŀǰ֧��SnmpTrap��־��ʽ����
     * 
     * @return ��־��ʽ��������ʧ���򷵻�null
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
