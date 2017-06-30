package com.neusoft.soc.nli.jamass.channel.identify;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;

/**
 * 日志识别类接口，实现对日志所属设备的识别
 */
public interface IIdentify extends Runnable{
    /**
     * 日志识别
     * @param amassEvent
     * @return 识别成功返回true，否则返回false
     */
    boolean doIdentify(AmassEvent amassEvent);

}
