package com.neusoft.soc.nli.jamass.channel.receive;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import com.neusoft.soc.nli.jamass.channel.identify.DevTypeIdentifier;
import com.neusoft.soc.nli.jamass.channel.identify.IIdentify;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SysLogReceiver SyslogЭ����ܳ��������� ��ֵ ����ʱ�䣬����Э�飬Ip���˿ڣ���־ԭ����Ϣ
 * Created by luyb on 2017/6/5.
 */
public class SysLogReceiver extends IReceiver {
    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(SysLogReceiver.class);

    @Override
    public void receive() {
        AmassEvent amassEvent = createAmassEvent(getMessage(),getIp(),getPort());
        if(amassEvent != null){
            amassEvent.setReceiveProtocol(ProtocolType.SysLog);
        }
        IIdentify identify = new DevTypeIdentifier(amassEvent);
        //����ʶ���߳�
        AmassEngine.getInstance().getIdentifyPool().execute(identify);
    }

}
