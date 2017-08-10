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
 * 日志解析，对日志信息进行解析格式化，实现提取日志的相关信息并生成告警事件等
 */
public class SyslogParser implements IParser {
    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(SyslogParser.class);

    private AmassEvent amassEvent;

    public SyslogParser(AmassEvent amassEvent){
        this.amassEvent = amassEvent;
    }
    /**
     * 根据日志格式化规则格式化日志
     * 
     * @param amassEvent
     *            事件信息,解析成功后,脚本填充事件解析状态
     * @return 解析成功返回true，否则返回false
     */
    public boolean doParse(AmassEvent amassEvent){
        boolean result = false;
        if(AmassEngine.getInstance().getRegularExpressionUtil().hasDeviceType(amassEvent.device)){
            //自定义解析中含有此设备类型
            logger.debug(amassEvent.device+"此设备类型使用自定义解析处理");
            amassEvent = AmassEngine.getInstance().getRegularExpressionUtil().executeExpression(amassEvent);
            if(StringUtils.isNotBlank(amassEvent.parseId)){
                result = true;
            }
            if (result) {
                // 修订level
                if (true) {

                    // 重对应ip0
                    if (amassEvent.addr0 == 0 && amassEvent.ip0 != null) {
                        amassEvent.addr0 = 9999999991l;
                    }
                    fixIpAddress(amassEvent, "ip1", "addr1");
                    fixIpAddress(amassEvent, "ip2", "addr2");
                    fixIpAddress(amassEvent, "ip3", "addr3");
                }
                //TODO: 日志过滤
                amassEvent.status = EventStatus.Parsed;
                logger.debug("Logid: " + amassEvent.rawid + " parsed");
                offerLogInfo(amassEvent);
                return result;
            }
        }
        //解析失败，没有此类型设备的自定义解析脚本，或者解析过程失败
//        logger.debug("Parse not sucamassEventssful.Id: " + amassEvent.rawid
//                + ", devId=" + amassEvent.device + " type -> " + amassEvent.type
//                + " result-> " + result + " code-> " + amassEvent.code);
        amassEvent.status = EventStatus.Unknown;
        offerLogInfo(amassEvent);
        return result;
    }

    /**
     * 加入到事件queue
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
            logger.error("IP Address 转换异常",e);
        }
    }

    @Override
    public void run() {
        doParse(this.amassEvent);
        AmassEngine.getInstance().count.incrementAndGet();
    }
}
