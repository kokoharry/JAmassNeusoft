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
import com.neusoft.soc.nli.jamass.core.AmassConstant;
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
            if(set != null && set.size() >= 1){
                //正常情况下一个IP映射一种设备类型 ,一个IP可以映射多种设备类型，如果存在复杂情况通过正则表达式来判断设备类型
                for(DevPatternInfo devPatternInfo : set){
                    if(devPatternInfo.getPattern() == null || checkDevPattern(devPatternInfo.getPattern(),amassEvent.getRaw())){
                        //没有设置日志全局判定正则,或者设置了正则，通过验证，查找到设备类型
                        amassEvent.setDevice(devPatternInfo.getDevId());
                        amassEvent.setStatus(EventStatus.Identidfied);
                        return true;
                    }
                }
                amassEvent.setErrorMsg(AmassConstant.INDENTIFY_ERROR_MSG_FAIL + amassEvent.getIp0());
            }else{
                amassEvent.setErrorMsg(AmassConstant.INDENTIFY_ERROR_MSG_NO_DEVPATTERN + amassEvent.getIp0());
            }
        }else{
            amassEvent.setErrorMsg(AmassConstant.INDENTIFY_ERROR_MSG_NO_IP + amassEvent.getIp0());
        }
        //映射关系中无法识别的设备类型，为 未知日志
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
        while(true){
            try {
                AmassEvent amassEventTemp = AmassEngine.getInstance().getEventIdentifyQueue().take();
                if(doIdentify(amassEventTemp)){
                    //识别成功
                    IParser parser = ParseFactory.createParser(amassEventTemp);
                    AmassEngine.getInstance().getParsePool().execute(parser);
                }else{
                    //识别失败
                    if(amassEventTemp.getStatus() == EventStatus.Unknown){
                        if(!AmassEngine.getInstance().getUnknowLogQueue().offer(amassEventTemp)){
                            //提交未知队列失败，未知日志队列缓冲满了 未知太多或者没有未知处理sink
                            logger.debug("未知队列已经饱和，不继续进行保存，位置队列长度：" + AmassEngine.getInstance().getUnknowLogQueue().size());
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.error("设备识别线程异常",e);
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
