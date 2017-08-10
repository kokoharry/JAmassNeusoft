package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.EventStatus;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import com.neusoft.soc.nli.jamass.util.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

/**
 * ��־����������־��Ϣ���н�����ʽ����ʵ����ȡ��־�������Ϣ�����ɸ澯�¼���
 */
public class SyslogParser implements IParser {
    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(SyslogParser.class);

    private AmassEvent amassEvent;

    public SyslogParser(AmassEvent amassEvent){
        this.amassEvent = amassEvent;
    }
    /**
     * ������־��ʽ�������ʽ����־
     * 
     * @param amassEvent
     *            �¼���Ϣ,�����ɹ���,�ű�����¼�����״̬
     * @return �����ɹ�����true�����򷵻�false
     */
    public boolean doParse(AmassEvent amassEvent){
        boolean result = false;
        if(AmassEngine.getInstance().getRegularExpressionUtil().hasDeviceType(amassEvent.device)){
            //�Զ�������к��д��豸����
            logger.debug(amassEvent.device+"���豸����ʹ���Զ����������");
            amassEvent = AmassEngine.getInstance().getRegularExpressionUtil().executeExpression(amassEvent);
            if(StringUtils.isNotBlank(amassEvent.parseId)){
                result = true;
            }
            if (result) {
                // �޶�level
                if (true) {

                    // �ض�Ӧip0
                    if (amassEvent.addr0 == 0 && amassEvent.ip0 != null) {
                        amassEvent.addr0 = 9999999991l;
                    }
                    fixIpAddress(amassEvent, "ip1", "addr1");
                    fixIpAddress(amassEvent, "ip2", "addr2");
                    fixIpAddress(amassEvent, "ip3", "addr3");
                }
                //TODO: ��־����
                amassEvent.status = EventStatus.Parsed;
                logger.debug("Logid: " + amassEvent.rawid + " parsed");
                offerLogInfo(amassEvent);
                return result;
            }
        }
        //����ʧ�ܣ�û�д������豸���Զ�������ű������߽�������ʧ��
//        logger.debug("Parse not sucamassEventssful.Id: " + amassEvent.rawid
//                + ", devId=" + amassEvent.device + " type -> " + amassEvent.type
//                + " result-> " + result + " code-> " + amassEvent.code);
        amassEvent.status = EventStatus.Unknown;
        offerLogInfo(amassEvent);
        return result;
    }

    /**
     * ���뵽�¼�queue
     * @param amassEvent
     */
    private void offerLogInfo(AmassEvent amassEvent){
        if(amassEvent.getStatus() == EventStatus.Parsed){
            AmassEngine.getInstance().getEventQueue().offer(amassEvent);
        }else if(amassEvent.getStatus() == EventStatus.Unknown){
            AmassEngine.getInstance().getUnknowLogQueue().offer(amassEvent);
        }
    }

    private void fixIpAddress(AmassEvent amassEvent, String ipField ,String addressFeild){
        try{
            Field fieldIp = amassEvent.getClass().getField(ipField);
            Field fieldAddress = amassEvent.getClass().getField(addressFeild);
            if (fieldIp.get(amassEvent) == null) {
                fieldAddress.set(amassEvent,9999999990l);
            } else if (NetUtil.isIPv6LiteralAddress(fieldIp.get(amassEvent).toString())) {
                fieldAddress.set(amassEvent,9999999991l);
                fieldIp.set(amassEvent,
                        NetUtil.normalizeIPV6(NetUtil.textToNumericFormatV6(fieldIp.get(amassEvent).toString())));
            } else {
                fieldAddress.set(amassEvent,NetUtil.getLongValuefromIPStr(fieldIp.get(amassEvent).toString()));
            }
        }catch (Exception e){
            logger.error("IP Address ת���쳣",e);
        }
    }

    @Override
    public void run() {
        doParse(this.amassEvent);
        AmassEngine.getInstance().count.incrementAndGet();
    }
}
