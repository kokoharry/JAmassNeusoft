package com.neusoft.soc.nli.jamass.channel.receive;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import com.neusoft.soc.nli.jamass.channel.identify.DevTypeIdentifier;
import com.neusoft.soc.nli.jamass.channel.identify.IIdentify;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SysLogReceiver Syslog协议接受初步处理类 赋值 接收时间，接受协议，Ip，端口，日志原文信息
 * Created by luyb on 2017/6/5.
 */
public class SysLogReceiver extends IReceiver {
    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(SysLogReceiver.class);

    @Override
    public void receive() {
        AmassEvent amassEvent = createAmassEvent(getMessage(),getIp(),getPort());
        if(amassEvent != null){
            amassEvent.setReceiveProtocol(ProtocolType.SysLog);
        }
        IIdentify identify = new DevTypeIdentifier(amassEvent);
        //启动识别线程
        AmassEngine.getInstance().getIdentifyPool().execute(identify);
    }

}
