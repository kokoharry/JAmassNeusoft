package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;

/**
 * 日志解析，对日志信息进行解析格式化，实现提取日志的相关信息并生成告警事件等
 */
public interface IParser extends Runnable {
    /**
     * 根据日志格式化规则格式化日志
     * 
     * @param amassEvent
     *            事件信息,解析成功后,脚本填充事件解析状态
     * @return 解析成功返回true，否则返回false
     */
    public boolean doParse(AmassEvent amassEvent);
}
