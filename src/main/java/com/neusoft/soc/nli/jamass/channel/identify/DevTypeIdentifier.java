/*-
 * Copyright (c) 2008-2010 Neusoft SOC
 * All rights reserved. 
 * UdpSysLogIdentifier.java
 * Date: 2010-06-11
 * Author: Lanqian Li
 */
package com.neusoft.soc.nli.jamass.channel.identify;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.DevPatternInfo;
import com.neusoft.soc.nli.jamass.bean.EventStatus;
import com.neusoft.soc.nli.jamass.channel.parse.IParser;
import com.neusoft.soc.nli.jamass.channel.parse.ParseFactory;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class DevTypeIdentifier implements IIdentify{

    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(DevTypeIdentifier.class);

    private AmassEvent amassEvent;

    /**
     * 构造方法
     * @param amassEvent
     */
    public DevTypeIdentifier(AmassEvent amassEvent){
        this.setAmassEvent(amassEvent);
    }

    public boolean doIdentify(AmassEvent amassEvent){
        if (amassEvent == null) {
            logger.error("In doIdentify param ce is null");
            return false;
        }
        // 获取IP设备映射关系
        Map<String,Set<DevPatternInfo>> ipDevMaps = AmassEngine.getInstance().getIpDevMap();
        if (ipDevMaps != null && ipDevMaps.size() > 0 && ipDevMaps.containsKey(amassEvent.getIp0())) {
            // 映射关系中含有对应的IP
            Set<DevPatternInfo> set = ipDevMaps.get(amassEvent.getIp0());
            if(set != null && set.size() == 1){
                //正常情况下一个IP映射一种设备类型 for只是执行一次，为了方便获取set中的值
                for(DevPatternInfo devPatternInfo : set){
                    if(devPatternInfo.getPattern() == null || checkDevPattern(devPatternInfo.getPattern(),amassEvent.getRaw())){
                        //没有设置日志全局判定正则,或者设置了正则，通过验证，查找到设备类型
                        amassEvent.setDevice(devPatternInfo.getDevId());
                        amassEvent.setStatus(EventStatus.Identidfied);
                        return true;
                    }
                }
            }else{
                //TODO:一个IP对应的多个设备类型

            }
        }
        //映射关系中无法识别的设备类型，为 位置日志
        amassEvent.status = EventStatus.Unknown;
        return false;
    }

    /**
     * 使用设备判断正则判定设备是否正确
     *
     * @param pattern 判定正则
     * @param message 日志内容
     * @return 鉴别结果
     */
    private boolean checkDevPattern(Pattern pattern, String message) {
        try{
            if (pattern != null && pattern.matcher(message).find()){
                return true;
            }
        }catch (Exception e){
            logger.error("设备判断正则判定设备类型异常正则表达式为："+pattern.pattern(),e);
        }
        return false;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(doIdentify(this.getAmassEvent())){
            //识别成功
            IParser parser = ParseFactory.createParser(this.getAmassEvent());
            AmassEngine.getInstance().getParsePool().execute(parser);

        }else{
            //识别失败
            if(this.getAmassEvent().getStatus() == EventStatus.Unknown){
                //处理位置日志
            }
        }
    }

    public AmassEvent getAmassEvent() {
        return amassEvent;
    }

    public void setAmassEvent(AmassEvent amassEvent) {
        this.amassEvent = amassEvent;
    }
}
