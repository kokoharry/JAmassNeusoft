package com.neusoft.soc.nli.jamass.channel.identify;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;

/**
 * ��־ʶ����ӿڣ�ʵ�ֶ���־�����豸��ʶ��
 */
public interface IIdentify extends Runnable{
    /**
     * ��־ʶ��
     * @param amassEvent
     * @return ʶ��ɹ�����true�����򷵻�false
     */
    boolean doIdentify(AmassEvent amassEvent);

}
