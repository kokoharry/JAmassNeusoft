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
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(DevTypeIdentifier.class);

    private AmassEvent amassEvent;

    public boolean doIdentify(AmassEvent amassEvent){
        if (amassEvent == null) {
            logger.error("In doIdentify param ce is null");
            return false;
        }
        // ��ȡIP�豸ӳ���ϵ
        Map<String,Set<DevPatternInfo>> ipDevMaps = AmassEngine.getInstance().getIpDevMap();
        if (ipDevMaps != null && ipDevMaps.size() > 0 && ipDevMaps.containsKey(amassEvent.getIp0())) {
            // ӳ���ϵ�к��ж�Ӧ��IP
            Set<DevPatternInfo> set = ipDevMaps.get(amassEvent.getIp0());
            if(set != null && set.size() >= 1){
                //���������һ��IPӳ��һ���豸���� ,һ��IP����ӳ������豸���ͣ�������ڸ������ͨ��������ʽ���ж��豸����
                for(DevPatternInfo devPatternInfo : set){
                    if(devPatternInfo.getPattern() == null || checkDevPattern(devPatternInfo.getPattern(),amassEvent.getRaw())){
                        //û��������־ȫ���ж�����,��������������ͨ����֤�����ҵ��豸����
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
        //ӳ���ϵ���޷�ʶ����豸���ͣ�Ϊ δ֪��־
        amassEvent.status = EventStatus.Unknown;
        return false;
    }

    /**
     * ʹ���豸�ж������ж��豸�Ƿ���ȷ
     *
     * @param pattern �ж�����
     * @param message ��־����
     * @return ������
     */
    private boolean checkDevPattern(Pattern pattern, String message) {
        try{
            if (pattern != null && pattern.matcher(message).find()){
                return true;
            }
        }catch (Exception e){
            logger.error("�豸�ж������ж��豸�����쳣������ʽΪ��"+pattern.pattern(),e);
        }
        return false;
    }

    @Override
    public void run() {
        while(true){
            try {
                AmassEvent amassEventTemp = AmassEngine.getInstance().getEventIdentifyQueue().take();
                if(doIdentify(amassEventTemp)){
                    //ʶ��ɹ�
                    IParser parser = ParseFactory.createParser(amassEventTemp);
                    AmassEngine.getInstance().getParsePool().execute(parser);
                }else{
                    //ʶ��ʧ��
                    if(amassEventTemp.getStatus() == EventStatus.Unknown){
                        if(!AmassEngine.getInstance().getUnknowLogQueue().offer(amassEventTemp)){
                            //�ύδ֪����ʧ�ܣ�δ֪��־���л������� δ֪̫�����û��δ֪����sink
                            logger.debug("δ֪�����Ѿ����ͣ����������б��棬λ�ö��г��ȣ�" + AmassEngine.getInstance().getUnknowLogQueue().size());
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.error("�豸ʶ���߳��쳣",e);
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
