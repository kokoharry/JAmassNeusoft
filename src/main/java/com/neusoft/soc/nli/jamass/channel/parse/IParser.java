package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;

/**
 * ��־����������־��Ϣ���н�����ʽ����ʵ����ȡ��־�������Ϣ�����ɸ澯�¼���
 */
public interface IParser extends Runnable {
    /**
     * ������־��ʽ�������ʽ����־
     * 
     * @param amassEvent
     *            �¼���Ϣ,�����ɹ���,�ű�����¼�����״̬
     * @return �����ɹ�����true�����򷵻�false
     */
    public boolean doParse(AmassEvent amassEvent);
}
